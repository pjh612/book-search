package com.example.bookapi.application.service;

import com.example.bookapi.application.dto.BookDetailResponse;
import com.example.bookapi.application.dto.BookResponse;
import com.example.bookapi.application.dto.BookSearchRequest;
import com.example.bookapi.application.dto.BookSearchResponse;
import com.example.bookapi.application.in.QueryBookUseCase;
import com.example.bookapi.application.out.BookQueryRepositoryPort;
import com.example.bookapi.application.out.SearchEnginePort;
import com.example.bookapi.common.model.CursorPageResponse;
import com.example.bookapi.infrastructure.search.model.SearchResult;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QueryBookService implements QueryBookUseCase {
    private final BookQueryRepositoryPort bookQueryRepository;
    private final SearchEnginePort<BookResponse> searchEngine;

    @Override
    @Cacheable(value = "bookDetail", key = "#id")
    public BookDetailResponse findById(UUID id) {
        return bookQueryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("도서 정보를 찾을 수 없습니다. ID: " + id));
    }

    @Override
    @Cacheable(value = "bookList", key = "#pageRequest.pageNumber + ':' + #pageRequest.pageSize + ':' + #pageRequest.sort.toString()")
    public Page<BookResponse> findBooks(Pageable pageRequest) {
        return bookQueryRepository.findBooks(pageRequest);
    }

    @Override
    @Cacheable(value = "bookList", key = "#cursor + ':' + #size")
    public CursorPageResponse<Instant, BookDetailResponse> findBooks(Instant cursor, int size) {
        return bookQueryRepository.findBooks(cursor, size);
    }

    @Override
    @Cacheable(value = "bookSearch",
            key = "#request.keyword() + ':' + #request.pageable.pageNumber + ':' + #request.pageable.pageSize + ':' + #request.pageable.sort.toString()")
    public BookSearchResponse searchBooks(BookSearchRequest request) {
        String keyword = request.keyword().trim();
        SearchResult<BookResponse> searchResult = searchEngine.search(keyword, request.pageable());

        return BookSearchResponse.from(searchResult);
    }
}
