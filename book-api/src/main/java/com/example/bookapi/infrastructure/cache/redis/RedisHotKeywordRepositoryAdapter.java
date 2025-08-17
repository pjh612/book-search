package com.example.bookapi.infrastructure.cache.redis;

import com.example.bookapi.application.out.HotKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RedisHotKeywordRepositoryAdapter implements HotKeywordRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String KEY = "search:hot-keyword:day:";

    @Override
    public List<String> find(int size, LocalDate date) {
        Set<ZSetOperations.TypedTuple<String>> tuples = redisTemplate.opsForZSet()
                .reverseRangeWithScores(KEY + date, 0, size - 1);
        if (tuples == null || tuples.isEmpty()) {
            return Collections.emptyList();
        }

        return tuples.stream()
                .map(ZSetOperations.TypedTuple::getValue)
                .toList();
    }
}
