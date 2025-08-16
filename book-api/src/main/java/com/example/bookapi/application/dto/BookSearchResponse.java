package com.example.bookapi.application.dto;

import com.example.bookapi.infrastructure.search.model.SearchOperator;
import com.example.bookapi.infrastructure.search.model.SearchResult;

import java.util.Collection;

public record BookSearchResponse(
        String searchQuery,
        PageInfo pageInfo,
        Collection<BookResponse> books,
        SearchMetaData searchMetaData
) {
    public BookSearchResponse(String searchQuery, PageInfo pageInfo, Collection<BookResponse> books, long executionTime, SearchOperator strategy) {
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
                searchResult.results().getContent(),
                searchResult.searchMetaData().executionTime(),
                searchResult.searchMetaData().strategy()
        );
    }

    public record SearchMetaData(
            long executionTime,
            SearchOperator strategy
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
