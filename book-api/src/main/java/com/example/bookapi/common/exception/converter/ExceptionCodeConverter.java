package com.example.bookapi.common.exception.converter;


import com.example.bookapi.common.exception.ApplicationException;
import com.example.bookapi.common.exception.ErrorResponse;

public interface ExceptionCodeConverter {

    ApplicationException toError(String code);

    ErrorResponse toResponse(ApplicationException exception);
}
