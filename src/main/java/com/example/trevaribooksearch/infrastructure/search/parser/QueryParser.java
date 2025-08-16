package com.example.trevaribooksearch.infrastructure.search.parser;

public interface QueryParser<R> {
    R parse(String query);
}
