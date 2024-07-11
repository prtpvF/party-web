package com.party.service.party_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
public class PartyServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(PartyServiceApplication.class, args);
		System.out.println("Current date and time: " + LocalDateTime.now());
	}

}
