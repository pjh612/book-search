package com.example.trevaribooksearch.infrastructure.search.exception;

public class InvalidSearchQueryException extends RuntimeException
{
    public InvalidSearchQueryException() {
    }

    public InvalidSearchQueryException(String message) {
        super(message);
    }
}
