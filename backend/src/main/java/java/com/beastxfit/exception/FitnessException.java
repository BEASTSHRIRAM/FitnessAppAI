package com.beastxfit.exception;

public class FitnessException extends RuntimeException {
    private final String errorCode;

    public FitnessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public FitnessException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
} 