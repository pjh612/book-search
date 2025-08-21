package com.example.bookapi.common.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    private final ExceptionContent exceptionContent;

    public ApplicationException(ExceptionContent exceptionContent) {
        super(exceptionContent.getDetail());
        this.exceptionContent = exceptionContent;
    }

    public ApplicationException(String message, ExceptionContent exceptionContent) {
        super(message);
        this.exceptionContent = exceptionContent;
    }

    public ApplicationException(String message, Throwable cause, ExceptionContent exceptionContent) {
        super(message, cause);
        this.exceptionContent = exceptionContent;
    }

    public ApplicationException(Throwable cause, ExceptionContent exceptionContent) {
        super(cause);
        this.exceptionContent = exceptionContent;
    }
}
