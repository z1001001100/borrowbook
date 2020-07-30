package xyz.zzd.book.common;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import xyz.zzd.book.entity.Record;
import xyz.zzd.book.entity.Student;
import xyz.zzd.book.mail.IMailService;
import xyz.zzd.book.mapper.RecordMapper;
import xyz.zzd.book.mapper.StudentMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class ScheduleTask {
    @Autowired
    RecordMapper recordMapper;
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    private IMailService mailService;
    //3.添加定时任务
    @Scheduled(cron = "0 0 0 1/1 * *")
    //每天
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void setOverdue() {
        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
        List<Integer> overdueSids = new ArrayList<>();
        List<Integer> tipSids = new ArrayList<>();
//        查找状态为未归还且未过期记录
        List<Record> records = recordMapper.selectList(new LambdaQueryWrapper<Record>().eq(Record::getIsReturn, 0).eq(Record::getIsOverdue,0));
        records.forEach(record -> {
            //获得当前记录应该归还日期，若过期，则修改过期状态
            //如果应还日在今天之前
            if (record.getReturnTime().toLocalDate().isBefore(LocalDateTime.now().toLocalDate()) ){
                record.setIsOverdue(1);
                recordMapper.updateById(record);
                overdueSids.add(record.getRSid());
            }
            //如果还有五天
            if (record.getReturnTime().toLocalDate().plusDays(5).isEqual(LocalDateTime.now().toLocalDate())){
                tipSids.add(record.getRSid());
            }
        });
        //邮箱提示
//        根据sid查学生邮箱
        overdueSids.forEach(integer -> {
            Student student = studentMapper.selectById(integer);
            mailService.sendSimpleMail("zzd9@foxmail.com","还书通知","君有书未归，今已届期，请速归");
        });
        tipSids.forEach(integer -> {
            Student student = studentMapper.selectById(integer);
            mailService.sendSimpleMail("zzd9@foxmail.com","还书通知","君有书籍未归，又五日当期，请续借或速归");
        });
    }


}
