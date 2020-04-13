package xyz.zzd.book;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.zzd.book.common.RedisUtil;
import xyz.zzd.book.common.RedisUtilForUser;
import xyz.zzd.book.entity.Book;
import xyz.zzd.book.entity.User;
import xyz.zzd.book.mapper.BookMapper;
import xyz.zzd.book.mapper.UserMapper;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EsTest {
    @Autowired
    BookMapper bookMapper;

    @Test
    public void dataEs(){

        esTemplate.deleteIndex("java");
        esTemplate.createIndex(Book.class);
        esTemplate.putMapping(Book.class);
        Book book = new Book();
        book.setIsbn("111");
        book.setBAuthor("33");
        book.setBName("444");
//        book.setLocation("xuexiao");
//        book.setCallNumber("qwewwqasd");
//        bookRepository.index(book);

//        Iterable<Book> all = bookRepository.findAll();
        System.out.println("---aaaa----");
//        System.out.println(all);
//        all.forEach(System.out::println);
    }

    @Autowired
    ElasticsearchTemplate esTemplate;
    @Test
    public void createIndex(){
        esTemplate.deleteIndex("java");
//        esTemplate.createIndex(Book.class);
//        esTemplate.putMapping(Book.class);
//success
    }


    @Test
    public void getQueryBooks(){
        String name = "德";
        List<Book> books = bookMapper.selectList(new LambdaQueryWrapper<Book>()
                .like(Book::getBName, name)
                .or()
                .like(Book::getIsbn, name)
                .or()
                .like(Book::getBAuthor, name)
//                .or()
//                .like(Book::getBPublishingHouse,name)
        );
        books.forEach(System.out::println);

    }

    @Autowired
    UserMapper userMapper;
    @Resource
    RedisUtil redisUtil;
    @Resource
    RedisUtilForUser redisUtilForUser;
    @Test
    public void setRedisUser(){
        User user = userMapper.selectById("oMrQR5SQmyJJ4jnIBaajjroRe-iI");
        redisUtil.set("user",user);
        Object user1 = redisUtil.get("user");
        System.out.println(user1);

    }

    @Test
    public void getRedisUser(){
        String skey = "c1ff4669-4be6-48e3-835c-c2c4b0501513";
        User user = redisUtilForUser.getUser(skey);
        System.out.println(user.getSId());
        System.out.println("user="+user);
    }




}
