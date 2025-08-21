package com.example.bookapi.common.exception;

import com.example.bookapi.common.exception.converter.ExceptionCodeConverter;
import com.example.bookapi.common.exception.converter.impl.DefaultExceptionCodeConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExceptionConfig {

    @Bean
    ExceptionCodeConverter exceptionCodeConverter() {
        return new DefaultExceptionCodeConverter<>(ExceptionCode.class);
    }
}
