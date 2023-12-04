package com.example.tiktokbackend.controller;

import com.example.tiktokbackend.repository.VideoRepository;
import com.example.tiktokbackend.domain.VideoUploadTicket;
import com.example.tiktokbackend.domain.Video;
import com.example.tiktokbackend.service.S3Service;
import com.example.tiktokbackend.service.TaskQueueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class VideoUploadController {
    private String bucketName = "scalable-p2";
    private S3Service s3Service;
    private VideoRepository videoRepository;
    private TaskQueueService taskQueueService;


    VideoUploadController(VideoRepository videoRepository, TaskQueueService taskQueueService, S3Service s3Service) {
        this.videoRepository = videoRepository;
        this.taskQueueService = taskQueueService;
        this.s3Service = s3Service;
    }

    @GetMapping("/url")
    ResponseEntity<VideoUploadTicket> getUploadUrl() {
        String uuid = UUID.randomUUID().toString();
        URL uploadUrl = s3Service.createPresignedUploadUrl(bucketName, uuid + "/original", null, null);
        VideoUploadTicket ticket = new VideoUploadTicket(uuid, uploadUrl.toString());
        return ResponseEntity.ok(ticket);
    }

    @PostMapping("/done")
    ResponseEntity<Void> doneUpload(@RequestBody String uuid) {
        Video newVideo = new Video(uuid);
        videoRepository.save(newVideo);
        taskQueueService.sendTask(String.valueOf(uuid));
        return ResponseEntity.ok().build();
    }

}
