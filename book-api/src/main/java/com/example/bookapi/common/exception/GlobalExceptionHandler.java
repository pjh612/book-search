package com.example.bookapi.common.exception;

import com.example.bookapi.common.exception.converter.ExceptionCodeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final Environment environment;
    private final ExceptionCodeConverter exceptionCodeConverter;

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException ex) {
        log(Level.WARN, ex);

        ErrorResponse response = exceptionCodeConverter.toResponse(ex);

        return new ResponseEntity<>(response, HttpStatus.valueOf(response.status()));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleMethodValidationException(HandlerMethodValidationException ex) {
        log(Level.WARN, ex);

        ErrorResponse response = new ErrorResponse(
                "요청 파라미터가 유효하지 않습니다: ",
                "VALIDATION_ERROR",
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log(Level.WARN, ex);

        ErrorResponse response = new ErrorResponse(
                "요청 파라미터의 타입이 일치하지 않습니다: " + ex.getPropertyName(),
                "TYPE_MISMATCH",
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        log(Level.WARN, ex);

        ErrorResponse response = new ErrorResponse(
                ex.getMessage(),
                "undefiend",
                HttpStatus.BAD_REQUEST
        );
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
