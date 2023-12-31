package com.example.tiktokbackend.service;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class TaskQueueService {

    private JedisPool jedisPool;

    TaskQueueService(Environment env) {
        jedisPool = new JedisPool(env.getProperty("redis.host"), 6379);
    }

    public void sendTask(String message) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.rpush("convert", message);
        }
    }

    String getTask(String queueName) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.blpop(0, queueName).get(1);
        }
    }
}
