package com.example.bookapi.application.in;

import com.example.bookapi.application.dto.BookDetailResponse;
import com.example.bookapi.application.dto.BookResponse;
import com.example.bookapi.application.dto.BookSearchRequest;
import com.example.bookapi.application.dto.BookSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface QueryBookUseCase {
    BookDetailResponse findById(UUID id);
    Page<BookResponse> findBooks(Pageable pageRequest);
    BookSearchResponse searchBooks(BookSearchRequest request);
}
