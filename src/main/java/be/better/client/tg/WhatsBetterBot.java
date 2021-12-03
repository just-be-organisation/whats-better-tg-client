package be.better.client.tg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"be.better.client.tg"})
public class WhatsBetterBot {

    public static void main(String[] args) {
        SpringApplication.run(WhatsBetterBot.class, args);
    }
}
