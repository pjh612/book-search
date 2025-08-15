package com.example.trevaribooksearch.common;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final Environment environment;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        log(Level.WARN, ex);

        ErrorResponse response = new ErrorResponse(
                ex.getMessage(),
                "NOT_FOUND",
                String.valueOf(System.currentTimeMillis()
                ));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        log(Level.WARN, ex);

        ErrorResponse response = new ErrorResponse(
                ex.getMessage(),
                "undefiend",
                String.valueOf(System.currentTimeMillis()
                ));
        return ResponseEntity.badRequest().body(response);
    }

    private void log(Level level, Exception exception) {
        boolean isProd = java.util.Arrays.stream(environment.getActiveProfiles())
                .anyMatch("prod"::equalsIgnoreCase);

        String message = exception.getMessage();
        if (isProd) {
            log.atLevel(level).log(message);
        } else {
            log.atLevel(level).log(message, exception);
        }
    }
}
