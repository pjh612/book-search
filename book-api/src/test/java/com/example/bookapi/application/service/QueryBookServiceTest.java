package com.example.bookapi.application.service;

import com.example.bookapi.application.dto.BookDetailResponse;
import com.example.bookapi.application.dto.BookResponse;
import com.example.bookapi.application.dto.BookSearchRequest;
import com.example.bookapi.application.dto.BookSearchResponse;
import com.example.bookapi.application.out.MessagePublisher;
import com.example.bookapi.application.out.SearchEnginePort;
import com.example.bookapi.common.exception.ApplicationException;
import com.example.bookapi.infrastructure.persistence.jpa.adapter.BookJpaRepositoryAdapter;
import com.example.bookapi.infrastructure.search.model.SearchOperatorType;
import com.example.bookapi.infrastructure.search.model.SearchResult;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QueryBookServiceTest {

    @Mock
    BookJpaRepositoryAdapter bookJpaRepositoryAdapter;

    @Mock
    SearchEnginePort<BookResponse> searchEngine;

    @Mock
    MessagePublisher messagePublisher;

    @InjectMocks
    QueryBookService service;

    @Test
    @DisplayName("findBooks가 BookQueryRepository를 통해 결과를 반환한다")
    void findBooks_returnsBooks() {
        // given
        BookResponse book = new BookResponse(
                UUID.randomUUID(), "테스트책", "부제", "저자", null, "isbn", "출판사", Instant.now()
        );
        Page<BookResponse> page = new PageImpl<>(List.of(book), PageRequest.of(0, 10), 1);
        when(bookJpaRepositoryAdapter.findBooks(any())).thenReturn(page);

        // when
        Page<BookResponse> result = service.findBooks(PageRequest.of(0, 10));

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).title()).isEqualTo("테스트책");
        verify(bookJpaRepositoryAdapter, times(1)).findBooks(any());
    }

    @Test
    @DisplayName("findBooks가 데이터가 없을 때 빈 페이지를 반환한다")
    void findBooks_returnsEmptyPage() {
        // given
        Page<BookResponse> emptyPage = Page.empty(PageRequest.of(0, 10));
        when(bookJpaRepositoryAdapter.findBooks(any())).thenReturn(emptyPage);

        // when
        Page<BookResponse> result = service.findBooks(PageRequest.of(0, 10));

        // then
        assertThat(result.getTotalElements()).isZero();
        assertThat(result.getContent()).isEmpty();
    }

    @Test
    @DisplayName("findById가 BookDetailResponse를 반환한다")
    void findById_returnsBookDetailResponse() {
        // given
        UUID id = UUID.randomUUID();
        BookDetailResponse response = new BookDetailResponse(
                id, "테스트책", "부제", "저자", null, "isbn", "출판사",
                Instant.now(), "admin", "admin", Instant.now(), Instant.now()
        );
        when(bookJpaRepositoryAdapter.findById(id)).thenReturn(Optional.of(response));

        // when
        BookDetailResponse result = service.findById(id);

        // then
        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("findById가 데이터가 없으면 예외를 던진다")
    void findById_throwsException() {
        // given
        UUID id = UUID.randomUUID();
        when(bookJpaRepositoryAdapter.findById(id)).thenReturn(Optional.empty());

        // expect
        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("도서 정보를 찾을 수 없습니다");
    }

    @Test
    @DisplayName("searchBooks가 SearchEnginePort를 통해 BookSearchResponse를 반환한다")
    void searchBooks_returnsBookSearchResponse() {
        // given
        String keyword = "테스트";
        PageRequest pageRequest = PageRequest.of(0, 10);
        BookSearchRequest request = new BookSearchRequest(keyword, pageRequest);

        BookResponse book = new BookResponse(
                UUID.randomUUID(), "테스트책", "부제", "저자", null, "isbn", "출판사", Instant.now()
        );
        List<BookResponse> content = List.of(book);
        Page<BookResponse> page = new PageImpl<>(content, pageRequest, 1);

        SearchResult.SearchMetaData metaData =
                new SearchResult.SearchMetaData(
                        123L, SearchOperatorType.NO_OPERATOR
                );

        SearchResult<BookResponse> searchResult =
                new SearchResult<>(
                        keyword, page, metaData
                );

        when(searchEngine.search(keyword, pageRequest)).thenReturn(searchResult);

        // when
        BookSearchResponse response = service.searchBooks(request);

        // then
        assertThat(response.searchQuery()).isEqualTo(keyword);
        assertThat(response.pageInfo().currentPage()).isEqualTo(0);
        assertThat(response.pageInfo().pageSize()).isEqualTo(10);
        assertThat(response.pageInfo().totalPages()).isEqualTo(1);
        assertThat(response.pageInfo().totalElements()).isEqualTo(1);
        assertThat(response.books()).hasSize(1);
        assertThat(response.books().iterator().next().title()).isEqualTo("테스트책");
        assertThat(response.searchMetaData().executionTime()).isEqualTo(123L);
        assertThat(response.searchMetaData().strategy())
                .isEqualTo(SearchOperatorType.NO_OPERATOR);
    }
}