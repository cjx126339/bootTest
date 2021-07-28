package com.jfcat.boottest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BootTestApplication {

    public static void main(String[] args) {
        System.out.println("master 第一次添加");
        SpringApplication.run(BootTestApplication.class, args);
    }

}
