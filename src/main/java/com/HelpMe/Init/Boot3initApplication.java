package com.HelpMe.Init;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * @author 艾莉希雅
 */
@MapperScan("com.HelpMe.**.mapper")
@ComponentScan(basePackages = {"com.*", "com.HelpMe.Config"})
@SpringBootApplication()
@RestControllerAdvice
public class Boot3initApplication {
    public static void main(String[] args) {
        SpringApplication.run(Boot3initApplication.class, args);
    }
}
