package com.ziphiro.browserBotPart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BrowserBotPartApplication {

	public static void main(String[] args) {
		SpringApplication.run(BrowserBotPartApplication.class, args);
	}


}
