package com.example.bookweb.dto;

import java.time.Instant;
import java.util.UUID;

public record BookCursor(
        Instant date,
        UUID id
) {
}
