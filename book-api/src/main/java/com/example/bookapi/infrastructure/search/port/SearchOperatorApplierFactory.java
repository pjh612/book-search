package com.example.bookapi.infrastructure.search.port;

import com.example.bookapi.infrastructure.search.model.SearchOperator;

public interface SearchOperatorApplierFactory<T extends SearchOperatorApplier<?>> {
    T getApplier(SearchOperator operator);
}
