package com.ziphiro.browserBotPart.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Slf4j
@Configuration
@EnableAsync
public class BrowserBotPartConfig {

    @Bean
    public WebMvcConfigurer corsConfig(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean(name = "asyncDownload")
    public ThreadPoolTaskExecutor downloadExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("downloadThread-");
        executor.setRejectedExecutionHandler((r, executor1) -> log.warn("Task rejected, " +
                "thread pool is full and queue is also full"));
        executor.initialize();
        return executor;
    }
    @Bean(name = "asyncUpload")
    public ThreadPoolTaskExecutor uploadExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("uploadThread-");
        executor.setRejectedExecutionHandler((r, executor1) -> log.warn("Task rejected," +
                "thread pool is full and queue is also full"));
        executor.initialize();
        return executor;
    }


}
