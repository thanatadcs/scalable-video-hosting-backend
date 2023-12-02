package com.example.tiktokbackend.controller;

import com.example.tiktokbackend.repository.VideoRepository;
import com.example.tiktokbackend.domain.UploadTicket;
import com.example.tiktokbackend.domain.Video;
import com.example.tiktokbackend.service.S3Service;
import com.example.tiktokbackend.service.TaskQueueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class UploadVideoController {
    final private String bucketName = "scalable-p2";

    private VideoRepository videoRepository;

    private TaskQueueService taskQueueService;


    UploadVideoController(VideoRepository videoRepository, TaskQueueService taskQueueService) {
        this.videoRepository = videoRepository;
        this.taskQueueService = taskQueueService;
    }

    @CrossOrigin
    @GetMapping("/url")
    ResponseEntity<UploadTicket> getUploadUrl(S3Service s3Service) {
        String uuid = UUID.randomUUID().toString();
        URL uploadUrl = s3Service.createPresignedUploadUrl(bucketName, uuid + "/original", null, null);
        UploadTicket ticket = new UploadTicket(uuid, uploadUrl.toString());
        return ResponseEntity.ok(ticket);
    }

    @CrossOrigin
    @PostMapping("/done")
    ResponseEntity<Void> doneUpload(@RequestBody UploadTicket ticket) {
        Video newVideo = new Video(ticket.uuid(), "NONE");
        videoRepository.save(newVideo);
        taskQueueService.sendTask(String.valueOf(ticket.uuid()));
        return ResponseEntity.ok().build();
    }
}
