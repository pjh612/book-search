package com.example.bookapi.common.exception;

public class InvalidCodeException extends RuntimeException{

    public InvalidCodeException(String message) {
        super(message);
    }
}
