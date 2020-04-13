package xyz.zzd.book.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.zzd.book.entity.User;

@Component
public class RedisUtilForUser {
    @Autowired
    RedisUtil redisUtil;

    public User getUser(String skey){
        Object o = redisUtil.get(skey);
        String s = JSON.toJSONString(o);
        JSONObject use = JSON.parseObject(s);
        User user = JSONObject.toJavaObject(use, User.class);
        return user;
    }

    public Integer getUserSid(String skey){
        System.out.println("util-skey="+skey);
        Object o = redisUtil.get(skey);
        String s = JSON.toJSONString(o);
        JSONObject use = JSON.parseObject(s);
        User user = JSONObject.toJavaObject(use, User.class);
        return user.getSId();
    }
}
