package com.example.bookapi.infrastructure.search.elasticsearch;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public interface QueryBuilder<T> {
    Query build(T searchCriteria);
}
