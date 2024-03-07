package com.man.Init;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@MapperScan("com.man.mapper")
@ComponentScan(basePackages = {"com.*","com.man.config"})
@SpringBootApplication()
@RestControllerAdvice
public class Boot3initApplication {
    public static void main(String[] args) {
        SpringApplication.run(Boot3initApplication.class, args);
    }
}
