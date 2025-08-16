package com.example.bookapi.infrastructure.persistence.jpa.mapper;

import com.example.bookapi.domain.model.AuditInfo;
import com.example.bookapi.domain.model.Book;
import com.example.bookapi.domain.model.Isbn;
import com.example.bookapi.infrastructure.persistence.jpa.entity.BookEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class BookMapperTest {

    @Test
    @DisplayName("BookEntity를 Book 도메인 객체로 변환한다")
    void toDomain_success() {
        UUID id = UUID.randomUUID();
        String isbn = "9781234567890";
        String title = "테스트제목";
        String subtitle = "부제";
        String image = "이미지";
        UUID authorId = UUID.randomUUID();
        UUID publisherId = UUID.randomUUID();
        Instant published = Instant.now();
        Instant createdAt = published.minusSeconds(100);
        Instant updatedAt = published;
        String createdBy = "user1";
        String updatedBy = "user2";

        BookEntity entity = new BookEntity(
                id, isbn, title, subtitle, image, published, publisherId, authorId,
                createdAt, updatedAt, createdBy, updatedBy
        );

        Book book = BookMapper.toDomain(entity);

        assertThat(book.getId()).isEqualTo(id);
        assertThat(book.getIsbn()).isEqualTo(isbn);
        assertThat(book.getTitle()).isEqualTo(title);
        assertThat(book.getSubtitle()).isEqualTo(subtitle);
        assertThat(book.getImage()).isEqualTo(image);
        assertThat(book.getAuthorId()).isEqualTo(authorId);
        assertThat(book.getPublisherId()).isEqualTo(publisherId);
        assertThat(book.getPublished()).isEqualTo(published);
        assertThat(book.getAuditInfo()).isNotNull();
        AuditInfo audit = book.getAuditInfo();
        assertThat(audit.getCreatedAt()).isEqualTo(createdAt);
        assertThat(audit.getUpdatedAt()).isEqualTo(updatedAt);
        assertThat(audit.getCreatedBy()).isEqualTo(createdBy);
        assertThat(audit.getUpdatedBy()).isEqualTo(updatedBy);
    }

    @Test
    @DisplayName("Book 도메인 객체를 BookEntity로 변환한다")
    void toEntity_success() {
        UUID id = UUID.randomUUID();
        String isbn = "9781234567890";
        String title = "테스트제목";
        String subtitle = "부제";
        String image = "이미지";
        UUID authorId = UUID.randomUUID();
        UUID publisherId = UUID.randomUUID();
        Instant published = Instant.now();
        Instant createdAt = published.minusSeconds(100);
        Instant updatedAt = published;
        String createdBy = "user1";
        String updatedBy = "user2";
        AuditInfo auditInfo = new AuditInfo(createdAt, createdBy, updatedAt, updatedBy);

        Book book = new Book(
                id, Isbn.of(isbn), title, subtitle, image, authorId, publisherId, published, auditInfo
        );

        BookEntity entity = BookMapper.toEntity(book);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getIsbn()).isEqualTo(isbn);
        assertThat(entity.getTitle()).isEqualTo(title);
        assertThat(entity.getSubtitle()).isEqualTo(subtitle);
        assertThat(entity.getImage()).isEqualTo(image);
        assertThat(entity.getAuthorId()).isEqualTo(authorId);
        assertThat(entity.getPublisherId()).isEqualTo(publisherId);
        assertThat(entity.getPublished()).isEqualTo(published);
        assertThat(entity.getCreatedAt()).isEqualTo(createdAt);
        assertThat(entity.getUpdatedAt()).isEqualTo(updatedAt);
        assertThat(entity.getCreatedBy()).isEqualTo(createdBy);
        assertThat(entity.getUpdatedBy()).isEqualTo(updatedBy);
    }
}