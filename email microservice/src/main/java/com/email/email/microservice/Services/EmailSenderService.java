package com.email.email.microservice.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
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

    public void sendEmail(String toEmail, String subject, String body) {
        sendEmail(toEmail, subject, body);
    }

    public void sendEmail(String toEmail, String subject, String body, List<File> attachments) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("caplyginmihail48@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body);

            if (attachments != null) {
                for (File file : attachments) {
                    helper.addAttachment(file.getName(), file);
                }
            }

            javaMailSender.send(message);
            System.out.println("mail send successfully...");
        } catch (MessagingException e) {
            System.out.println("Error while sending mail ..");
            e.printStackTrace();
        }
    }


}