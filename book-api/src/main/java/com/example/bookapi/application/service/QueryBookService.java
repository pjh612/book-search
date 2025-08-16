package com.example.bookapi.application.service;

import com.example.bookapi.application.dto.BookDetailResponse;
import com.example.bookapi.application.dto.BookResponse;
import com.example.bookapi.application.dto.BookSearchRequest;
import com.example.bookapi.application.dto.BookSearchResponse;
import com.example.bookapi.application.event.SearchEvent;
import com.example.bookapi.application.in.QueryBookUseCase;
import com.example.bookapi.application.out.BookQueryRepositoryPort;
import com.example.bookapi.application.out.MessagePublisher;
import com.example.bookapi.application.out.SearchEnginePort;
import com.example.bookapi.infrastructure.search.model.SearchResult;
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
    private final MessagePublisher messagePublisher;

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
        String keyword = request.keyword().trim();
        SearchResult<BookResponse> searchResult = searchEngine.search(keyword, request.pageable());

        messagePublisher.publish("search-keyword", new SearchEvent(keyword));

        return BookSearchResponse.from(searchResult);
    }
}
