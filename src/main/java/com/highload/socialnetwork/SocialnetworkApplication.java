package com.highload.socialnetwork;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SocialnetworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialnetworkApplication.class, args);
        System.out.println(System.getProperty("com.sun.management.jmxremote"));
        System.out.println(System.getProperty("com.sun.management.jmxremote.port"));
        System.out.println(System.getProperty("com.sun.management.jmxremote.authenticate"));
        System.out.println(System.getProperty("com.sun.management.jmxremote.ssl"));
        System.out.println(System.getProperty("java.rmi.server.hostname"));
    }
}
