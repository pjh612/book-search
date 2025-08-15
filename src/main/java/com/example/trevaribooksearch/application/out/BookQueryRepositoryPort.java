package com.example.trevaribooksearch.application.out;

import com.example.trevaribooksearch.application.dto.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookQueryRepositoryPort {
    Page<BookResponse> findBooks(Pageable pageRequest);
}
