package xyz.zzd.book.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.zzd.book.common.GlobalResult;
import xyz.zzd.book.entity.Book;
import xyz.zzd.book.entity.Record;
import xyz.zzd.book.entity.RecordInfo;
import xyz.zzd.book.entity.Student;
import xyz.zzd.book.mapper.BookMapper;
import xyz.zzd.book.mapper.RecordMapper;
import xyz.zzd.book.mapper.StudentMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecordService {
    @Autowired
    RecordMapper recordMapper;
    @Autowired
    BookMapper bookMapper;
    @Autowired
    StudentMapper studentMapper;

    public List<RecordInfo> getBorrowingRecord1(Integer sid) {
        List<RecordInfo> recordInfos = new ArrayList<>();
        List<Record> records = recordMapper.selectList(new LambdaQueryWrapper<Record>().eq(Record::getRSid, sid));
        System.out.println(records);
        records.forEach(record -> {
            //没有归还或没有异常
            System.out.println("第一次");
            if (record.getIsReturn()==0 && record.getIsAbnormal()==0) {

                String rIsbn = record.getRIsbn();
                Book book = bookMapper.selectById(rIsbn);
                LocalDate borrowDate=record.getBorrowTime().toLocalDate();
                LocalDate returnDate=record.getReturnTime().toLocalDate();
                RecordInfo recordInfo = new RecordInfo(record.getRId(),
                        record.getRSid(),
                        record.getRIsbn(),
                        borrowDate,
                        returnDate,
                        record.getIsRenew(),
                        record.getIsOverdue(),
                        book.getBName(),
                        book.getBAuthor(),
                        book.getBPublishingHouse(),
                        book.getBSummary(),
                        book.getBImgPath(),
                        book.getLocation(),
                        book.getCallNumber());
                System.out.println("------recordInfo------");
                System.out.println(recordInfo);
                recordInfos.add(recordInfo);
            }
//            System.out.println(record);
        });
        System.out.println(recordInfos);
        return recordInfos;
    }

    public List<RecordInfo> getBorrowingRecord(Integer sid) {
        List<RecordInfo> recordInfos = new ArrayList<>();
        List<Record> records = recordMapper.selectList(new LambdaQueryWrapper<Record>().eq(Record::getRSid, sid)
                .eq(Record::getIsReturn,0).eq(Record::getIsAbnormal,0));
        System.out.println(records);
        records.forEach(record -> {
            //没有归还或没有异常
            String rIsbn = record.getRIsbn();
            Book book = bookMapper.selectById(rIsbn);
            LocalDate borrowDate=record.getBorrowTime().toLocalDate();
            LocalDate returnDate=record.getReturnTime().toLocalDate();
            RecordInfo recordInfo = new RecordInfo(record.getRId(),
                    record.getRSid(),
                    record.getRIsbn(),
                    borrowDate,
                    returnDate,
                    record.getIsRenew(),
                    record.getIsOverdue(),
                    book.getBName(),
                    book.getBAuthor(),
                    book.getBPublishingHouse(),
                    book.getBSummary(),
                    book.getBImgPath(),
                    book.getLocation(),
                    book.getCallNumber());
            System.out.println("------recordInfo------");
            System.out.println(recordInfo);
            recordInfos.add(recordInfo);
        });
        System.out.println(recordInfos);
        return recordInfos;
    }

    public Record renew(String isbn,Integer sid){
        Record record = recordMapper.selectOne(new LambdaQueryWrapper<Record>().eq(Record::getRIsbn, isbn).eq(Record::getRSid, sid));
        record.setIsRenew(1);
        record.setReturnTime(record.getReturnTime().plusDays(30));
        int i = recordMapper.updateById(record);
        if (i==1){
            return record;
        }else return null;
    }

    public GlobalResult borrowBook(String isbn, Integer sid) {
        LocalDateTime borrowTime = LocalDateTime.now();
        LocalDateTime returnTime = borrowTime.plusDays(30);

        Record record = new Record();
        record.setBorrowTime(borrowTime);
        record.setReturnTime(returnTime);
        record.setRSid(sid);
        record.setRIsbn(isbn);
        recordMapper.insert(record);
        return GlobalResult.ok();
    }

    public Record isBorrowing(String isbn, Integer sid) {
        Record record = recordMapper.selectOne(new LambdaQueryWrapper<Record>()
                .eq(Record::getRIsbn, isbn)
                .eq(Record::getRSid, sid)
                .eq(Record::getIsReturn,0)
        );
        if (record!= null){
            return record;
        }
        return null;
    }

    //归还用,查询指定isbn未归还书籍记录
    public RecordInfo getBorrowingRecord(Integer sid,String isbn) {

        Record record = recordMapper.selectOne(new LambdaQueryWrapper<Record>().eq(Record::getRSid, sid)
                .eq(Record::getIsReturn,0).eq(Record::getIsAbnormal,0).eq(Record::getRIsbn,isbn));
        System.out.println(record);
        String rIsbn = record.getRIsbn();
        Book book = bookMapper.selectById(rIsbn);
        LocalDate borrowDate=record.getBorrowTime().toLocalDate();
        LocalDate returnDate=record.getReturnTime().toLocalDate();
        RecordInfo recordInfo = new RecordInfo(record.getRId(),
                record.getRSid(),
                record.getRIsbn(),
                borrowDate,
                returnDate,
                record.getIsRenew(),
                record.getIsOverdue(),
                book.getBName(),
                book.getBAuthor(),
                book.getBPublishingHouse(),
                book.getBSummary(),
                book.getBImgPath(),
                book.getLocation(),
                book.getCallNumber());
        System.out.println("------recordInfo------");
        System.out.println(recordInfo);
        return recordInfo;
    }

    public GlobalResult submitReturn(String[] bList, Integer sid,String code) {
        //每一本书库存加一
        //对应记录修改归还状态
        //学生积分加一
        Student student = studentMapper.selectById(sid);
        for (int i=0 ; i<bList.length;i++){
            Book book = bookMapper.selectById(bList[i]);
            book.setBReserve(book.getBReserve() +1);
            bookMapper.updateById(book);

            Record record = recordMapper.selectOne(new LambdaQueryWrapper<Record>().eq(Record::getRIsbn, bList[i]).eq(Record::getRSid, sid).eq(Record::getIsReturn,0));
            record.setIsReturn(1);
            record.setMsg(code);
            recordMapper.updateById(record);


            student.setSNewIntegral(student.getSNewIntegral()+1);
            student.setSTotalIntegral(student.getSTotalIntegral()+1);
            studentMapper.updateById(student);
        }
        return GlobalResult.ok(student);
    }

    public List<RecordInfo> getAllRecord(Integer sid) {
        List<RecordInfo> recordInfos = new ArrayList<>();
        List<Record> records = recordMapper.selectList(new LambdaQueryWrapper<Record>().eq(Record::getRSid, sid));
        System.out.println(records);
        records.forEach(record -> {
            String rIsbn = record.getRIsbn();
            Book book = bookMapper.selectById(rIsbn);
            LocalDate borrowDate=record.getBorrowTime().toLocalDate();
            LocalDate returnDate=record.getReturnTime().toLocalDate();
            RecordInfo recordInfo = new RecordInfo(record.getRId(),
                    record.getRSid(),
                    record.getRIsbn(),
                    borrowDate,
                    returnDate,
                    record.getIsRenew(),
                    record.getIsOverdue(),
                    book.getBName(),
                    book.getBAuthor(),
                    book.getBPublishingHouse(),
                    book.getBSummary(),
                    book.getBImgPath(),
                    book.getLocation(),
                    book.getCallNumber());
            recordInfo.setIsAbnormal(record.getIsAbnormal());
            recordInfo.setIsReturn(record.getIsReturn());
            System.out.println("------recordInfo------");
            System.out.println(recordInfo);
            recordInfos.add(recordInfo);
        });
        System.out.println(recordInfos);
        return recordInfos;
    }
}
