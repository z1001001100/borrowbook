package xyz.zzd.book.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.zzd.book.common.RedisUtil;
import xyz.zzd.book.entity.Student;
import xyz.zzd.book.entity.User;
import xyz.zzd.book.mapper.StudentMapper;
import xyz.zzd.book.mapper.UserMapper;

@Service
public class StudentService {
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisUtil redisUtil;

    public Student studentLogin(Integer sid,Integer pwd,String skey){
//        1.找到学生

        Student student = studentMapper.selectOne(new LambdaQueryWrapper<Student>()
                .eq(Student::getSId, sid).eq(Student::getSPassword, pwd));
        if (student!=null){//找到学生
            System.out.println("找到学生");
            System.out.println("studentLogin-Result=>/n"+student);
//        2.微信绑定学生
            User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                    .eq(User::getSkey, skey));
            user.setSId(sid);
            int i = userMapper.updateById(user);
            redisUtil.set(user.getSkey(),user,259200);
            System.out.println("完成绑定");
            System.out.println("i="+i);
            return student;
        }else return null;
    }
}
