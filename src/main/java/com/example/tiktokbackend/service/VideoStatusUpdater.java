package com.example.tiktokbackend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VideoStatusUpdater implements ApplicationListener<ApplicationReadyEvent> {

    private TaskQueueService taskQueueService;
    private final String QueueName = "backend";

    VideoStatusUpdater(TaskQueueService taskQueueService) {
        this.taskQueueService = taskQueueService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        new SimpleAsyncTaskExecutor().execute(this::updateVideoProcessingStatus);
    }

    public void updateVideoProcessingStatus() {
        while (true) {
            try {
                String taskName = taskQueueService.getTask(QueueName);
                log.info(taskName);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

}
