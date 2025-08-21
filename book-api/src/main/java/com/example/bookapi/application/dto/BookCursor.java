package com.example.bookapi.application.dto;

import jakarta.validation.constraints.AssertTrue;

import java.time.Instant;
import java.util.UUID;

public record BookCursor(
        Instant date,
        UUID id
) {
    @AssertTrue(message = "createdAt과 id는 둘 다 null이거나, 둘 다 값이 있어야 합니다.")
    public boolean isValid() {
        return (date == null && id == null) ||
                (date != null && id != null);
    }
}
