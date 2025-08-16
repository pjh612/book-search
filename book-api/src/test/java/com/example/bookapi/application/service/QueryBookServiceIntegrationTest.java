package com.example.bookapi.application.service;

import com.example.bookapi.application.dto.BookDetailResponse;
import com.example.bookapi.application.dto.BookResponse;
import com.example.bookapi.application.dto.BookSearchRequest;
import com.example.bookapi.application.dto.BookSearchResponse;
import com.example.bookapi.domain.model.Isbn;
import com.example.bookapi.infrastructure.persistence.jpa.entity.AuthorEntity;
import com.example.bookapi.infrastructure.persistence.jpa.entity.BookEntity;
import com.example.bookapi.infrastructure.persistence.jpa.entity.PublisherEntity;
import com.example.bookapi.infrastructure.search.exception.InvalidSearchQueryException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    @DisplayName("findById가 실제 DB에서 BookDetailResponse를 반환한다")
    void findById_returnsBookDetailResponse_fromDatabase() {
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
        BookDetailResponse result = queryBookService.findById(book.getId());

        // then
        assertThat(result.id()).isEqualTo(book.getId());
        assertThat(result.title()).isEqualTo("테스트책");
        assertThat(result.author()).isEqualTo("저자");
        assertThat(result.publisher()).isEqualTo("출판사");
    }

    @Test
    @DisplayName("findById는 없는 ID일 때 EntityNotFoundException을 던진다")
    void findById_throwsEntityNotFoundException_whenNotFound() {
        // given
        UUID notExistId = UUID.randomUUID();

        // expect
        assertThatThrownBy(() -> queryBookService.findById(notExistId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("도서 정보를 찾을 수 없습니다");
    }

    @Test
    @DisplayName("단일 키워드로 검색 시 결과를 반환한다")
    void searchBooks_singleKeyword() {
        // given
        AuthorEntity author = new AuthorEntity(null, "홍길동", null, null, null, null);
        em.persist(author);
        PublisherEntity publisher = new PublisherEntity(null, "출판사", null, null, null, null);
        em.persist(publisher);

        BookEntity book1 = new BookEntity(null, "isbn1", "자바 프로그래밍", "부제1", null, Instant.now(), publisher.getId(), author.getId(), null, null, null, null);
        em.persist(book1);
        em.flush();
        em.clear();

        // when
        BookSearchRequest request = new BookSearchRequest("자바", PageRequest.of(0, 10));
        BookSearchResponse response = queryBookService.searchBooks(request);

        // then
        assertThat(response.books()).extracting(BookResponse::title)
                .anyMatch(t -> t.contains("자바"));
    }

    @Test
    @DisplayName("단일 검색어로 검색 시 결과를 반환한다")
    void searchBooks_andOperator() {
        // given
        AuthorEntity author = new AuthorEntity(null, "홍길동", null, null, null, null);
        em.persist(author);
        PublisherEntity publisher = new PublisherEntity(null, "출판사", null, null, null, null);
        em.persist(publisher);

        BookEntity book = new BookEntity(null, "isbn3", "자바와 스프링", "부제3", null, Instant.now(), publisher.getId(), author.getId(), null, null, null, null);
        em.persist(book);
        em.flush();
        em.clear();

        // when
        BookSearchRequest request = new BookSearchRequest("자바와", PageRequest.of(0, 10));
        BookSearchResponse response = queryBookService.searchBooks(request);

        // then
        assertThat(response.books()).extracting(BookResponse::title)
                .containsExactly("자바와 스프링");
    }

    @Test
    @DisplayName("OR 연산(|)으로 검색 시 결과를 반환한다")
    void searchBooks_orOperator() {
        // given
        AuthorEntity author = new AuthorEntity(null, "홍길동", null, null, null, null);
        em.persist(author);
        PublisherEntity publisher = new PublisherEntity(null, "출판사", null, null, null, null);
        em.persist(publisher);

        BookEntity book1 = new BookEntity(null, "isbn1", "자바 프로그래밍", "부제1", null, Instant.now(), publisher.getId(), author.getId(), null, null, null, null);
        BookEntity book2 = new BookEntity(null, "isbn2", "스프링 부트", "부제2", null, Instant.now(), publisher.getId(), author.getId(), null, null, null, null);
        BookEntity book3 = new BookEntity(null, "isbn3", "자바와 스프링", "부제3", null, Instant.now(), publisher.getId(), author.getId(), null, null, null, null);
        em.persist(book1);
        em.persist(book2);
        em.persist(book3);
        em.flush();
        em.clear();

        // when
        BookSearchRequest request = new BookSearchRequest("자바|스프링", PageRequest.of(0, 10));
        BookSearchResponse response = queryBookService.searchBooks(request);

        // then
        assertThat(response.books()).extracting(BookResponse::title)
                .contains("자바 프로그래밍", "스프링 부트", "자바와 스프링");
    }

    @Test
    @DisplayName("NOT 연산(-)으로 검색 시 제외한 결과를 반환한다")
    void searchBooks_notOperator() {
        // given
        AuthorEntity author = new AuthorEntity(null, "홍길동", null, null, null, null);
        em.persist(author);
        PublisherEntity publisher = new PublisherEntity(null, "출판사", null, null, null, null);
        em.persist(publisher);

        BookEntity book1 = new BookEntity(null, "isbn1", "자바 프로그래밍", "부제1", null, Instant.now(), publisher.getId(), author.getId(), null, null, null, null);
        BookEntity book2 = new BookEntity(null, "isbn2", "스프링 부트", "부제2", null, Instant.now(), publisher.getId(), author.getId(), null, null, null, null);
        em.persist(book1);
        em.persist(book2);
        em.flush();
        em.clear();

        // when
        BookSearchRequest request = new BookSearchRequest("자바-스프링", PageRequest.of(0, 10));
        BookSearchResponse response = queryBookService.searchBooks(request);

        // then
        assertThat(response.books()).extracting(BookResponse::title)
                .containsExactly("자바 프로그래밍");
    }

    @Test
    @DisplayName("빈 keyword로 검색 시 InvalidSearchQueryException이 발생한다")
    void searchBooks_throwsException_whenKeywordBlank() {
        // expect
        BookSearchRequest request = new BookSearchRequest("   ", PageRequest.of(0, 10));
        assertThatThrownBy(() -> queryBookService.searchBooks(request))
                .isInstanceOf(InvalidSearchQueryException.class)
                .hasMessageContaining("query cannot be null or blank");
    }

    @Test
    @DisplayName("검색 결과가 없으면 빈 응답을 반환한다")
    void searchBooks_returnsEmptyResult_whenNoMatch() {
        // given
        AuthorEntity author = new AuthorEntity(null, "홍길동", null, null, null, null);
        em.persist(author);
        PublisherEntity publisher = new PublisherEntity(null, "출판사", null, null, null, null);
        em.persist(publisher);
        BookEntity book = new BookEntity(null, "isbn1", "자바 프로그래밍", "부제1", null, Instant.now(), publisher.getId(), author.getId(), null, null, null, null);
        em.persist(book);
        em.flush();
        em.clear();

        // when
        BookSearchRequest request = new BookSearchRequest("파이썬", PageRequest.of(0, 10));
        BookSearchResponse response = queryBookService.searchBooks(request);

        // then
        assertThat(response.books()).isEmpty();
        assertThat(response.pageInfo().totalElements()).isZero();
    }

}