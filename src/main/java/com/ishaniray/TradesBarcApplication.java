package com.ishaniray;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TradesBarcApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradesBarcApplication.class, args);
	}
}
