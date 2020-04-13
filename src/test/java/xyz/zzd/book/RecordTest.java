package xyz.zzd.book;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.zzd.book.entity.Book;
import xyz.zzd.book.entity.Record;
import xyz.zzd.book.entity.User;
import xyz.zzd.book.mapper.BookMapper;
import xyz.zzd.book.mapper.RecordMapper;
import xyz.zzd.book.mapper.UserMapper;

import java.util.List;
@SpringBootTest
@RunWith(SpringRunner.class)
public class RecordTest {
    @Autowired
    RecordMapper recordMapper;
    @Autowired
    BookMapper bookMapper;
    @Autowired
    UserMapper userMapper;

    @Test
    public void lamTest(){
//        List<Record> records = recordMapper.selectList(null);
//        records.stream().filter(record -> record.getIsReturn()==0).forEach(System.out::println);
        List<Book> books = bookMapper.selectList(null);
        books.stream().filter(book -> book.getBReserve()==1).forEach(System.out::println);
//        List<Record> records = recordMapper.selectList(new LambdaQueryWrapper<Record>()
//                .eq(Record::getRSid, 1600502101)
//        );
//        records.stream().filter(record -> record.getIsReturn()==0).forEach(System.out::println);
    }

}
