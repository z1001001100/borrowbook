package xyz.zzd.book;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.zzd.book.mail.IMailService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class mailTest {

    /**
     * 注入发送邮件的接口
     */
    @Autowired
    private IMailService mailService;

    /**
     * 测试发送文本邮件
     */
    @Test
    public void sendmail() {
        mailService.sendSimpleMail("zzd9@foxmail.com","还书通知","君有书籍未归，又五日当期，请续借或速归");
    }
}
