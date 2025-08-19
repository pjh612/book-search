package com.example.bookweb.dto;

import org.springframework.data.domain.Page;

public record SearchResult<T>(
        String searchQuery,
        Page<T> results,
        SearchMetaData searchMetaData

) {
    public record SearchMetaData(
            long executionTime,
            SearchOperatorType strategy
    ) {
    }
}
