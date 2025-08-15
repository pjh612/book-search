package com.example.trevaribooksearch.application.service;

import com.example.trevaribooksearch.application.dto.BookResponse;
import com.example.trevaribooksearch.domain.model.Isbn;
import com.example.trevaribooksearch.infrastructure.persistence.jpa.entity.AuthorEntity;
import com.example.trevaribooksearch.infrastructure.persistence.jpa.entity.BookEntity;
import com.example.trevaribooksearch.infrastructure.persistence.jpa.entity.PublisherEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class QueryBookServiceIntegrationTest {

    @Autowired
    private QueryBookService queryBookService;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("findBooks가 실제 DB에서 데이터를 조회한다")
    void findBooks_returnsBooks_fromDatabase() {
        // given
        AuthorEntity author = new AuthorEntity(null, "저자", null, null, null, null);
        em.persist(author);
        PublisherEntity publisher = new PublisherEntity(null, "출판사", null, null, null, null);
        em.persist(publisher);
        BookEntity book = new BookEntity(null, Isbn.randomIsbn13(), "테스트책", "부제", null, Instant.now(), publisher.getId(), author.getId(), null, null, null, null);
        em.persist(book);
        em.flush();
        em.clear();

        // when
        Page<BookResponse> result = queryBookService.findBooks(PageRequest.of(0, 10));

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);
        BookResponse response = result.getContent().get(0);
        assertThat(response.title()).isEqualTo("테스트책");
        assertThat(response.author()).isEqualTo("저자");
        assertThat(response.publisher()).isEqualTo("출판사");
    }

    @Test
    @DisplayName("findBooks는 데이터가 없을 때 빈 페이지를 반환한다")
    void findBooks_returnsEmptyPage() {
        // when
        Page<BookResponse> result = queryBookService.findBooks(PageRequest.of(0, 10));

        // then
        assertThat(result.getTotalElements()).isZero();
        assertThat(result.getContent()).isEmpty();
    }

    @Test
    @DisplayName("findBooks는 여러 데이터에서 페이징이 정상 동작한다")
    void findBooks_pagingWorks() {
        // given
        AuthorEntity author = new AuthorEntity(null, "저자", null, null, null, null);
        em.persist(author);
        PublisherEntity publisher = new PublisherEntity(null, "출판사", null, null, null, null);
        em.persist(publisher);
        for (int i = 1; i <= 15; i++) {
            BookEntity book = new BookEntity(null, "isbn" + i, "제목" + i, "부제" + i, null, Instant.now(), publisher.getId(), author.getId(), null, null, null, null);
            em.persist(book);
        }
        em.flush();
        em.clear();

        // when
        Page<BookResponse> page = queryBookService.findBooks(PageRequest.of(0, 10));

        // then
        assertThat(page.getContent()).hasSize(10);
        assertThat(page.getTotalElements()).isEqualTo(15);
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
            BookEntity book = new BookEntity(null, "isbn" + i, "제목" + i, "부제" + i, null, Instant.now().plusSeconds(i), publisher.getId(), author.getId(), null, null, null, null);
            em.persist(book);
        }
        em.flush();
        em.clear();

        // when
        Page<BookResponse> page = queryBookService.findBooks(PageRequest.of(0, 10, Sort.by("notExistField").ascending()));

        // then
        assertThat(page.getContent()).hasSize(3);
        assertThat(page.getContent().get(0).title()).isEqualTo("제목3");
        assertThat(page.getContent().get(2).title()).isEqualTo("제목1");
    }
}