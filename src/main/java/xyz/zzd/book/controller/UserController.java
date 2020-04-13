package xyz.zzd.book.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.zzd.book.common.GlobalResult;
import xyz.zzd.book.common.RedisUtil;
import xyz.zzd.book.common.WechatUtil;
import xyz.zzd.book.entity.Student;
import xyz.zzd.book.entity.User;
import xyz.zzd.book.mapper.StudentMapper;
import xyz.zzd.book.mapper.UserMapper;


import java.time.LocalDateTime;
import java.util.UUID;


@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    RedisUtil redisUtil;
    @PostMapping("/wxlogin")
    public GlobalResult wxLogin(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "rawData", required = false) String rawData
    ) {
        JSONObject rawDataJson = JSON.parseObject(rawData);
        JSONObject SessionKeyOpenId = WechatUtil.getSessionKeyOrOpenId(code);
        String openid = SessionKeyOpenId.getString("openid");
        String sessionKey = SessionKeyOpenId.getString("session_key");
        User user = this.userMapper.selectById(openid);
        String skey = UUID.randomUUID().toString();
        if (user == null) {
            String nickName = rawDataJson.getString("nickName");
            String avatarUrl = rawDataJson.getString("avatarUrl");
            user = new User();
            user.setOpenId(openid);
            user.setSkey(skey);
            user.setCreateTime(LocalDateTime.now());
            user.setLastVisitTime(LocalDateTime.now());
            user.setSession_key(sessionKey);
            user.setAvatarUrl(avatarUrl);
            user.setNickName(nickName);
            redisUtil.set(skey,user,259200);
            System.out.println(user);
            this.userMapper.insert(user);
        }else {
            // 已存在，更新用户登录时间
            user.setLastVisitTime(LocalDateTime.now());
//            // 重新设置会话skey
            user.setSkey(skey);
            redisUtil.set(skey,user,259200);
            System.out.println("skey="+skey);
            this.userMapper.updateById(user);
            }
        //            是否已经绑定学号？
        Integer sId = user.getSId();
        if (sId!=null){
            System.out.println("已绑定");
            Student student = studentMapper.selectById(sId);
            System.out.println("sid="+sId);
            System.out.println("student="+student);

            return GlobalResult.build(200,skey,student);
        }
        else {
            System.out.println("未绑定");
            return  GlobalResult.build(200, skey, null);
        }
    }
}