package com.example.bookapi.infrastructure.cache.redis;

import com.example.bookapi.application.out.CacheProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisCacheProvider implements CacheProvider {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public <T> Optional<T> get(String key, Class<T> clazz) {
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(objectMapper.readValue(value, clazz));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public void save(String key, Object value, Duration ttl) {
        try {
            String valueString = objectMapper.writeValueAsString(value);

            redisTemplate.opsForValue().set(key, valueString, ttl);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
