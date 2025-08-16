package com.example.bookapi.infrastructure.search.exception;

public class InvalidSearchQueryException extends RuntimeException
{
    public InvalidSearchQueryException() {
    }

    public InvalidSearchQueryException(String message) {
        super(message);
    }
}
