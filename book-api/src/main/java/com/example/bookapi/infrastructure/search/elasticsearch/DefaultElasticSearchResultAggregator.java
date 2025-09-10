package com.example.bookapi.infrastructure.search.elasticsearch;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import com.example.bookapi.infrastructure.search.port.SearchResultMapper;
import com.example.bookapi.infrastructure.search.model.SearchOperator;
import com.example.bookapi.infrastructure.search.model.SearchResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;

public class DefaultElasticSearchResultAggregator<T, R> implements SearchResultAggregator<T, R> {
    private final SearchResultMapper<T, R> mapper;

    public DefaultElasticSearchResultAggregator(SearchResultMapper<T, R> mapper) {
        this.mapper = mapper;
    }

    @Override
    public SearchResult<R> mapToResult(String keyword, Pageable pageable, SearchResponse<T> searchResponse, long elapsed, SearchOperator operator) {
        Page<R> page = new PageImpl<>(
                getContent(searchResponse),
                pageable,
                getTotal(searchResponse)
        );

        return SearchResult.of(
                keyword,
                page,
                elapsed,
                operator
        );
    }

    private List<R> getContent(SearchResponse<T> search) {
        return search.hits()
                .hits()
                .stream()
                .map(Hit::source)
                .filter(Objects::nonNull)
                .map(mapper::map)
                .toList();
    }

    private long getTotal(SearchResponse<T> search) {
        TotalHits totalHits = search.hits().total();
        return totalHits != null
                ? totalHits.value()
                : 0L;
    }
}
