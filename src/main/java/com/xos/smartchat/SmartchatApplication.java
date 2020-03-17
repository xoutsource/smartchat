package com.xos.smartchat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.xos.**.dao")
@SpringBootApplication(
        scanBasePackages = {
                "com.xos.**.controller",
                "com.xos.**.service",
                "com.xos.**.dao",
                "com.xos.**.config"
        }
)
public class SmartchatApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartchatApplication.class, args);
    }

}
