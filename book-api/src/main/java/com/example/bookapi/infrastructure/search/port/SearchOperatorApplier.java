package com.example.bookapi.infrastructure.search.port;

public interface SearchOperatorApplier<T> {
    T apply(T builder, String keyword);
}
