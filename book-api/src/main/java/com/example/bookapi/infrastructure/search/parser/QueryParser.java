package com.example.bookapi.infrastructure.search.parser;

public interface QueryParser<R> {
    R parse(String query);
}
