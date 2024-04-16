package com.email.email.microservice.Services;

import com.email.email.microservice.EmailSamples.SamplesPars;
import com.email.email.microservice.EmailSamples.Type;
import com.email.email.microservice.Model.EmailParams;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
@RefreshScope
public class EmailSenderService {
    private final JavaMailSender javaMailSender;
    @Value("spring.mail.username")
    private  String from;
    private final SamplesPars samplesPars;

    public void sendEmail(EmailParams emailParams) {
       SimpleMailMessage message = new SimpleMailMessage();
       message.setFrom(from);
       message.setSubject(emailParams.getSubject());
       message.setTo(emailParams.getEmail());
        message.setText(getNecessaryText(emailParams));
       javaMailSender.send(message);
    }

    private String getNecessaryText(EmailParams emailParams){
        String text="";
        if(emailParams.getType()==Type.REQUEST_ANSWER && emailParams.getMessage()==null) {
             text = samplesPars.getSampleTextWithParams(emailParams.getType(), emailParams.getPartyId(), emailParams.getRequestId());
        }
        if(emailParams.getMessage()!=null){
            text = emailParams.getMessage();
        }
        if(emailParams.getType()!=null){
             text = samplesPars.getSampleText(emailParams.getType());
        }
        return text;
    }



}