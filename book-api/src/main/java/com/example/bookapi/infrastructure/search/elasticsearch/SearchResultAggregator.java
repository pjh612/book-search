package com.example.bookapi.infrastructure.search.elasticsearch;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.example.bookapi.infrastructure.search.model.SearchOperator;
import com.example.bookapi.infrastructure.search.model.SearchResult;
import org.springframework.data.domain.Pageable;

public interface SearchResultAggregator<T, R> {
    SearchResult<R> mapToResult(String keyword, Pageable pageable, SearchResponse<T> searchResponse, long elapsed, SearchOperator operator);
}
