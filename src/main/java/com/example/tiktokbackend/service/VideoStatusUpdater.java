package com.example.tiktokbackend.service;

import com.example.tiktokbackend.domain.Video;
import com.example.tiktokbackend.repository.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VideoStatusUpdater implements ApplicationListener<ApplicationReadyEvent> {

    private VideoRepository videoRepository;
    private TaskQueueService taskQueueService;
    private final String QueueName = "backend";

    VideoStatusUpdater(TaskQueueService taskQueueService, VideoRepository videoRepository) {
        this.taskQueueService = taskQueueService;
        this.videoRepository = videoRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        new SimpleAsyncTaskExecutor().execute(this::updateVideoProcessingStatus);
    }

    public void updateVideoProcessingStatus() {
        while (true) {
            try {
                String message = taskQueueService.getTask(QueueName);
                String[] splitMessage = message.split(",");
                String taskName = splitMessage[0];
                String uuid = splitMessage[1];
                Video video = videoRepository.findByUuid(uuid);
                if (taskName.equals("convert")) video.setConvertStatus("done");
                else if (taskName.equals("thumbnail")) video.setThumbnailStatus("done");
                else if (taskName.equals("chunk")) video.setChunkStatus("done");
                videoRepository.save(video);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

}
