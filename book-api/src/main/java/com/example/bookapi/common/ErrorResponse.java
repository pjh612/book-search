package com.example.bookapi.common;

public record ErrorResponse(
        String message,
        String code,
        String timestamp
) {
}
