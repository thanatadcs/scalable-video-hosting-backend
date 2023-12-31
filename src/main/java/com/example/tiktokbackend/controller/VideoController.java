package com.example.tiktokbackend.controller;

import com.example.tiktokbackend.domain.Video;
import com.example.tiktokbackend.repository.VideoRepository;
import com.example.tiktokbackend.service.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Scanner;

@RestController
@RequestMapping("/api/video")
@Slf4j
public class VideoController {
    private VideoRepository videoRepository;
    private S3Service s3Service;

    VideoController(S3Service s3Service, VideoRepository videoRepository) {
        this.s3Service = s3Service;
        this.videoRepository = videoRepository;
    }

    @GetMapping("/{uuid}/playlist.m3u8")
    public ResponseEntity<String> getVideo(@PathVariable String uuid) {
        try {
            String videoPlaylist = s3Service.getObjectString(uuid + "/playlist/playlist.m3u8");
            String presignedPlaylist = createPresignedPlaylist(uuid, videoPlaylist);
            return ResponseEntity.ok(presignedPlaylist);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    private String createPresignedPlaylist(String uuid, String videoPlaylist) {
        StringBuilder modifiedPlaylist = new StringBuilder();
        Scanner scanner = new Scanner(videoPlaylist);
        int index = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.charAt(0) == '#') {
                modifiedPlaylist.append(line);
            } else {
                String presignedChunk = s3Service.getPresignedUrl(
                        uuid + "/playlist/playlist" + index++ + ".ts", 15).toString();
                modifiedPlaylist.append(presignedChunk);
            }
            modifiedPlaylist.append('\n');
        }
        return modifiedPlaylist.toString();
    }

    @GetMapping("/thumbnail/{uuid}")
    public ResponseEntity<String> getVideoThumbnailUrl(@PathVariable String uuid) {
        try {
            String url = s3Service.getPresignedUrl(uuid + "/" + "thumbnail.png", 30).toString();
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/status-all")
    public ResponseEntity<List<Video>> getAllVideoStatus() {
        return ResponseEntity.ok(videoRepository.findAll());
    }

    @GetMapping
    public ResponseEntity<List<Video>> getAllReadyVideo() {
        return ResponseEntity.ok(videoRepository.findVideosByReadyStatus("ready"));
    }

}
