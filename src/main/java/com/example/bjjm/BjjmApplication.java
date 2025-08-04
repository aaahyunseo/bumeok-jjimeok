package com.example.bjjm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BjjmApplication {

    public static void main(String[] args) {
        SpringApplication.run(BjjmApplication.class, args);
    }

}
