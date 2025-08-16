package com.example.bookapi.infrastructure.web;


import com.example.bookapi.application.dto.BookDetailResponse;
import com.example.bookapi.application.dto.BookResponse;
import com.example.bookapi.application.dto.BookSearchRequest;
import com.example.bookapi.application.dto.BookSearchResponse;
import com.example.bookapi.application.in.QueryBookUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/search")
    BookSearchResponse searchBooks(@RequestParam @Valid @NotBlank String keyword, @PageableDefault Pageable pageable) {
        return queryBookUseCase.searchBooks(new BookSearchRequest(keyword, pageable));
    }
}