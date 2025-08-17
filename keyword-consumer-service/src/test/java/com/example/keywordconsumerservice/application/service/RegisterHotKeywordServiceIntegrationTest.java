package com.example.keywordconsumerservice.application.service;

import com.example.keywordconsumerservice.infrastructure.cache.redis.TestRedisConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Import(TestRedisConfiguration.class)
class RegisterHotKeywordServiceIntegrationTest {

    @Autowired
    RegisterHotKeywordService registerHotKeywordService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @BeforeEach
    void tearDown() {
        String key = "search:hot-keyword:day:" + LocalDate.now();
        redisTemplate.delete(key);
    }

    @DisplayName("registerKeyword 호출 시 Redis에 키워드가 저장된다")
    @Test
    void registerKeyword_saves_keyword_to_redis() {
        // given
        String keyword = "spring";
        String key = "search:hot-keyword:day:" + LocalDate.now();

        // when
        registerHotKeywordService.registerKeyword(" spring ");

        // then
        Double score = redisTemplate.opsForZSet().score(key, keyword);
        assertThat(score).isNotNull();
        assertThat(score).isEqualTo(1.0);
    }

    @DisplayName("동일 키워드 여러 번 등록 시 점수가 누적된다")
    @Test
    void registerKeyword_accumulates_score_on_multiple_calls() {
        String keyword = "spring";
        String key = "search:hot-keyword:day:" + LocalDate.now();

        registerHotKeywordService.registerKeyword(keyword);
        registerHotKeywordService.registerKeyword(keyword);

        Double score = redisTemplate.opsForZSet().score(key, keyword);
        assertThat(score).isEqualTo(2.0);
    }

    @DisplayName("null 입력 시 IllegalArgumentException이 발생한다")
    @Test
    void registerKeyword_with_null_throws_exception() {
        String key = "search:hot-keyword:day:" + LocalDate.now();

        assertThatThrownBy(() -> registerHotKeywordService.registerKeyword(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Keyword cannot be null or empty");

        assertThat(redisTemplate.hasKey(key)).isFalse();
    }

    @DisplayName("공백만 입력 시 IllegalArgumentException이 발생한다")
    @Test
    void registerKeyword_with_blank_throws_exception() {
        String key = "search:hot-keyword:day:" + LocalDate.now();

        assertThatThrownBy(() -> registerHotKeywordService.registerKeyword("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Keyword cannot be null or empty");

        assertThat(redisTemplate.hasKey(key)).isFalse();
    }


    @DisplayName("서로 다른 키워드 등록 시 각각 점수가 1이다")
    @Test
    void registerKeyword_saves_multiple_keywords() {
        String key = "search:hot-keyword:day:" + LocalDate.now();

        registerHotKeywordService.registerKeyword("spring");
        registerHotKeywordService.registerKeyword("java");

        assertThat(redisTemplate.opsForZSet().score(key, "spring")).isEqualTo(1.0);
        assertThat(redisTemplate.opsForZSet().score(key, "java")).isEqualTo(1.0);
    }

    @DisplayName("등록된 키워드의 TTL이 1일로 설정된다")
    @Test
    void registerKeyword_sets_ttl_to_one_day() {
        String keyword = "spring";
        String key = "search:hot-keyword:day:" + LocalDate.now();

        registerHotKeywordService.registerKeyword(keyword);

        Long ttl = redisTemplate.getExpire(key);
        assertThat(ttl).isGreaterThan(0);
        assertThat(ttl).isLessThanOrEqualTo(86400); // 1일(초)
    }

    @DisplayName("대소문자가 다른 키워드는 별도로 저장된다")
    @Test
    void registerKeyword_case_sensitive() {
        String key = "search:hot-keyword:day:" + LocalDate.now();

        registerHotKeywordService.registerKeyword("Spring");
        registerHotKeywordService.registerKeyword("spring");

        assertThat(redisTemplate.opsForZSet().score(key, "Spring")).isEqualTo(1.0);
        assertThat(redisTemplate.opsForZSet().score(key, "spring")).isEqualTo(1.0);
    }
}