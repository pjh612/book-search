package com.example.bookweb.controller;

import com.example.bookweb.client.BookApiClient;
import com.example.bookweb.dto.BookDetailResponse;
import com.example.bookweb.dto.BookResponse;
import com.example.bookweb.dto.BookSearchRequest;
import com.example.bookweb.dto.BookSearchResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class BookController {
    private final BookApiClient bookApiClient;

    @GetMapping("books")
    String bookPage() {
        return "books";
    }

    @GetMapping("/books/{id}")
    public String detailPage(@PathVariable UUID id) {
        return "book-detail";
    }

    @ResponseBody
    @GetMapping("/api/books")
    Page<BookResponse> getBooks(@PageableDefault Pageable pageable) {
        return bookApiClient.getBooks(pageable);
    }

    @ResponseBody
    @GetMapping("/api/books/{id}")
    public ResponseEntity<BookDetailResponse> getById(@PathVariable UUID id) {
            BookDetailResponse body = bookApiClient.getBookDetailById(id);
            return ResponseEntity.ok(body);
    }

    @ResponseBody
    @GetMapping("/api/books/search")
    BookSearchResponse searchBooks(@RequestParam @Valid @NotBlank String keyword, @PageableDefault Pageable pageable) {
        BookSearchRequest request = new BookSearchRequest(keyword, pageable);
        return bookApiClient.searchBooks(request);
    }


}
