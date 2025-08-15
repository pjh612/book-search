package com.example.trevaribooksearch.application.service;

import com.example.trevaribooksearch.application.dto.BookResponse;
import com.example.trevaribooksearch.application.in.QueryBookUseCase;
import com.example.trevaribooksearch.application.out.BookQueryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueryBookService implements QueryBookUseCase {
    private final BookQueryRepositoryPort bookQueryRepository;

    @Override
    public Page<BookResponse> findBooks(Pageable pageRequest) {
        return bookQueryRepository.findBooks(pageRequest);
    }
}
