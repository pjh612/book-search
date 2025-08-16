package com.example.bookapi.domain.repository;

import com.example.bookapi.domain.model.Book;

import java.util.List;

public interface BookRepository {
    void saveAll(List<Book> books);
}
