package com.example.bookapi.infrastructure.web;

import com.example.bookapi.application.dto.BookCursor;
import com.example.bookapi.application.dto.BookDetailResponse;
import com.example.bookapi.application.dto.BookResponse;
import com.example.bookapi.application.dto.BookSearchRequest;
import com.example.bookapi.application.dto.BookSearchResponse;
import com.example.bookapi.application.facade.QueryBookFacade;
import com.example.bookapi.application.in.QueryBookUseCase;
import com.example.bookapi.common.model.CursorPageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final QueryBookUseCase queryBookUseCase;
    private final QueryBookFacade queryBookFacade;

    @GetMapping
    Page<BookResponse> getBooks(@PageableDefault Pageable pageable) {
        return queryBookUseCase.findBooks(pageable);
    }

    @GetMapping("/cursor")
    CursorPageResponse<BookCursor, BookDetailResponse> getBooks(@Valid @ModelAttribute BookCursor cursor,
                                                                @RequestParam(defaultValue = "10") int size) {
        return queryBookUseCase.findBooks(cursor, size);
    }

    @GetMapping("/{id}")
    BookDetailResponse getById(@PathVariable UUID id) {
        return queryBookUseCase.findById(id);
    }

    @GetMapping("/search")
    BookSearchResponse searchBooks(@RequestParam @Valid @NotBlank String keyword, @PageableDefault Pageable pageable) {
        return queryBookFacade.searchBooks(new BookSearchRequest(keyword, pageable));
    }
}