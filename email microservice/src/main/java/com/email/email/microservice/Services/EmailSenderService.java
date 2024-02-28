package com.email.email.microservice.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailSenderService {
    private final JavaMailSender javaMailSender;
    @Value("spring.mail.username")
    private  String from;

    public void sendEmail(String toEmail, String subject, String body) {
       SimpleMailMessage message = new SimpleMailMessage();
       message.setFrom(from);
       message.setSubject(subject);
       message.setTo(toEmail);
       message.setText(body);
       javaMailSender.send(message);
    }

//    public void sendEmail(String toEmail, String subject, String body, List<File> attachments) {
//        try {
//            MimeMessage message = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//            helper.setFrom("caplyginmihail48@gmail.com");
//            helper.setTo(toEmail);
//            helper.setSubject(subject);
//            helper.setText(body);
//
//            if (attachments != null) {
//                for (File file : attachments) {
//                    helper.addAttachment(file.getName(), file);
//                }
//            }
//
//            javaMailSender.send(message);
//            System.out.println("mail send successfully...");
//        } catch (MessagingException e) {
//            System.out.println("Error while sending mail ..");
//            e.printStackTrace();
//        }
  //  }


}