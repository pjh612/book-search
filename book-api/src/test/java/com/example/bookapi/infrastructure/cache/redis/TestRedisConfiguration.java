package com.example.bookapi.infrastructure.cache.redis;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.test.util.TestSocketUtils;
import redis.embedded.RedisServer;

import java.io.IOException;

@TestConfiguration
public class TestRedisConfiguration {

    private final RedisServer redisServer;
    private final int port;
    public TestRedisConfiguration(RedisProperties redisProperties) throws IOException {
        port = TestSocketUtils.findAvailableTcpPort();
            this.redisServer = new RedisServer(port);
    }

    @PostConstruct
    public void postConstruct() throws IOException {
        redisServer.start();
    }

    @PreDestroy
    public void preDestroy() throws IOException {
        redisServer.stop();
    }
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("localhost", port));
    }
}