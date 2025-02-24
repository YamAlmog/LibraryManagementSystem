package com.yamalmog.springboot_app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {
    
    // Handle Book-related exception
    @ExceptionHandler(BooksAppException.class)
    public ResponseEntity<Map<String, String>> handleBookAppException(BooksAppException ex) {
        return buildErrorResponse(ex.getErrorType().name(), ex.getMessage(), getHttpStatus(ex.getErrorType()));
    }

    // Handle User-related exception
    @ExceptionHandler(UserAppException.class)
    public ResponseEntity<Map<String, String>> handleUserAppException(UserAppException ex) {
        return buildErrorResponse(ex.getErrorType().name(), ex.getMessage(), getHttpStatus(ex.getErrorType()));
    }


    // Common method to build response
    private ResponseEntity<Map<String, String>> buildErrorResponse(String errorType, String message, HttpStatus status) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", message);
        errorResponse.put("errorType", errorType);
        return ResponseEntity.status(status).body(errorResponse);
    }

    private HttpStatus getHttpStatus(BooksAppException.ErrorType errorType) {
        return switch (errorType) {
            case BOOK_ID_ALREADY_EXIST -> HttpStatus.CONFLICT; // 409 Conflict
            case BOOK_NOT_FOUND -> HttpStatus.NOT_FOUND; // 404 Not Found
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

    private HttpStatus getHttpStatus(UserAppException.ErrorType errorType) {
        return switch (errorType) {
            case USER_ID_ALREADY_EXIST -> HttpStatus.CONFLICT; // 409 Conflict
            case USER_NOT_FOUND -> HttpStatus.NOT_FOUND; // 404 Not Found
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

}
