package com.highload.socialnetwork;


import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class SocialnetworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialnetworkApplication.class, args);
    }
}
