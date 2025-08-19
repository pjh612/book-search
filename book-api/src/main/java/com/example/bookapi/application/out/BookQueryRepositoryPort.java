package com.example.bookapi.application.out;

import com.example.bookapi.application.dto.BookDetailResponse;
import com.example.bookapi.application.dto.BookResponse;
import com.example.bookapi.common.model.CursorPageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface BookQueryRepositoryPort {
    Page<BookResponse> findBooks(Pageable pageRequest);

    Optional<BookDetailResponse> findById(UUID id);

    CursorPageResponse<Instant, BookDetailResponse> findBooks(Instant cursor, int size);
}

