package com.rubber.test.web;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.rubber.*")
@SpringBootApplication
public class RubberTestWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(RubberTestWebApplication.class, args);
    }
}
