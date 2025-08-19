package com.example.bookweb.dto;

import java.time.Instant;
import java.util.UUID;

public record BookResponse(UUID id,
                           String title,
                           String subtitle,
                           String author,
                           String image,
                           String isbn,
                           String publisher,
                           Instant published) {
}
