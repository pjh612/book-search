package com.example.bookweb.dto;

import java.util.ArrayList;
import java.util.List;

public record BookSearchResponse(
        String searchQuery,
        PageInfo pageInfo,
        List<BookResponse> books,
        SearchMetaData searchMetaData
) {
    public BookSearchResponse(String searchQuery, PageInfo pageInfo, List<BookResponse> books, long executionTime, SearchOperatorType strategy) {
        this(searchQuery, pageInfo, books, new SearchMetaData(executionTime, strategy));
    }

    public static BookSearchResponse from(SearchResult<BookResponse> searchResult) {
        return new BookSearchResponse(
                searchResult.searchQuery(),
                new PageInfo(
                        searchResult.results().getNumber(),
                        searchResult.results().getSize(),
                        searchResult.results().getTotalPages(),
                        searchResult.results().getTotalElements()
                ),
                new ArrayList<>(searchResult.results().getContent()),
                searchResult.searchMetaData().executionTime(),
                searchResult.searchMetaData().strategy()
        );
    }

    public record SearchMetaData(
            long executionTime,
            SearchOperatorType strategy
    ) {
    }

    public record PageInfo(
            int currentPage,
            int pageSize,
            int totalPages,
            long totalElements
    ) {
    }
}
