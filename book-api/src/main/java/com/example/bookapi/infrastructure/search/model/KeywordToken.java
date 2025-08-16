package com.example.bookapi.infrastructure.search.model;

public record KeywordToken(
        String keyword,
        SearchOperator operator
) {
}
