package xyz.zzd.book.adminController;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.zzd.book.common.GlobalResult;
import xyz.zzd.book.entity.Admin;
import xyz.zzd.book.entity.Book;
import xyz.zzd.book.entity.User;
import xyz.zzd.book.mapper.BookMapper;
import xyz.zzd.book.service.AdminService;

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
}
