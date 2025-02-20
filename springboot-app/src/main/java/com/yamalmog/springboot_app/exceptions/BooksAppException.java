package com.yamalmog.springboot_app.exceptions;


public class BooksAppException extends RuntimeException {
    private final ErrorType errorType;
    

    public BooksAppException(ErrorType errorType, String message){
        super(message);
        this.errorType = errorType;
    }

    public ErrorType getErrorType(){
        return errorType;
    }

    public enum ErrorType {
        BOOK_ID_ALREADY_EXIST,
        BOOK_NOT_FOUND
    }
}