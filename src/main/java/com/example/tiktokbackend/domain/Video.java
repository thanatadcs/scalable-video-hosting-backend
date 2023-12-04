package com.example.tiktokbackend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    private String convertStatus;
    private String thumbnailStatus;
    private String chunkStatus;
    private String readyStatus;

    public Video() {
    }

    public Video(String uuid) {
        this(uuid, null, null, null, null);
    }

    public Video(String uuid, String convertStatus, String thumbnailStatus, String chunkStatus, String readyStatus) {
        this.uuid = uuid;
        this.convertStatus = convertStatus;
        this.thumbnailStatus = thumbnailStatus;
        this.chunkStatus = chunkStatus;
        this.readyStatus = readyStatus;
    }

}
