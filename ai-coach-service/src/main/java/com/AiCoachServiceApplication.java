package com.lifesync.aicoach;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AiCoachServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiCoachServiceApplication.class, args);
    }
}