package com.example.bookapi.infrastructure.search.exception;

import com.example.bookapi.common.exception.ApplicationException;
import com.example.bookapi.common.exception.ExceptionCode;

public class InvalidSearchQueryException extends ApplicationException {
    public InvalidSearchQueryException(String message) {
        super(message, ExceptionCode.INVALID_SEARCH_QUERY);
    }
}
