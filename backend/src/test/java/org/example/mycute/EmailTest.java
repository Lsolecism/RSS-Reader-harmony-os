package org.example.mycute;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
public class EmailTest {

    @Autowired
    private JavaMailSender mailSender;

    @Test
    void sendTestMail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("1738978509@qq.com");
        message.setTo("1738978509@qq.com");
        message.setSubject("测试邮件");
        message.setText("这是一封测试邮件");
        mailSender.send(message);
    }
}

