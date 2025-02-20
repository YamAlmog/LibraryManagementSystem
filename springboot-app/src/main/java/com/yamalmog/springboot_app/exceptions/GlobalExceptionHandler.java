package com.yamalmog.springboot_app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BooksAppException.class)
    public ResponseEntity<Map<String, String>> handleBookAppException(BooksAppException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        errorResponse.put("errorType", ex.getErrorType().name());

        return ResponseEntity
            .status(getHttpStatus(ex.getErrorType()))
            .body(errorResponse);
    }

    private HttpStatus getHttpStatus(BooksAppException.ErrorType errorType) {
        return switch (errorType) {
            case BOOK_ID_ALREADY_EXIST -> HttpStatus.CONFLICT; // 409 Conflict
            case BOOK_NOT_FOUND -> HttpStatus.NOT_FOUND; // 404 Not Found
        };
    }

}
