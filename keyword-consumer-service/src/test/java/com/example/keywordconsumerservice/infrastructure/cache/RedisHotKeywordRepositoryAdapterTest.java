package com.example.keywordconsumerservice.infrastructure.cache;

import com.example.keywordconsumerservice.application.out.HotKeywordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class RedisHotKeywordRepositoryAdapterTest {

    @DisplayName("register 호출 시 ZSet 점수 증가와 TTL 설정이 수행된다")
    @Test
    void register_increments_score_and_sets_ttl() {
        // given
        RedisTemplate<String, String> redisTemplate = mock(RedisTemplate.class);
        ZSetOperations<String, String> zSetOperations = mock(ZSetOperations.class);
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);

        HotKeywordRepository repository = new RedisHotKeywordRepositoryAdapter(redisTemplate);
        String keyword = "spring";
        String key = "search:hot-keyword:day:" + LocalDate.now();

        // when
        repository.register(keyword);

        // then
        verify(zSetOperations).incrementScore(key, keyword, 1);
        verify(redisTemplate).expire(eq(key), any());
    }
}