package com.example.trevaribooksearch.infrastructure.persistence.jpa.mapper;

import com.example.trevaribooksearch.domain.model.AuditInfo;
import com.example.trevaribooksearch.domain.model.Publisher;
import com.example.trevaribooksearch.infrastructure.persistence.jpa.entity.PublisherEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PublisherMapperTest {

    @Test
    @DisplayName("PublisherEntity를 Publisher 도메인 객체로 변환한다")
    void toDomain_success() {
        UUID id = UUID.randomUUID();
        String name = "출판사";
        Instant createdAt = Instant.now();
        Instant updatedAt = createdAt.plusSeconds(100);
        String createdBy = "user1";
        String updatedBy = "user2";

        PublisherEntity entity = new PublisherEntity(
                id, name, createdAt, updatedAt, createdBy, updatedBy
        );

        Publisher publisher = PublisherMapper.toDomain(entity);

        assertThat(publisher.getId()).isEqualTo(id);
        assertThat(publisher.getName()).isEqualTo(name);
        assertThat(publisher.getAuditInfo()).isNotNull();
        AuditInfo audit = publisher.getAuditInfo();
        assertThat(audit.getCreatedAt()).isEqualTo(createdAt);
        assertThat(audit.getUpdatedAt()).isEqualTo(updatedAt);
        assertThat(audit.getCreatedBy()).isEqualTo(createdBy);
        assertThat(audit.getUpdatedBy()).isEqualTo(updatedBy);
    }

    @Test
    @DisplayName("Publisher 도메인 객체를 PublisherEntity로 변환한다")
    void toEntity_success() {
        UUID id = UUID.randomUUID();
        String name = "출판사";
        Instant createdAt = Instant.now();
        Instant updatedAt = createdAt.plusSeconds(100);
        String createdBy = "user1";
        String updatedBy = "user2";
        AuditInfo auditInfo = new AuditInfo(createdAt, createdBy, updatedAt, updatedBy);

        Publisher publisher = new Publisher(id, name, auditInfo);

        PublisherEntity entity = PublisherMapper.toEntity(publisher);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getName()).isEqualTo(name);
        assertThat(entity.getCreatedAt()).isEqualTo(createdAt);
        assertThat(entity.getUpdatedAt()).isEqualTo(updatedAt);
        assertThat(entity.getCreatedBy()).isEqualTo(createdBy);
        assertThat(entity.getUpdatedBy()).isEqualTo(updatedBy);
    }
}