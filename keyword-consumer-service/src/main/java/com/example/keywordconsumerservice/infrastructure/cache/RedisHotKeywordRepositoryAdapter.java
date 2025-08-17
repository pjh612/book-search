package com.example.keywordconsumerservice.infrastructure.cache;

import com.example.keywordconsumerservice.application.out.HotKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class RedisHotKeywordRepositoryAdapter implements HotKeywordRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String KEY_PREFIX = "search:hot-keyword:day:";

    @Override
    public void register(String keyword) {
        String key = KEY_PREFIX + LocalDate.now();
        redisTemplate.opsForZSet().incrementScore(key, keyword, 1);
        redisTemplate.expire(key, Duration.ofDays(1));
    }
}
