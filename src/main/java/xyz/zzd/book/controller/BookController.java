package xyz.zzd.book.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zzd.book.common.GlobalResult;
import xyz.zzd.book.common.RedisUtil;
import xyz.zzd.book.common.RedisUtilForUser;
import xyz.zzd.book.entity.Book;
import xyz.zzd.book.entity.Record;
import xyz.zzd.book.entity.User;
import xyz.zzd.book.mapper.BookMapper;
import xyz.zzd.book.mapper.RecordMapper;
import xyz.zzd.book.mapper.UserMapper;
import xyz.zzd.book.service.BookService;
import xyz.zzd.book.service.RecordService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class BookController {
    @Autowired
    BookService bookService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisUtilForUser redisUtil;


    @PostMapping("/searchBook")
    public GlobalResult searchBook(String content,String skey){
        List<Book> books = bookService.queryBooks(content);
        return GlobalResult.build(200,"success",books);
    }



}
