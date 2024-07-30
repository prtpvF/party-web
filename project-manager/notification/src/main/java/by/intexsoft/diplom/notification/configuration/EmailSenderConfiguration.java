package by.intexsoft.diplom.notification.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@PropertySource("classpath:message.properties")
public class EmailSenderConfiguration {

        @Value("${mail.host}")
        private String mailHost;

        @Value("${mail.port}")
        private int mailPort;

        @Value("${mail.username}")
        private String mailUsername;

        @Value("${mail.password}")
        private String mailPassword;

        @Bean
        public JavaMailSenderImpl mailSender() {
            JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

            javaMailSender.setHost(mailHost);
            javaMailSender.setPort(mailPort);
            javaMailSender.setUsername(mailUsername);
            javaMailSender.setPassword(mailPassword);

            Properties props = javaMailSender.getJavaMailProperties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            return javaMailSender;
        }
}