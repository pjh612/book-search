package com.example.bookapi.application.dto;

import org.springframework.data.domain.Pageable;

public record BookSearchRequest(String keyword, Pageable pageable) {
}
