package com.example.tiktokbackend;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class TaskQueueService {
    private JedisPool jedisPool;

    TaskQueueService() {
        jedisPool = new JedisPool("localhost", 6379);
    }

    void sendTask(String message) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.rpush("convert-queue", message);
        }
    }
}
