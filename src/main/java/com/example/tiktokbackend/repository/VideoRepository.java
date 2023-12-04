package com.example.tiktokbackend.repository;

import com.example.tiktokbackend.domain.Video;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface VideoRepository extends ListCrudRepository<Video, Long> {
    Video findByUuid(String uuid);
}
