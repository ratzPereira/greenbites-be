package com.ratz.greenbites.exception;

public class ForbiddenException extends RuntimeException {

    private final String errorCode;

    public ForbiddenException(String message) {
        super(message);
        this.errorCode = "GENERIC_ERROR";
    }

    public ForbiddenException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
