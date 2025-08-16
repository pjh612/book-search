package com.example.trevaribooksearch.application.service;

import com.example.trevaribooksearch.application.dto.BookDetailResponse;
import com.example.trevaribooksearch.application.dto.BookResponse;
import com.example.trevaribooksearch.application.dto.BookSearchRequest;
import com.example.trevaribooksearch.application.dto.BookSearchResponse;
import com.example.trevaribooksearch.application.in.QueryBookUseCase;
import com.example.trevaribooksearch.application.out.BookQueryRepositoryPort;
import com.example.trevaribooksearch.application.out.SearchEnginePort;
import com.example.trevaribooksearch.infrastructure.search.model.SearchResult;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QueryBookService implements QueryBookUseCase {
    private final BookQueryRepositoryPort bookQueryRepository;
    private final SearchEnginePort<BookResponse> searchEngine;

    @Override
    public BookDetailResponse findById(UUID id) {
        return bookQueryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("도서 정보를 찾을 수 없습니다. ID: " + id));
    }

    @Override
    public Page<BookResponse> findBooks(Pageable pageRequest) {
        return bookQueryRepository.findBooks(pageRequest);
    }

    @Override
    public BookSearchResponse searchBooks(BookSearchRequest request) {
        SearchResult<BookResponse> searchResult = searchEngine.search(request.keyword(), request.pageable());

        return BookSearchResponse.from(searchResult);
    }
}
