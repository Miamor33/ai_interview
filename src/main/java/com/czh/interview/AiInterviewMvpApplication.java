package com.czh.interview;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.czh.interview.mapper")
public class AiInterviewMvpApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiInterviewMvpApplication.class, args);
    }
}
