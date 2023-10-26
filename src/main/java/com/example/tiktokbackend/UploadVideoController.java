package com.example.tiktokbackend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URL;

@RestController
@RequestMapping("/api/upload")
public class UploadVideoController {
    final private String bucketName = "scalable-p2";

    private VideoRepository videoRepository;

    UploadVideoController(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
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
        return ResponseEntity.ok().build();
    }
}
