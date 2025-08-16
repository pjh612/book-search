package com.example.trevaribooksearch.infrastructure.search.model;

public record KeywordToken(
        String keyword,
        SearchOperator operator
) {
}
