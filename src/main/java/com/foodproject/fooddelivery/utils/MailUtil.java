package com.foodproject.fooddelivery.utils;

import com.foodproject.fooddelivery.repository.UsersRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class MailUtil {
    @Autowired
    private JavaMailSender javaMailSender;

    public boolean sendEmail(String toEmail, String subject, String content) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            helper.setFrom("myfoodrestaurant85@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content,true);
            javaMailSender.send(mimeMessage);
            return true;
        } catch (MailException | MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
