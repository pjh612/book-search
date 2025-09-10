package com.example.bookapi.domain.repository;

import com.example.bookapi.domain.model.Publisher;

import java.util.Optional;
import java.util.UUID;

public interface PublisherRepository {
    Publisher save(Publisher publisher);

    Optional<Publisher> findById(UUID id);
}
