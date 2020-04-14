package xyz.zzd.book.adminController;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.zzd.book.common.GlobalResult;
import xyz.zzd.book.entity.Admin;
import xyz.zzd.book.entity.Book;
import xyz.zzd.book.entity.Student;
import xyz.zzd.book.entity.User;
import xyz.zzd.book.mapper.BookMapper;
import xyz.zzd.book.mapper.StudentMapper;
import xyz.zzd.book.service.AdminService;
import xyz.zzd.book.service.BookService;
import xyz.zzd.book.service.RecordService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vue-admin-template")
public class AdminController {
    @Autowired
    AdminService adminService;

    @PostMapping("/user/login")
    public GlobalResult login(@RequestBody Admin admin){
        String account = admin.getUsername();
        String password = admin.getPassword();
        System.out.println(account+"---"+password);
        GlobalResult login = adminService.login(account, password);
        if (login.isOK()){
            String s = "{roles: ['admin'],introduction: 'I am a super administrator',avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif', name: 'Super Admin' }";

            return GlobalResult.build(20000,"ok",JSON.parseObject(s));
//            return GlobalResult.ok(login.getData());
        }else return GlobalResult.errorTokenMsg(login.getMsg());

    }


    @PostMapping("/user/info")
    public GlobalResult getInfo(){
        String s = "{roles: ['admin'],introduction: 'I am a super administrator',avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif', name: 'Super Admin' }";
        return GlobalResult.build(20000,null,JSON.parseObject(s));
    }

    @PostMapping("/user/logout")
    @RequestMapping("/user/logout")
    public GlobalResult logout(){
        System.out.println("get-logout");
        return GlobalResult.build(20000,null,null);
    }

    @Autowired
    BookMapper bookMapper;
    @GetMapping("/table/list")
    public GlobalResult tableTest(){

        System.out.println("---tableList---");
//        System.out.println(params);
        List<Book> books = bookMapper.selectList(null);
        return GlobalResult.build(20000,null,books);
    }

    @PostMapping("/book/edit")
    public GlobalResult editBook(@RequestBody Book book){
        System.out.println(book);
        int i = bookMapper.updateById(book);
        return GlobalResult.success(book);
    }

    @PostMapping("/book/delete")
    public GlobalResult deleteBook(@RequestBody Book book){
        System.out.println(book);
        bookMapper.deleteById(book);
        return GlobalResult.success(book);
    }

    @PostMapping("/book/save")
    public GlobalResult saveBook(@RequestBody Book book){
        System.out.println(book);
        bookMapper.insert(book);
//        bookMapper.
        return GlobalResult.success(book);
    }


    @Autowired
    RecordService recordService;
    @Autowired
    BookService bookService;
    @RequestMapping("/student/borrow")
    public GlobalResult borrow(@RequestParam(required=true) Integer sid,@RequestParam String isbn){
        System.out.println("borrow="+sid+isbn);
        GlobalResult result = bookService.borrowBook(isbn);
        if (result.isOK()){
            recordService.borrowBook(isbn,sid);
            return GlobalResult.success();
        }
        else return GlobalResult.errorTokenMsg("库存空");
    }

    @RequestMapping("/student/return")
    public GlobalResult returnBook(@RequestParam(required=true) Integer sid,@RequestParam String isbn){
        String[] blist =isbn.split(",");
        List<Book> books = bookMapper.selectList(new LambdaQueryWrapper<Book>().eq(Book::getIsbn, isbn));
        recordService.submitReturn(blist,sid,"服务台");
        return GlobalResult.success();
    }

    @RequestMapping("/student/renew")
    public GlobalResult renewBook(@RequestParam(required=true) Integer sid,@RequestParam String isbn){
        String[] blist =isbn.split(",");
        List<Book> books = bookMapper.selectList(new LambdaQueryWrapper<Book>().eq(Book::getIsbn, isbn));
        recordService.submitReturn(blist,sid,"服务台");
        return GlobalResult.success();
    }

    @RequestMapping("/record/getAllList")
    public GlobalResult getAllList(){
        return GlobalResult.success(recordService.getAllRecord());
    }

    @RequestMapping("/record/getSearchList")
    public GlobalResult getSearchList(@RequestParam String strings){
        return GlobalResult.success(recordService.getsearchRecord(strings));
    }

    @Autowired
    StudentMapper studentMapper;

    @RequestMapping("/student/getList")
    public GlobalResult getStudentList(){
        return GlobalResult.success(studentMapper.selectList(null));
    }

    @RequestMapping("/student/edit")
    public GlobalResult editStudent(@RequestBody Student student){
        studentMapper.updateById(student);
        return GlobalResult.success();
    }

    @RequestMapping("/student/save")
    public GlobalResult saveStudent(@RequestBody Student student){
        studentMapper.insert(student);
        return GlobalResult.success();

    }

    @RequestMapping("/student/search")
    public GlobalResult searchStudent(@RequestParam String strings){
        System.out.println(strings);
        List<Student> students = studentMapper.selectList(new LambdaQueryWrapper<Student>()
                .like(Student::getSId, strings).or()
                .like(Student::getSName, strings)
            );
        return GlobalResult.success(students);
    }

    @RequestMapping("/student/delete")
    public GlobalResult deleteStudent(@RequestBody Student student){
        studentMapper.deleteById(student);
        return GlobalResult.success();
    }



}
