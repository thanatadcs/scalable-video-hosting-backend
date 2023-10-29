package com.example.tiktokbackend.repository;

import com.example.tiktokbackend.domain.Video;
import org.springframework.data.repository.CrudRepository;

public interface VideoRepository extends CrudRepository<Video, Integer> {
}
