package com.example.trevaribooksearch.common;

public record ErrorResponse(
        String message,
        String code,
        String timestamp
) {
}
