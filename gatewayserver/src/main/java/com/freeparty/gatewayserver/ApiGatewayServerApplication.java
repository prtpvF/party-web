package com.freeparty.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
@RefreshScope
public class ApiGatewayServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayServerApplication.class, args);
	}

}
