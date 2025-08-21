package com.example.bookapi.common.exception.converter.impl;

import com.example.bookapi.common.exception.ErrorResponse;
import com.example.bookapi.common.exception.ApplicationException;
import com.example.bookapi.common.exception.ExceptionContent;
import com.example.bookapi.common.exception.InvalidCodeException;
import com.example.bookapi.common.exception.converter.ExceptionCodeConverter;

import java.util.Arrays;

public class DefaultExceptionCodeConverter<E extends Enum<E> & ExceptionContent> implements ExceptionCodeConverter {
    private final Class<? extends ExceptionContent> exceptionContent;

    public DefaultExceptionCodeConverter(Class<E> exceptionContent) {
        this.exceptionContent = exceptionContent;
    }

    @Override
    public ApplicationException toError(String code) {
        return Arrays.stream(exceptionContent.getEnumConstants())
                .filter(it -> it.getCode().equals(code))
                .findFirst()
                .map(ApplicationException::new)
                .orElseThrow(() -> new InvalidCodeException("code[" + code + "] is not found"));
    }

    @Override
    public ErrorResponse toResponse(ApplicationException exception) {
        ExceptionContent content = exception.getExceptionContent();
        String message = content.getDetail() == null
                ? exception.getMessage()
                : content.getDetail();
        return new ErrorResponse(message, content.getCode(), content.getStatus());
    }
}
