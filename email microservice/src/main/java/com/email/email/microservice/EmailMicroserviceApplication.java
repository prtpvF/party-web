package com.email.email.microservice;

import com.email.email.microservice.Services.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class EmailMicroserviceApplication {
	@Autowired
	private EmailSenderService emailSenderServices;
	public static void main(String[] args) {

		SpringApplication.run(EmailMicroserviceApplication.class, args);
	}

}
