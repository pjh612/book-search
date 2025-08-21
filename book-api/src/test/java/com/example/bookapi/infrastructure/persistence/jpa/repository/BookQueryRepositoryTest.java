package com.example.bookapi.infrastructure.persistence.jpa.repository;

import com.example.bookapi.application.dto.BookCursor;
import com.example.bookapi.application.dto.BookDetailResponse;
import com.example.bookapi.application.dto.BookResponse;
import com.example.bookapi.infrastructure.persistence.jpa.config.TestJpaConfig;
import com.example.bookapi.infrastructure.persistence.jpa.config.TestQuerydslConfig;
import com.example.bookapi.infrastructure.persistence.jpa.entity.AuthorEntity;
import com.example.bookapi.infrastructure.persistence.jpa.entity.BookEntity;
import com.example.bookapi.infrastructure.persistence.jpa.entity.PublisherEntity;
import com.fasterxml.uuid.Generators;
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
import java.util.List;
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

    @Test
    @DisplayName("커서가 null이면 첫번째 데이터는 가장 최신 데이터가 된다")
    void findBooks_returnsLatestBooks_whenCursorIsNull() {
        // Given
        AuthorEntity author = new AuthorEntity(null, "저자", null, null, null, null);
        em.persist(author);
        PublisherEntity publisher = new PublisherEntity(null, "출판사", null, null, null, null);
        em.persist(publisher);

        BookEntity[] books = new BookEntity[5];
        for (int i = 0; i < 5; i++) {
            books[i] = new BookEntity(null, "isbn" + i, "제목" + i, "부제" + i, null, Instant.now().plusSeconds(i), publisher.getId(), author.getId(), null, null, null, null);
            em.persist(books[i]);
        }
        em.flush();
        em.clear();

        // When
        List<BookDetailResponse> result = bookQueryRepository.findBooks(null, 3);

        // Then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).title()).isEqualTo("제목4");
        assertThat(result.get(2).title()).isEqualTo("제목2");
    }

    @Test
    @DisplayName("커서가 있으면 해당 커서 값 보다 작은 데이터만 반환")
    void findBooks_returnsBooksLessThanCursor() {
        // Given
        AuthorEntity author = new AuthorEntity(null, "저자", null, null, null, null);
        em.persist(author);
        PublisherEntity publisher = new PublisherEntity(null, "출판사", null, null, null, null);
        em.persist(publisher);

        BookEntity[] books = new BookEntity[5];
        for (int i = 0; i < 5; i++) {
            books[i] = new BookEntity(Generators.timeBasedEpochGenerator().generate(), "isbn" + i, "제목" + i, "부제" + i, null, Instant.now().plusSeconds(i), publisher.getId(), author.getId(), null, null, null, null);
            em.persist(books[i]);
        }
        em.flush();
        em.clear();


        BookDetailResponse bookDetailResponse = bookQueryRepository.findById(books[2].getId()).get();
        BookCursor bookCursor = new BookCursor(bookDetailResponse.createdAt(), bookDetailResponse.id());


        // When
        List<BookDetailResponse> result = bookQueryRepository.findBooks(bookCursor, 10);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).title()).isEqualTo("제목1");
        assertThat(result.get(1).title()).isEqualTo("제목0");
    }

    @Test
    @DisplayName("findBooks(BookCursor, int): size 제한이 잘 동작한다")
    void findBooks_sizeLimitWorks() {
        // Given
        AuthorEntity author = new AuthorEntity(null, "저자", null, null, null, null);
        em.persist(author);
        PublisherEntity publisher = new PublisherEntity(null, "출판사", null, null, null, null);
        em.persist(publisher);

        for (int i = 0; i < 10; i++) {
            em.persist(new BookEntity(null, "isbn" + i, "제목" + i, "부제" + i, null, Instant.now().plusSeconds(i), publisher.getId(), author.getId(), null, null, null, null));
        }
        em.flush();
        em.clear();

        // When
        List<BookDetailResponse> result = bookQueryRepository.findBooks(null, 5);

        // Then
        assertThat(result).hasSize(5);
    }

    @Test
    @DisplayName("findBooks(BookCursor, int): createdAt이 같은 경우 id로 정렬이 잘 동작한다.")
    void findBooks_ifCreatedAtIsEqualSuccess() {
        // Given
        Instant now = Instant.now();
        AuthorEntity author = new AuthorEntity(null, "저자", null, null, null, null);
        em.persist(author);
        PublisherEntity publisher = new PublisherEntity(null, "출판사", null, null, null, null);
        em.persist(publisher);
        BookEntity entity1 = new BookEntity(UUID.fromString("00000000-0000-0000-0000-000000000001"), "isbn1", "제목1", "부제1", null, Instant.now(), publisher.getId(), author.getId(), null, null, null, null);
        BookEntity entity2 = new BookEntity(UUID.fromString("00000000-0000-0000-0000-000000000002"), "isbn2", "제목2", "부제2", null, Instant.now(), publisher.getId(), author.getId(), null, null, null, null);
        BookEntity entity3 = new BookEntity(UUID.fromString("00000000-0000-0000-0000-000000000003"), "isbn3", "제목3", "부제3", null, Instant.now(), publisher.getId(), author.getId(), null, null, null, null);
        em.persist(entity1);
        em.persist(entity2);
        em.persist(entity3);

        // createdAt을 동일하게 설정
        em.createQuery("update BookEntity b set b.createdAt = :createdAt")
                .setParameter("createdAt", now)
                .executeUpdate();
        em.flush();
        em.clear();

        // When
        List<BookDetailResponse> books = bookQueryRepository.findBooks(new BookCursor(now, entity2.getId()), 10);

        // Then
        assertThat(books).hasSize(1);
    }
}