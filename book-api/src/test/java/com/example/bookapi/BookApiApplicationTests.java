package com.example.bookapi;

import com.example.bookapi.infrastructure.cache.redis.TestRedisConfiguration;
import com.example.bookapi.infrastructure.search.elasticsearch.TestElasticSearchConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@Import({TestElasticSearchConfig.class, TestRedisConfiguration.class})
class BookApiApplicationTests {

    @Test
    void contextLoads() {
    }

}
