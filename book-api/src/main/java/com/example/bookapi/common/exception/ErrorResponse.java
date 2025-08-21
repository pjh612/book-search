package com.example.bookapi.common.exception;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        String message,
        String code,
        int status,
        long timestamp
) {
    public ErrorResponse(String message, String code, int status) {
        this(message, code, status, System.currentTimeMillis());
    }

    public ErrorResponse(String message, String code, HttpStatus status) {
        this(message, code, status.value(), System.currentTimeMillis());
    }
}
