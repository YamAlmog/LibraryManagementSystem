package com.yamalmog.springboot_app.exceptions;


public class UserAppException extends RuntimeException{
    private final ErrorType errorType;

    public UserAppException(ErrorType errorType, String message){
        super(message);
        this.errorType = errorType;
    }

    public ErrorType getErrorType(){
        return errorType;
    }

    public enum ErrorType{
        USER_ID_ALREADY_EXIST,
        USER_NOT_FOUND
    }
}
