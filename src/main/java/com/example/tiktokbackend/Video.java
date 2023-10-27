package com.example.tiktokbackend;

import jakarta.persistence.*;

@Entity
@Table(name = "video")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private Integer ticketNumber;
    private String convertStatus;

    public Video() {
    }

    public Video(Integer ticketNumber, String convertStatus) {
        this.ticketNumber = ticketNumber;
        this.convertStatus = convertStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
