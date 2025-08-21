package com.example.bookapi.infrastructure.persistence.jpa.adapter;

import com.example.bookapi.application.dto.BookCursor;
import com.example.bookapi.application.dto.BookDetailResponse;
import com.example.bookapi.application.dto.BookResponse;
import com.example.bookapi.application.out.BookQueryRepositoryPort;
import com.example.bookapi.common.model.CursorPageResponse;
import com.example.bookapi.domain.model.Book;
import com.example.bookapi.domain.repository.BookRepository;
import com.example.bookapi.infrastructure.persistence.jpa.mapper.BookMapper;
import com.example.bookapi.infrastructure.persistence.jpa.repository.BookJpaRepository;
import com.example.bookapi.infrastructure.persistence.jpa.repository.BookQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BookJpaRepositoryAdapter implements BookRepository, BookQueryRepositoryPort {
    private final BookJpaRepository bookJpaRepository;
    private final BookQueryRepository bookQueryRepository;

    @Override
    public Page<BookResponse> findBooks(Pageable pageRequest) {
        return bookQueryRepository.findBooks(pageRequest);
    }

    @Override
    public Optional<BookDetailResponse> findById(UUID id) {
        return bookQueryRepository.findById(id);
    }

    @Override
    public CursorPageResponse<BookCursor, BookDetailResponse> findBooks(BookCursor cursor, int size) {
        List<BookDetailResponse> books = bookQueryRepository.findBooks(cursor, size);
        if (books.isEmpty()) {
            return new CursorPageResponse<>(null, books);
        }

        BookCursor nextCursor = new BookCursor(books.getLast().createdAt(), books.getLast().id());
        return new CursorPageResponse<>(nextCursor, books);
    }

    @Override
    public void saveAll(List<Book> books) {
        bookJpaRepository.saveAll(books.stream()
                .map(BookMapper::toEntity)
                .toList());
    }
}
