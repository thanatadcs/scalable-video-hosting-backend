package com.example.tiktokbackend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;

@RestController
@RequestMapping("/api/upload")
public class UploadVideoController {
    final private String bucketName = "scalable-p2";

    @GetMapping("/url")
    ResponseEntity<URL> getUploadUrl(S3Service s3Service) {
        String randomKeyName = String.valueOf((int)(Math.random() * 100000000));
        return ResponseEntity.ok(s3Service.createPresignedUrl(bucketName, randomKeyName, null, null));
    }
}
