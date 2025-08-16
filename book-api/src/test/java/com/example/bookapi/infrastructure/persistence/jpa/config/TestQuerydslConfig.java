package com.example.bookapi.infrastructure.persistence.jpa.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestQuerydslConfig {
    private final EntityManager entityManager;

    public TestQuerydslConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Bean
    public JPAQueryFactory queryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
