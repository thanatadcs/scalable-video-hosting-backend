package com.example.tiktokbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URL;

@SpringBootApplication
public class TiktokBackendApplication {
    public static void main(String[] args) {
        URL url = S3Service.createPresignedUrl("scalable-p2", "sai", null, null);
        System.out.println(url);

        SpringApplication.run(TiktokBackendApplication.class, args);
    }

}
