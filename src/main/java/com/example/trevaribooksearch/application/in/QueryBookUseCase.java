package com.example.trevaribooksearch.application.in;

import com.example.trevaribooksearch.application.dto.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryBookUseCase {
    Page<BookResponse> findBooks(Pageable pageRequest);
}
