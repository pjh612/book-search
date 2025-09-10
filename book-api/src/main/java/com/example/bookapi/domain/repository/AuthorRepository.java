package com.example.bookapi.domain.repository;

import com.example.bookapi.domain.model.Author;

import java.util.Optional;
import java.util.UUID;

public interface AuthorRepository {
    Author save(Author author);

    Optional<Author> findById(UUID uuid);
}
