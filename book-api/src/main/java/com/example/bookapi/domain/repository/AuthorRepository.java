package com.example.bookapi.domain.repository;

import com.example.bookapi.domain.model.Author;

public interface AuthorRepository {
    Author save(Author author);
}
