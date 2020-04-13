package xyz.zzd.book.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.zzd.book.common.GlobalResult;
import xyz.zzd.book.entity.Admin;
import xyz.zzd.book.mapper.AdminMapper;

import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    AdminMapper adminMapper;

    public GlobalResult login(String account, String password){
        System.out.println(account);
        Admin admin = adminMapper.selectById(account);
        if(admin!= null){
            if (admin.getPassword().equals(password)){
                return GlobalResult.ok(admin);
            }
            else return GlobalResult.errorTokenMsg("密码错误");
        }
        else return GlobalResult.errorTokenMsg("账号错误");
    }
}
