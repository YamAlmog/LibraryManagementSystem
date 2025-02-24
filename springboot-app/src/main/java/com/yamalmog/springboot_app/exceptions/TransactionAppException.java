package com.yamalmog.springboot_app.exceptions;


public class TransactionAppException extends RuntimeException {
    private final ErrorType errorType;
    

    public TransactionAppException(ErrorType errorType, String message){
        super(message);
        this.errorType = errorType;
    }

    public ErrorType getErrorType(){
        return errorType;
    }

    public enum ErrorType {
        BOOK_NOT_FOUND,
        USER_NOT_FOUND
    }
}