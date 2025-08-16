package com.example.bookapi.application.out;

import com.example.bookapi.infrastructure.search.model.SearchResult;
import org.springframework.data.domain.Pageable;

public interface SearchEnginePort<T> {
    SearchResult<T> search(String keyword, Pageable pageable);
}
