package com.example.trevaribooksearch.domain.repository;

import com.example.trevaribooksearch.domain.model.Book;

import java.util.List;

public interface BookRepository {
    void saveAll(List<Book> books);
}
