package com.golflearn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableCircuitBreaker
@SpringBootApplication
public class BackResaleApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackResaleApplication.class, args);
	}

}
