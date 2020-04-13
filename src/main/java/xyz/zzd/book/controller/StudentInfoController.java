package xyz.zzd.book.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.zzd.book.common.GlobalResult;
import xyz.zzd.book.entity.Student;
import xyz.zzd.book.entity.User;
import xyz.zzd.book.mapper.UserMapper;
import xyz.zzd.book.service.StudentInfoService;
import xyz.zzd.book.service.StudentService;

@RestController
public class StudentInfoController {
    @Autowired
    StudentInfoService studentInfoService;
    @Autowired
    UserMapper userMapper;

    @PostMapping("/updateMail")
    public GlobalResult updateMail(
            @RequestParam(value = "mail", required = false)String mail,
            @RequestParam (value = "skey", required = false) String skey
    ){
        System.out.println("---updatemail---");
        System.out.println("mail="+mail);
        System.out.println("skey="+skey);
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getSkey, skey));
        Student student = studentInfoService.updateMail(user.getSId(), mail);
        return GlobalResult.build(200,"success",student);
    }

    @PostMapping("/updatePhone")
    public GlobalResult updatePhone(
            @RequestParam(value = "phone", required = false)String phone,
            @RequestParam (value = "skey", required = false) String skey
    ){
        System.out.println("---updatePhone---");
        System.out.println("phone="+phone);
        System.out.println("skey="+skey);
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getSkey, skey));
        Student student = studentInfoService.updatePhone(user.getSId(), phone);
        return GlobalResult.build(200,"success",student);
    }

    @PostMapping("/updatePassword")
    public GlobalResult updatePassword(
            @RequestParam(value = "password", required = false)String password,
            @RequestParam (value = "skey", required = false) String skey
    ){
        System.out.println("---updatePwd---");
        System.out.println("phone="+password);
        System.out.println("skey="+skey);
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getSkey, skey));
        Student student = studentInfoService.updatePassword(user.getSId(), password);
        return GlobalResult.build(200,"success",student);
    }
}
