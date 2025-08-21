package com.example.bookapi.common.exception.converter;


import com.example.bookapi.common.exception.ErrorResponse;
import com.example.bookapi.common.exception.ApplicationException;

public interface ExceptionCodeConverter {

    ApplicationException toError(String code);

    ErrorResponse toResponse(ApplicationException exception);
}
