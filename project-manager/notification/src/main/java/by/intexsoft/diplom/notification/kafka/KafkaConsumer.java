package by.intexsoft.diplom.notification.kafka;

import by.intexsoft.diplom.notification.service.EmailSenderServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

        private final EmailSenderServices emailSenderServices;

        @Value("${auth.registration.message}")
        private String registrationMessage;

        @KafkaListener(topics = "${spring.kafka.topic-auth.name}", groupId = "auth-group")
        public void listenRegistrationMessage(String toEmail){
                emailSenderServices.sendEmail(toEmail, "Welcome!", registrationMessage);
                log.info("Message sent to email: {}", toEmail);
        }

        @KafkaListener(topics = "${spring.kafka.topic-verify.name}", groupId = "auth-group")
        public void listenVerifyCodeMessage(KafkaMessageModel messageModelDto){
                emailSenderServices.sendEmail(messageModelDto.getToEmail(),
                        "Don't trust anybody!",
                        messageModelDto.getVerificationCode());
                log.info("Message sent to email: {}", messageModelDto.getToEmail());
        }
}