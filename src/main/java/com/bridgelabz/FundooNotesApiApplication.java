package com.bridgelabz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.bridgelabz" })
public class FundooNotesApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FundooNotesApiApplication.class, args);
	}
}