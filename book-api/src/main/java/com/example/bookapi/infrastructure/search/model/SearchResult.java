package com.example.bookapi.infrastructure.search.model;

import org.springframework.data.domain.Page;

public record SearchResult<T>(
        String searchQuery,
        Page<T> results,
        SearchMetaData searchMetaData

) {
    public static <T> SearchResult<T> of(
            String searchQuery,
            Page<T> results,
            long executionTime,
            SearchOperator strategy
    ) {
        return new SearchResult<>(
                searchQuery,
                results,
                new SearchMetaData(executionTime, strategy)
        );
    }

    public record SearchMetaData(
            long executionTime,
            SearchOperator strategy
    ) {
    }
}
