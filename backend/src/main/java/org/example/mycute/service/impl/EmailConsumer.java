package org.example.mycute.service.impl;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mycute.configuration.RabbitMQConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailConsumer {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine; // 注入 Thymeleaf 模板引擎

    @RabbitListener(queues = RabbitMQConfig.queueName)
    public void processEmailTask(@NotNull Map<String, String> message) {
        String email = message.get("email");
        String code = message.get("code");

        try {
            // 创建 HTML 邮件
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom("1738978509@qq.com");
            helper.setTo(email);
            helper.setSubject("您的验证码");

            // 渲染模板并设置内容
            Context context = new Context();
            context.setVariable("content", code); // 对应模板中的 ${content}
            String htmlContent = templateEngine.process("common", context); // 模板名 common
            helper.setText(htmlContent, true); // true 表示发送 HTML

            mailSender.send(mimeMessage);
            log.info("HTML 邮件发送成功：{}", email);
        } catch (Exception e) {
            log.error("邮件发送失败：{}", e.getMessage());
        }
    }
}
