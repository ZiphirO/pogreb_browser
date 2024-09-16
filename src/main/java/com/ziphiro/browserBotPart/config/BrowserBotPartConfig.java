package com.ziphiro.browserBotPart.config;

import com.ziphiro.browserBotPart.values.StV;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Log4j2
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

    @Bean(name = "uploadExecutor")
    public ThreadPoolTaskExecutor uploadExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(5000);
        executor.setThreadNamePrefix("MyAsyncThread-");
        executor.setRejectedExecutionHandler((r, executor1) -> log.warn(StV.TASK_REJECTED));
        executor.initialize();
        return executor;
    }
    @Bean(name = "downloadExecutor")
    public ThreadPoolTaskExecutor downloadExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(5000);
        executor.setThreadNamePrefix("MyAsyncThread-");
        executor.setRejectedExecutionHandler((r, executor1) -> log.warn(StV.TASK_REJECTED));
        executor.initialize();
        return executor;
    }
}
