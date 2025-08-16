package com.example.trevaribooksearch.application.out;

import com.example.trevaribooksearch.infrastructure.search.model.SearchResult;
import org.springframework.data.domain.Pageable;

public interface SearchEnginePort<T> {
    SearchResult<T> search(String keyword, Pageable pageable);
}
