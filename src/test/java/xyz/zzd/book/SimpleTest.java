package xyz.zzd.book;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.zzd.book.entity.*;
import xyz.zzd.book.mapper.AdminMapper;
import xyz.zzd.book.mapper.BookMapper;
import xyz.zzd.book.mapper.RecordMapper;
import xyz.zzd.book.mapper.StudentMapper;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SimpleTest {

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
//        List<User> userList = userMapper.selectList(null);
//        Assert.assertEquals(5, userList.size());
//        userList.forEach(System.out::println);
    }

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private AdminMapper adminMapper;

    @Test
    public void saveStudent(){
        System.out.println(("----- selectAll method test ------"));
        Integer sNumber = 1600502103;
        String sName = "aaa";

        Student student = new Student();
        student.setSId(sNumber);
        student.setSName(sName);
        student.setSPassword(sNumber.toString());
        int insertResult = studentMapper.insert(student);
        System.out.println(insertResult);
        List<Student> students = studentMapper.selectList(null);
        System.out.println("查询结果");
        students.forEach(System.out::println);

    }

    @Test
    public void saveBook(){
        System.out.println("---saveBookTest---");
        String author = "(卡勒德·胡赛尼（著）李继宏（译）";
        String bName = "追风筝的人";
        String isbn = "9787208061644";
        String bPublishingHouse = "上海人民出版社";
        String imgPath ="http://app2.showapi.com/isbn/img1/eaa363cbced8474e992dea310faf176d.jpg";
        String summary = "许多年过去了，人们说陈年旧事可以被埋葬，然而我终于明白这是错的，因为往事会自行爬上来。回首前尘，我意识到在过去二十六年里，自己始终在窥视着那荒芜的小径。";



        Book book = new Book();
        book.setBAuthor(author);
        book.setBName(bName);
        book.setIsbn(isbn);
        book.setBPublishingHouse(bPublishingHouse);
        book.setBSummary(summary);
        book.setBImgPath(imgPath);

        int insert = bookMapper.insert(book);
        System.out.println("插入结果"+insert);
        List<Book> books = bookMapper.selectList(null);
        books.forEach(System.out::println);
    }


    @Test
    public void saveAdmin(){
        System.out.println("---saveAdminTest---");

        String account = "444";
        String password = "444";

        Admin admin = new Admin();
        admin.setUsername(account);
        admin.setPassword(password);

        System.out.println("插入结果"+adminMapper.insert(admin));
        List<Admin> admins = adminMapper.selectList(null);
        admins.forEach(System.out::println);
    }


    @Autowired
    private RecordMapper recordMapper;

    @Test
    public void saveRecord(){
        System.out.println("---saveRecordTest---");

        Integer sId = 1600502101;
        String isbn = "9787208061644";
        LocalDateTime borrowTime = LocalDateTime.now();
        LocalDateTime returnTime = borrowTime.plusDays(30);
//        System.out.println(localDateTime.isBefore(localDateTime1));
//        System.out.println(localDateTime1.format(DateTimeFormatter.ISO_LOCAL_DATE));

        Record record = new Record();
        record.setBorrowTime(borrowTime);
        record.setReturnTime(returnTime);
        record.setRSid(sId);
        record.setRIsbn(isbn);

        System.out.println("插入结果"+recordMapper.insert(record));
        List<Record> records = recordMapper.selectList(null);
        records.forEach(System.out::println);

    }

//    张三借了第一本书
    @Test
    public void borrowTest(){
        Integer sid = 1600502101;
        String isbn = "9787208061644";
//        生成记录
        LocalDateTime borrowTime = LocalDateTime.now();
        LocalDateTime returnTime = borrowTime.plusDays(30);

        Record record = new Record();
        record.setBorrowTime(borrowTime);
        record.setReturnTime(returnTime);
        record.setRSid(sid);
        record.setRIsbn(isbn);

//        修改库存
        Book book = bookMapper.selectOne(new LambdaQueryWrapper<Book>().eq(Book::getIsbn, isbn));
        System.out.println(book);
        Book book1 = new Book();

//        book.setBReserve(book.getBReserve() - 1);
        book1.setBReserve(book.getBReserve() - 1);
        bookMapper.update(book1, new UpdateWrapper<Book>().lambda().eq(Book::getIsbn,isbn));
    }

//    张三还书
    @Test
    public void returnBook(){
//        还书学号，还书isbn，还书时间
        Integer sid = 1600502101;
        String isbn = "9787208061644";
        LocalDateTime returningTime = LocalDateTime.now();

//        修改还书记录状态
        Record record1 = recordMapper.selectList(new LambdaQueryWrapper<Record>().eq(Record::getRSid, sid).eq(Record::getRIsbn, isbn)).get(0);
        Record record = new Record();
        record.setIsReturn(1);
        recordMapper.update(record,new UpdateWrapper<Record>().lambda().eq(Record::getRSid, sid).eq(Record::getRIsbn, isbn).last("limit 1"));

        //库存
        Book book = new Book();
        Book book1 = bookMapper.selectById(isbn);
        book.setBReserve(book1.getBReserve() + 1);

        //改成只修改第一个记录标记已归还
        Integer update = bookMapper.update(book, new UpdateWrapper<Book>().lambda().eq(Book::getIsbn, isbn).last("limit 1"));
        System.out.println(update.equals(1)?"成功":"失败");
    }

    @Test
    public void smallTest(){
        String isbn = "9787208061644";
        Integer sid = 1600502101;
        Book book = bookMapper.selectById(isbn);
        List<Book> books = bookMapper.selectList(new LambdaQueryWrapper<Book>().eq(Book::getIsbn, isbn).last("limit 2"));
        List<Record> records = recordMapper.selectList(new LambdaQueryWrapper<Record>().eq(Record::getRSid, sid).eq(Record::getRIsbn, isbn).last("limit 1"));
//        System.out.println(book);
        records.forEach(System.out::println);
    }


//    @Autowired
//    RecordMapper recordMapper;
//    @Autowired
//    BookMapper bookMapper;
    @Test
    public void getBorrowingRecord() {
        Integer sid =1600502101;
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
//        return recordInfos;
    }
}



