package xyz.zzd.book.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zzd.book.common.GlobalResult;
import xyz.zzd.book.common.RedisUtilForUser;
import xyz.zzd.book.entity.Record;
import xyz.zzd.book.entity.RecordInfo;
import xyz.zzd.book.entity.User;
import xyz.zzd.book.mapper.UserMapper;
import xyz.zzd.book.service.BookService;
import xyz.zzd.book.service.RecordService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RecordController {
    @Autowired
    RecordService recordService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisUtilForUser redisUtilForUser;
    @Autowired
    BookService bookService;

    @PostMapping("/getBorrowingRecord")
    public List<RecordInfo> getBorrowingRecord(String skey){
        Integer sid = redisUtilForUser.getUserSid(skey);
        return recordService.getBorrowingRecord(sid);
    }

    @PostMapping("/renew")
    public GlobalResult renew(String isbn,String skey){
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getSkey, skey));
        Record result = recordService.renew(isbn, user.getSId());
        if (result.getIsRenew()==0){
            return GlobalResult.errorMsg("fail");
        }else return GlobalResult.build(200,null,result);
    }

    //借书前检查
    @PostMapping("/checkBorrow")
    public GlobalResult checkBorrow(String isbn,String skey){
        Integer userSid = redisUtilForUser.getUserSid(skey);
        //存在这本书？ 还有库存？
        GlobalResult grb = bookService.checkBook(isbn);
        if (grb.isOK()) {
            //存在且有库存 =》 检查学生是否可以借阅这本书
            GlobalResult result = bookService.checkRecord(isbn, userSid);
            if (result.isOK()){
                return GlobalResult.ok(grb.getData());
            }else return GlobalResult.errorTokenMsg(result.getMsg());
        }
        return GlobalResult.errorTokenMsg(grb.getMsg());
    }

    //确认借书
    @PostMapping("/submitBorrow")
    public GlobalResult submitBorrow(String isbn,String skey){
        Integer userSid = redisUtilForUser.getUserSid(skey);
        //减库存
        GlobalResult result1 = bookService.borrowBook(isbn);
        if (result1.isOK()){
            //生成记录
            GlobalResult result = recordService.borrowBook(isbn, userSid);
            return result;
        }else return result1;
    }

    @PostMapping("/checkCode")//验证还书点二维码
    public GlobalResult checkCode(String code){
        System.out.println(code);
        if (code.equals("returning place 2020-04-08T17:08:24.739")){
            return GlobalResult.ok();
        }
        return GlobalResult.errorTokenMsg("error");
    }

    @PostMapping("/checkReturn")
    public GlobalResult checkReturn(String isbn,String skey){
        Integer userSid = redisUtilForUser.getUserSid(skey);
        RecordInfo borrowingRecord = recordService.getBorrowingRecord(userSid, isbn);
        if (borrowingRecord!=null){
            return GlobalResult.ok(borrowingRecord);
        }else return GlobalResult.errorTokenMsg("找不到这本书的在借记录");
    }

    @PostMapping("/submitReturn")
    public GlobalResult submitReturn(String[] Blist, String skey,String code){
        Integer sid = redisUtilForUser.getUserSid(skey);
        System.out.println("here---here");
        if (Blist.length>0){
            GlobalResult result = recordService.submitReturn(Blist, sid, code);
            return GlobalResult.ok(result.getData());
        }
        else return GlobalResult.errorTokenMsg("null list for ISBN");
    }

    @PostMapping("/getAllRecord")
    public GlobalResult getAllRecord(String skey){
        Integer sid = redisUtilForUser.getUserSid(skey);
        List<RecordInfo> allRecord = recordService.getAllRecordById(sid);
        return GlobalResult.ok(allRecord);
    }


}
