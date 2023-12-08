package com.example.tiktokbackend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

@Entity
@Table(name = "video")
@Getter
@Setter
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String uuid;
    private String title;
    private String convertStatus;
    private String thumbnailStatus;
    private String chunkStatus;
    private String readyStatus;

    public Video() {
    }

    public Video(String uuid, String title) {
        this(uuid, title,"none", "none", "none", "none");
    }

    public Video(String uuid, String title, String convertStatus, String thumbnailStatus, String chunkStatus, String readyStatus) {
        this.uuid = uuid;
        this.title = title;
        this.convertStatus = convertStatus;
        this.thumbnailStatus = thumbnailStatus;
        this.chunkStatus = chunkStatus;
        this.readyStatus = readyStatus;
    }

}
