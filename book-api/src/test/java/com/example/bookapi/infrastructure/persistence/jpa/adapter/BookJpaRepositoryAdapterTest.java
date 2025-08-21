package com.example.bookapi.infrastructure.persistence.jpa.adapter;

import com.example.bookapi.application.dto.BookCursor;
import com.example.bookapi.application.dto.BookDetailResponse;
import com.example.bookapi.common.model.CursorPageResponse;
import com.example.bookapi.domain.model.Isbn;
import com.example.bookapi.infrastructure.persistence.jpa.repository.BookJpaRepository;
import com.example.bookapi.infrastructure.persistence.jpa.repository.BookQueryRepository;
import com.fasterxml.uuid.Generators;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookJpaRepositoryAdapterTest {

    BookJpaRepository bookJpaRepository = mock(BookJpaRepository.class);
    BookQueryRepository bookQueryRepository = mock(BookQueryRepository.class);
    BookJpaRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new BookJpaRepositoryAdapter(bookJpaRepository, bookQueryRepository);
    }

    @Test
    @DisplayName("도서 결과가 없으면 커서가 null이다.")
    void findBooks_cursorShouldNullIfEmptyResult() {
        when(bookQueryRepository.findBooks(any(), anyInt())).thenReturn(List.of());

        CursorPageResponse<BookCursor, BookDetailResponse> result = adapter.findBooks(new BookCursor(null, null), 10);

        assertThat(result.getCursor()).isNull();
        assertThat(result.getItems()).isEmpty();
    }

    @Test
    @DisplayName("도서 결과가 있으면 마지막 요소의 생성일이 커서로 설정된다.")
    void findBooks_ShouldCursorIsLastElement() {
        UUID id1 = Generators.timeBasedEpochGenerator().generate();
        UUID id2 = Generators.timeBasedEpochGenerator().generate();
        BookDetailResponse book1 = new BookDetailResponse(id1, "title1", "subtitle1", "author", null, Isbn.randomIsbn13(), "publisher", Instant.now(), "test", "test", Instant.now(), Instant.now());
        BookDetailResponse book2 = new BookDetailResponse(id2, "title2", "subtitle2", "author", null, Isbn.randomIsbn13(), "publisher", Instant.now(), "test", "test", Instant.now(), Instant.now());
        List<BookDetailResponse> books = List.of(book2, book1);

        when(bookQueryRepository.findBooks(any(), anyInt())).thenReturn(books);

        CursorPageResponse<BookCursor, BookDetailResponse> result = adapter.findBooks(new BookCursor(null, null), 10);

        assertThat(result.getCursor().date()).isEqualTo(book1.createdAt());
        assertThat(result.getCursor().id()).isEqualTo(book1.id());
        assertThat(result.getItems()).containsExactly(book2, book1);
    }
}