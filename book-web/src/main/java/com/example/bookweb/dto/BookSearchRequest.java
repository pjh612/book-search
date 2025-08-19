package com.example.bookweb.dto;

import org.springframework.data.domain.Pageable;

public record BookSearchRequest(String keyword, Pageable pageable) {
}
