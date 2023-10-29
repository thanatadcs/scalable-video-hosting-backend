package com.example.tiktokbackend.controller;

import com.example.tiktokbackend.repository.VideoRepository;
import com.example.tiktokbackend.domain.UploadTicket;
import com.example.tiktokbackend.domain.Video;
import com.example.tiktokbackend.service.S3Service;
import com.example.tiktokbackend.service.TaskQueueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URL;

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
        int id = (int) (Math.random() * 100000000);
        URL uploadUrl = s3Service.createPresignedUploadUrl(bucketName, id + "/original", null, null);
        UploadTicket ticket = new UploadTicket(id, uploadUrl.toString());
        return ResponseEntity.ok(ticket);
    }

    @CrossOrigin
    @PostMapping("/done")
    ResponseEntity<Void> doneUpload(@RequestBody UploadTicket ticket) {
        Video newVideo = new Video(ticket.id(), "NONE");
        videoRepository.save(newVideo);
        taskQueueService.sendTask(String.valueOf(ticket.id()));
        return ResponseEntity.ok().build();
    }
}
