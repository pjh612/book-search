package com.example.bookapi.application.dto;

import java.time.Instant;
import java.util.UUID;

public record CreateBookRequest(
        String title,
        String subtitle,
        String isbn,
        String image,
        UUID publisherId,
        UUID authorId,
        Instant published,
        String createdBy
) {
}
