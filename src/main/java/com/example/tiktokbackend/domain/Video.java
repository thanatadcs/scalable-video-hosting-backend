package com.example.tiktokbackend.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "video")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String uuid;
    private String convertStatus;

    public Video() {
    }

    public Video(String uuid, String convertStatus) {
        this.uuid = uuid;
        this.convertStatus = convertStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
