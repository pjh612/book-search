package com.example.bookapi.application.out;

import com.example.bookapi.application.dto.BookDetailResponse;
import com.example.bookapi.application.dto.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface BookQueryRepositoryPort {
    Page<BookResponse> findBooks(Pageable pageRequest);

    Optional<BookDetailResponse> findById(UUID id);
}

