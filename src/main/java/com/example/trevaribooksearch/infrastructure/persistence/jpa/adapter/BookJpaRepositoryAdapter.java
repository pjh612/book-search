package com.example.trevaribooksearch.infrastructure.persistence.jpa.adapter;

import com.example.trevaribooksearch.application.dto.BookResponse;
import com.example.trevaribooksearch.application.out.BookQueryRepositoryPort;
import com.example.trevaribooksearch.domain.model.Book;
import com.example.trevaribooksearch.domain.repository.BookRepository;
import com.example.trevaribooksearch.infrastructure.persistence.jpa.mapper.BookMapper;
import com.example.trevaribooksearch.infrastructure.persistence.jpa.repository.BookJpaRepository;
import com.example.trevaribooksearch.infrastructure.persistence.jpa.repository.BookQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public void saveAll(List<Book> books) {
        bookJpaRepository.saveAll(books.stream()
                .map(BookMapper::toEntity)
                .toList());
    }
}
