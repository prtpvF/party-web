package by.intexsoft.diplom.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderServices {

        private final JavaMailSender javaMailSender;

        public void sendEmail(String toEmail,
                              String subject,
                              String body) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("caplyginmihail48@gmail.com");
            message.setTo(toEmail);
            message.setText(body);
            message.setSubject(subject);
            javaMailSender.send(message);
            log.info("Email sent to {}", toEmail);
        }
}