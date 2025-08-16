package com.example.bookapi.application.dto;

import java.time.Instant;
import java.util.UUID;

public record BookDetailResponse(
        UUID id,
        String title,
        String subtitle,
        String author,
        String image,
        String isbn,
        String publisher,
        Instant published,
        String createdBy,
        String updatedBy,
        Instant createdAt,
        Instant updatedAt
) {
}
