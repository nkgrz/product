package ru.buynest.product.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.buynest.product.exception.BusinessException;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleExceptionBusiness(BusinessException e) {
        ErrorResponse errorResponse = ErrorResponse.create(e, e.getHttpStatus(), e.getMessage());
        return new ResponseEntity<>(errorResponse, e.getHttpStatus());
    }

}
