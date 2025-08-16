package com.example.bookapi.infrastructure.persistence.jpa.repository;

import com.example.bookapi.application.dto.BookDetailResponse;
import com.example.bookapi.application.dto.BookResponse;
import com.example.bookapi.infrastructure.persistence.jpa.config.TestJpaConfig;
import com.example.bookapi.infrastructure.persistence.jpa.config.TestQuerydslConfig;
import com.example.bookapi.infrastructure.persistence.jpa.entity.AuthorEntity;
import com.example.bookapi.infrastructure.persistence.jpa.entity.BookEntity;
import com.example.bookapi.infrastructure.persistence.jpa.entity.PublisherEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import({BookQueryRepository.class, TestQuerydslConfig.class, TestJpaConfig.class})
class BookQueryRepositoryTest {

    @Autowired
    private BookQueryRepository bookQueryRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("findBooks가 정상적으로 데이터를 반환한다")
    void findBooks_returnsBooks() {
        // given
        AuthorEntity author = new AuthorEntity(null, "저자", null, null, null, null);
        em.persist(author);

        PublisherEntity publisher = new PublisherEntity(null, "출판사", null, null, null, null);
        em.persist(publisher);

        BookEntity book = new BookEntity(null, "9781234567890", "테스트책", "부제", null, Instant.now(), publisher.getId(), author.getId(), null, null, null, null);
        em.persist(book);

        em.flush();
        em.clear();

        // when
        Page<BookResponse> result = bookQueryRepository.findBooks(PageRequest.of(0, 10));

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);
        BookResponse response = result.getContent().get(0);
        assertThat(response.title()).isEqualTo(book.getTitle());
        assertThat(response.subtitle()).isEqualTo(book.getSubtitle());
        assertThat(response.image()).isEqualTo(book.getImage());
        assertThat(response.isbn()).isEqualTo(book.getIsbn());
        assertThat(response.author()).isEqualTo(author.getName());
        assertThat(response.publisher()).isEqualTo(publisher.getName());
        assertThat(response.published()).isEqualTo(book.getPublished());
    }

    @Test
    @DisplayName("findBooks는 페이징이 정상적으로 동작한다")
    void findBooks_pagingWorks() {
        // given
        AuthorEntity author = new AuthorEntity(null, "저자", null, null, null, null);
        em.persist(author);

        PublisherEntity publisher = new PublisherEntity(null, "출판사", null, null, null, null);
        em.persist(publisher);

        for (int i = 1; i <= 15; i++) {
            BookEntity book = new BookEntity(
                    null,
                    "isbn" + i,
                    "제목" + i,
                    "부제" + i,
                    null,
                    Instant.now(),
                    publisher.getId(),
                    author.getId(),
                    null, null, null, null
            );
            em.persist(book);

        }
        em.flush();
        em.clear();

        // when
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<BookResponse> page = bookQueryRepository.findBooks(pageRequest);


        // then
        assertThat(page.getContent()).hasSize(10);
        assertThat(page.getTotalElements()).isEqualTo(15);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getContent().get(0).title()).isEqualTo("제목15");
        assertThat(page.getContent().get(9).title()).isEqualTo("제목6");
    }

    @Test
    @DisplayName("findBooks는 데이터가 없을 때 빈 페이지를 반환한다")
    void findBooks_returnsEmptyPage() {
        // when
        Page<BookResponse> result = bookQueryRepository.findBooks(PageRequest.of(0, 10));

        // then
        assertThat(result.getTotalElements()).isZero();
        assertThat(result.getContent()).isEmpty();
    }


    @Test
    @DisplayName("findBooks는 지원하지 않는 정렬 파라미터가 들어와도 기본 정렬로 동작한다")
    void findBooks_unsupportedSort_returnsDefaultOrder() {
        // given
        AuthorEntity author = new AuthorEntity(null, "저자", null, null, null, null);
        em.persist(author);

        PublisherEntity publisher = new PublisherEntity(null, "출판사", null, null, null, null);
        em.persist(publisher);

        for (int i = 1; i <= 3; i++) {
            BookEntity book = new BookEntity(
                    null,
                    "isbn" + i,
                    "제목" + i,
                    "부제" + i,
                    null,
                    Instant.now().plusSeconds(i),
                    publisher.getId(),
                    author.getId(),
                    null, null, null, null
            );
            em.persist(book);
        }
        em.flush();
        em.clear();

        // when
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("notExistField").ascending());
        Page<BookResponse> page = bookQueryRepository.findBooks(pageRequest);

        // then
        assertThat(page.getContent()).hasSize(3);
        assertThat(page.getContent().get(0).title()).isEqualTo("제목3");
        assertThat(page.getContent().get(2).title()).isEqualTo("제목1");
    }

    @Test
    @DisplayName("findById가 BookDetailResponse를 반환한다")
    void findById_returnsBookDetailResponse() {
        // given
        AuthorEntity author = new AuthorEntity(null, "저자", null, null, null, null);
        em.persist(author);
        PublisherEntity publisher = new PublisherEntity(null, "출판사", null, null, null, null);
        em.persist(publisher);
        BookEntity book = new BookEntity(null, "isbn123", "테스트책", "부제", null, Instant.now(), publisher.getId(), author.getId(), null, null, null, null);
        em.persist(book);
        em.flush();
        em.clear();

        // when
        Optional<BookDetailResponse> result = bookQueryRepository.findById(book.getId());

        // then
        assertThat(result).isPresent();
        BookDetailResponse detail = result.get();
        assertThat(detail.id()).isEqualTo(book.getId());
        assertThat(detail.title()).isEqualTo("테스트책");
        assertThat(detail.author()).isEqualTo("저자");
        assertThat(detail.publisher()).isEqualTo("출판사");
    }

    @Test
    @DisplayName("findById는 없는 ID일 때 빈 Optional을 반환한다")
    void findById_returnsEmptyOptional_whenNotFound() {
        // given
        UUID notExistId = UUID.randomUUID();

        // when
        Optional<BookDetailResponse> result = bookQueryRepository.findById(notExistId);

        // then
        assertThat(result).isEqualTo(Optional.empty());
    }
}