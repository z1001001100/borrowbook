package xyz.zzd.book;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.zzd.book.common.QRCodeUtil;

import java.time.LocalDateTime;

@SpringBootTest
@RunWith(SpringRunner.class)
public class QRTest {
    @Test
    public void createCode(){
        String content =  "returning place "+ LocalDateTime.now().toString();

        QRCodeUtil.zxingCodeCreate(content, "C:/Users/12031/Desktop/",500,"C:/Users/12031/Desktop/books_select.png");
        System.out.println(content);
    }
}
