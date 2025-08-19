package com.example.bookapi.application.service;

import com.example.bookapi.application.dto.HotKeywordResponse;
import com.example.bookapi.infrastructure.cache.redis.TestRedisConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import({TestRedisConfiguration.class})
@ActiveProfiles("test")
class QueryHotKeywordServiceIntegrationTest {

    @Autowired
    QueryHotKeywordService queryHotKeywordService;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    private static final String KEY = "search:hot-keyword:day:";

    @BeforeEach
    void tearDown() {
        redisTemplate.delete(KEY);
    }

    @Test
    @DisplayName("많이 검색한 검색어 순으로 조회한다.")
    void find_returnsHotKeywords_fromRedis() {
        // given
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        LocalDate now = LocalDate.now();
        zSetOps.add(KEY + now, "자바", 100);
        zSetOps.add(KEY + now, "스프링", 90);
        zSetOps.add(KEY + now, "JPA", 80);

        // when
        HotKeywordResponse response = queryHotKeywordService.find(3, now);

        // then
        assertThat(response.keywords()).containsExactly("자바", "스프링", "JPA");
    }

    @Test
    @DisplayName("레디스에 핫 키워드가 없으면 빈 리스트 반환")
    void find_returnsEmptyList_whenNoKeywordsInRedis() {
        // when
        HotKeywordResponse response = queryHotKeywordService.find(5, LocalDate.now());

        // then
        assertThat(response.keywords()).isEmpty();
    }
}