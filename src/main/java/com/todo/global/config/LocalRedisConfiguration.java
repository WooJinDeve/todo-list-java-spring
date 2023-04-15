package com.todo.global.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

@Configuration
public class LocalRedisConfiguration {

    private final RedisServer redisServer = new RedisServer();

    @PostConstruct
    public void initializeRedisServer() {
        redisServer.start();
    }

    @PreDestroy
    public void destroyRedisServer() {
        redisServer.stop();
    }
}
