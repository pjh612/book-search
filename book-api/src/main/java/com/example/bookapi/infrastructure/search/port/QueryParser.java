package com.example.bookapi.infrastructure.search.port;

public interface QueryParser<R> {
    R parse(String query);
}
