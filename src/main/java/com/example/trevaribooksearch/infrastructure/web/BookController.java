package com.example.trevaribooksearch.infrastructure.web;


import com.example.trevaribooksearch.application.dto.BookDetailResponse;
import com.example.trevaribooksearch.application.dto.BookResponse;
import com.example.trevaribooksearch.application.in.QueryBookUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final QueryBookUseCase queryBookUseCase;

    @GetMapping
    Page<BookResponse> getBooks(@PageableDefault Pageable pageable) {
        return queryBookUseCase.findBooks(pageable);
    }

    @GetMapping("/{id}")
    BookDetailResponse getById(@PathVariable UUID id) {
        return queryBookUseCase.findById(id);
    }
}