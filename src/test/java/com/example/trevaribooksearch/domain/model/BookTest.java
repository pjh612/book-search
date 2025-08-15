package com.example.trevaribooksearch.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BookTest {

    @Test
    @DisplayName("Book 생성자: isbn이 null이면 예외 발생")
    void constructor_isbn_null() {
        UUID authorId = UUID.randomUUID();
        UUID publisherId = UUID.randomUUID();
        Instant now = Instant.now();

        assertThatThrownBy(() -> new Book(UUID.randomUUID(), null, "제목", null, null, authorId, publisherId, now, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Book 생성자: isbn 값이 비어있으면 예외 발생")
    void constructor_isbn_blank() {
        UUID authorId = UUID.randomUUID();
        UUID publisherId = UUID.randomUUID();
        Instant now = Instant.now();

        assertThatThrownBy(() -> new Book(UUID.randomUUID(), Isbn.of(""), "제목", null, null, authorId, publisherId, now, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Book 생성자: isbn 내부 값이 null이면 예외 발생")
    void constructor_isbn_value_null() {
        UUID authorId = UUID.randomUUID();
        UUID publisherId = UUID.randomUUID();
        Instant now = Instant.now();

        assertThatThrownBy(() -> new Book(UUID.randomUUID(), Isbn.of(null), "제목", null, null, authorId, publisherId, now, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Book 생성자: title이 null이면 예외 발생")
    void constructor_title_null() {
        UUID authorId = UUID.randomUUID();
        UUID publisherId = UUID.randomUUID();
        Instant now = Instant.now();

        assertThatThrownBy(() -> new Book(UUID.randomUUID(), Isbn.of("9781234567890"), null, null, null, authorId, publisherId, now, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Book 생성자: title이 비어있으면 예외 발생")
    void constructor_title_blank() {
        UUID authorId = UUID.randomUUID();
        UUID publisherId = UUID.randomUUID();
        Instant now = Instant.now();

        assertThatThrownBy(() -> new Book(UUID.randomUUID(), Isbn.of("9781234567890"), "", null, null, authorId, publisherId, now, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Book 생성자: authorId가 null이면 예외 발생")
    void constructor_authorId_null() {
        UUID publisherId = UUID.randomUUID();
        Instant now = Instant.now();

        assertThatThrownBy(() -> new Book(UUID.randomUUID(), Isbn.of("9781234567890"), "제목", null, null, null, publisherId, now, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Book 생성자: publisherId가 null이면 예외 발생")
    void constructor_publisherId_null() {
        UUID authorId = UUID.randomUUID();
        Instant now = Instant.now();

        assertThatThrownBy(() -> new Book(UUID.randomUUID(), Isbn.of("9781234567890"), "제목", null, null, authorId, null, now, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Book.create는 정상적으로 Book을 생성한다")
    void create_success() {
        UUID authorId = UUID.randomUUID();
        UUID publisherId = UUID.randomUUID();
        Instant now = Instant.now();

        Book book = Book.create("테스트제목", "부제", "이미지", "9781234567890", authorId, publisherId, now, "user1");

        assertThat(book.getTitle()).isEqualTo("테스트제목");
        assertThat(book.getSubtitle()).isEqualTo("부제");
        assertThat(book.getImage()).isEqualTo("이미지");
        assertThat(book.getIsbn()).isEqualTo("9781234567890");
        assertThat(book.getAuthorId()).isEqualTo(authorId);
        assertThat(book.getPublisherId()).isEqualTo(publisherId);
        assertThat(book.getPublished()).isEqualTo(now);
        assertThat(book.getAuditInfo()).isNotNull();
    }

    @Test
    @DisplayName("getIsbn()은 Isbn 값의 문자열을 반환한다")
    void getIsbn_returnsValue() {
        Book book = new Book(
                UUID.randomUUID(),
                Isbn.of("9781234567890"),
                "제목",
                null,
                null,
                UUID.randomUUID(),
                UUID.randomUUID(),
                Instant.now(),
                null
        );
        assertThat(book.getIsbn()).isEqualTo("9781234567890");
    }
}