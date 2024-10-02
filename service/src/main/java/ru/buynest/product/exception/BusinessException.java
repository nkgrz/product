package ru.buynest.product.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus httpStatus;

    public BusinessException(String message, HttpStatus httpStatus) {
        super(message);
        this.errorCode = "BUSINESS_ERROR";
        this.httpStatus = httpStatus;
    }

    public BusinessException(String message, String errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}