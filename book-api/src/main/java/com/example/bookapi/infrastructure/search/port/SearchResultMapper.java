package com.example.bookapi.infrastructure.search.port;

@FunctionalInterface
public interface SearchResultMapper<T, C> {
    C map(T source);
}
