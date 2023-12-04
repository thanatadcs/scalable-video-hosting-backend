package com.example.tiktokbackend.service;

import com.example.tiktokbackend.domain.Video;
import com.example.tiktokbackend.repository.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
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
                // Message format: TaskName,UUID,status
                String[] splitMessage = message.split(",");
                String taskName = splitMessage[0];
                String uuid = splitMessage[1];
                String status = splitMessage[2];
                Video video = videoRepository.findByUuid(uuid);
                if (taskName.equals("convert")) video.setConvertStatus(status);
                else if (taskName.equals("thumbnail")) video.setThumbnailStatus(status);
                else if (taskName.equals("chunk")) video.setChunkStatus(status);

                if (video.getThumbnailStatus().equals("success") &&
                        video.getChunkStatus().equals("success"))
                    video.setReadyStatus("ready");
                else if (video.getConvertStatus().equals("fail") ||
                        video.getThumbnailStatus().equals("fail") ||
                        video.getChunkStatus().equals("fail"))
                    video.setReadyStatus("never"); // Need to clean video from S3
                videoRepository.save(video);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

}
