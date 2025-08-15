package com.example.trevaribooksearch.infrastructure.persistence.jpa.mapper;

import com.example.trevaribooksearch.domain.model.AuditInfo;
import com.example.trevaribooksearch.domain.model.Author;
import com.example.trevaribooksearch.infrastructure.persistence.jpa.entity.AuthorEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorMapperTest {

    @Test
    @DisplayName("AuthorEntity를 Author 도메인 객체로 변환한다")
    void toDomain_success() {
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.now();
        Instant updatedAt = createdAt.plusSeconds(100);
        String createdBy = "user1";
        String updatedBy = "user2";

        AuthorEntity entity = new AuthorEntity(
                id, "홍길동", createdAt, updatedAt, createdBy, updatedBy
        );

        Author author = AuthorMapper.toDomain(entity);

        assertThat(author.getId()).isEqualTo(id);
        assertThat(author.getName()).isEqualTo("홍길동");
        assertThat(author.getAuditInfo()).isNotNull();
        AuditInfo audit = author.getAuditInfo();
        assertThat(audit.getCreatedAt()).isEqualTo(createdAt);
        assertThat(audit.getUpdatedAt()).isEqualTo(updatedAt);
        assertThat(audit.getCreatedBy()).isEqualTo(createdBy);
        assertThat(audit.getUpdatedBy()).isEqualTo(updatedBy);
    }

    @Test
    @DisplayName("Author 도메인 객체를 AuthorEntity로 변환한다")
    void toEntity_success() {
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.now();
        Instant updatedAt = createdAt.plusSeconds(100);
        String createdBy = "user1";
        String updatedBy = "user2";
        AuditInfo auditInfo = new AuditInfo(createdAt, createdBy, updatedAt, updatedBy);

        Author author = new Author(id, "이몽룡", auditInfo);

        AuthorEntity entity = AuthorMapper.toEntity(author);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getName()).isEqualTo("이몽룡");
        assertThat(entity.getCreatedAt()).isEqualTo(createdAt);
        assertThat(entity.getUpdatedAt()).isEqualTo(updatedAt);
        assertThat(entity.getCreatedBy()).isEqualTo(createdBy);

        assertThat(entity.getUpdatedBy()).isEqualTo(updatedBy);
    }
}