package com.example.trevaribooksearch.application.in;

import com.example.trevaribooksearch.application.dto.BookDetailResponse;
import com.example.trevaribooksearch.application.dto.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface QueryBookUseCase {
    BookDetailResponse findById(UUID id);
    Page<BookResponse> findBooks(Pageable pageRequest);
}
