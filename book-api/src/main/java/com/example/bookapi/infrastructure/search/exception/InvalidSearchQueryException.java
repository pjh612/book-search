package com.example.bookapi.infrastructure.search.exception;

public class InvalidSearchQueryException extends SearchException {
    public InvalidSearchQueryException(String message) {
        super(message);
    }
}
