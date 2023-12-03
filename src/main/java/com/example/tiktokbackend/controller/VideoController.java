package com.example.tiktokbackend.controller;

import com.example.tiktokbackend.service.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Scanner;

@RestController
@RequestMapping("/api/video")
@Slf4j
public class VideoController {

    private String bucketName = "scalable-p2";
    private S3Service s3Service;

    VideoController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<String> getVideo(@PathVariable String uuid) {
        try {
            StringBuilder modifiedPlaylist = new StringBuilder();
            String videoPlaylist = s3Service.getObjectString(bucketName, uuid + "/playlist/playlist.m3u8");
            Scanner scanner = new Scanner(videoPlaylist);
            int index = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.charAt(0) == '#') {
                    modifiedPlaylist.append(line);
                } else {
                    String presignedChunk = s3Service.getPresignedUrl(bucketName,
                            uuid + "/playlist/playlist" + index + ".ts").toString();
                    modifiedPlaylist.append(presignedChunk);
                }
                modifiedPlaylist.append('\n');
            }
            return ResponseEntity.ok(modifiedPlaylist.toString());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

}
