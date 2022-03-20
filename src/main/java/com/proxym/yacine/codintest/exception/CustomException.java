package com.proxym.yacine.codintest.exception;

import java.util.List;

public class CustomException extends RuntimeException{
    private String error;
    private Integer errorCode;
    private List<String> errors;

    public CustomException( String message, String error , List<String> errors,Integer errorCode) {
        super(message);
        this.error = error;
        this.errors = errors;
        this.errorCode = errorCode;
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message,String error) {
        super(message);
        this.error = error;
    }
    public CustomException(String message,String error,Integer errorCode) {
        super(message);
        this.error = error;
        this.errorCode = errorCode;
    }
    public CustomException(String message,Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    public CustomException(String message,List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public String getError() {
        return error;
    }

    public List<String> getErrors() {
        return errors;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}

