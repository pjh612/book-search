package com.example.bookapi.application.dto;

import com.example.bookapi.infrastructure.search.model.SearchOperator;
import com.example.bookapi.infrastructure.search.model.SearchResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
public class BookSearchResponse {
    private String searchQuery;
    private PageInfo pageInfo;
    private List<BookResponse> books;
    private SearchMetaData searchMetaData;

    public BookSearchResponse(
            String searchQuery,
            PageInfo pageInfo,
            List<BookResponse> books,
            SearchMetaData searchMetaData
    ) {
        this.searchQuery = searchQuery;
        this.pageInfo = pageInfo;
        this.books = books;
        this.searchMetaData = searchMetaData;
    }

    public BookSearchResponse(String searchQuery, PageInfo pageInfo, List<BookResponse> books, long executionTime, SearchOperator strategy) {
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

    public String searchQuery() {
        return searchQuery;
    }

    public PageInfo pageInfo() {
        return pageInfo;
    }

    public List<BookResponse> books() {
        return books;
    }

    public SearchMetaData searchMetaData() {
        return searchMetaData;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (BookSearchResponse) obj;
        return Objects.equals(this.searchQuery, that.searchQuery) &&
                Objects.equals(this.pageInfo, that.pageInfo) &&
                Objects.equals(this.books, that.books) &&
                Objects.equals(this.searchMetaData, that.searchMetaData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(searchQuery, pageInfo, books, searchMetaData);
    }

    @Override
    public String toString() {
        return "BookSearchResponse[" +
                "searchQuery=" + searchQuery + ", " +
                "pageInfo=" + pageInfo + ", " +
                "books=" + books + ", " +
                "searchMetaData=" + searchMetaData + ']';
    }

    @Data
    @NoArgsConstructor
    public static class SearchMetaData {
        private long executionTime;
        private SearchOperator strategy;

        public SearchMetaData(
                long executionTime,
                SearchOperator strategy
        ) {
            this.executionTime = executionTime;
            this.strategy = strategy;
        }

        public long executionTime() {
            return executionTime;
        }

        public SearchOperator strategy() {
            return strategy;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (SearchMetaData) obj;
            return this.executionTime == that.executionTime &&
                    Objects.equals(this.strategy, that.strategy);
        }

        @Override
        public int hashCode() {
            return Objects.hash(executionTime, strategy);
        }

        @Override
        public String toString() {
            return "SearchMetaData[" +
                    "executionTime=" + executionTime + ", " +
                    "strategy=" + strategy + ']';
        }


    }

    @Data
    @NoArgsConstructor
    public static class PageInfo {
        private int currentPage;
        private int pageSize;
        private int totalPages;
        private long totalElements;

        public PageInfo(
                int currentPage,
                int pageSize,
                int totalPages,
                long totalElements
        ) {
            this.currentPage = currentPage;
            this.pageSize = pageSize;
            this.totalPages = totalPages;
            this.totalElements = totalElements;
        }

        public int currentPage() {
            return currentPage;
        }

        public int pageSize() {
            return pageSize;
        }

        public int totalPages() {
            return totalPages;
        }

        public long totalElements() {
            return totalElements;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (PageInfo) obj;
            return this.currentPage == that.currentPage &&
                    this.pageSize == that.pageSize &&
                    this.totalPages == that.totalPages &&
                    this.totalElements == that.totalElements;
        }

        @Override
        public int hashCode() {
            return Objects.hash(currentPage, pageSize, totalPages, totalElements);
        }

        @Override
        public String toString() {
            return "PageInfo[" +
                    "currentPage=" + currentPage + ", " +
                    "pageSize=" + pageSize + ", " +
                    "totalPages=" + totalPages + ", " +
                    "totalElements=" + totalElements + ']';
        }

    }
}
