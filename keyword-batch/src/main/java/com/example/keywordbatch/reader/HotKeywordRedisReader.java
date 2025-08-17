package com.example.keywordbatch.reader;

import com.example.keywordbatch.entity.HotKeyword;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;

@StepScope
@Component
@RequiredArgsConstructor
public class HotKeywordRedisReader implements ItemReader<HotKeyword> {
    private final RedisTemplate<String, String> redisTemplate;

    @Value("#{jobParameters['date']}")
    private String date;

    @Value("#{jobParameters['topN'] ?: 10}")
    private Integer topN;

    private Iterator<ZSetOperations.TypedTuple<String>> iterator;

    @Override
    public HotKeyword read() {
        if (iterator == null) {
            Set<ZSetOperations.TypedTuple<String>> top =
                    redisTemplate.opsForZSet().reverseRangeWithScores("search:hot-keyword:day:" + date, 0, topN - 1);
            if (top == null || top.isEmpty()) return null;
            iterator = top.iterator();
        }
        if (!iterator.hasNext()) return null;
        ZSetOperations.TypedTuple<String> t = iterator.next();
        return new HotKeyword(null, t.getValue(), t.getScore() == null ? 0L : t.getScore().longValue(), LocalDateTime.now());
    }
}