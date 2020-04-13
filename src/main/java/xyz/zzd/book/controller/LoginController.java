package xyz.zzd.book.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.zzd.book.common.GlobalResult;
import xyz.zzd.book.common.RedisUtil;
import xyz.zzd.book.entity.Article;
import xyz.zzd.book.entity.Book;
import xyz.zzd.book.entity.Student;
import xyz.zzd.book.mapper.ArticleMapper;
import xyz.zzd.book.service.BookService;
import xyz.zzd.book.service.StudentService;

import java.util.List;

@RestController
public class LoginController{
    @Autowired
    StudentService studentService;



    @PostMapping("/login")
    public GlobalResult wxLogin(
            @RequestParam (value = "sid", required = false)Integer sid,
            @RequestParam (value = "pwd", required = false)Integer pwd,
            @RequestParam (value = "skey" , required = false)String skey
    ){
        System.out.println(sid+"---"+pwd+"---"+skey);

        Student res = studentService.studentLogin(sid, pwd, skey);
//        登录失败
        if (res==null)
        {
            return GlobalResult.errorException("学号或密码输入错误");
        }
        else{
            return GlobalResult.build(200,null,res);
        }
    }

    @Autowired
    BookService bookService;
    @PostMapping("/selectIsbn")
    public GlobalResult selectIsbn(@RequestParam(value = "isbn", required = false)String isbn) {
        System.out.println("isbn"+isbn);
        Book book = bookService.selectBook(isbn);
        System.out.println(book);
        GlobalResult result;
        if (book!=null){
             result = GlobalResult.build(200,null,book);
        }
        else  result = GlobalResult.errorMsg("select null");
        return result;
    }

    @Autowired
    ArticleMapper articleMapper;
    @PostMapping("/getArticle")
    public GlobalResult getArticle(){
        System.out.println("---getArticle---");
        List<Article> articles = articleMapper.selectList(null);
        return GlobalResult.ok(articles);
    }

    @PostMapping("/getArticleById")
    public String getArticleById(String id){
        System.out.println("---getArticleById---");
        Article article = articleMapper.selectById(id);
        return article.getStrings();
    }





}

