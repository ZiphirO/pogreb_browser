package com.ziphiro.browserBotPart;

import com.ziphiro.browserBotPart.entityes.UserFile;
import com.ziphiro.browserBotPart.service.UserFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@EnableAsync
public class BrowserBotPartApplication {

	public static void main(String[] args) {
		SpringApplication.run(BrowserBotPartApplication.class, args);
	}


}
