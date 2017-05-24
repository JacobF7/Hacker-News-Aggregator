package com.uom.assignment.controller;

import org.springframework.http.HttpStatus;

/**
 * A custom {@link RuntimeException} that is thrown for any application error.
 *
 * Created by jacobfalzon on 06/05/2017.
 */
public class BusinessErrorException extends RuntimeException {

    private final HttpStatus statusCode;

    public BusinessErrorException(final String errorMessage, final HttpStatus statusCode) {
        super(errorMessage);
        this.statusCode = statusCode;
    }

    public BusinessErrorException(final BusinessError businessError) {
        super(businessError.getErrorMessage());
        this.statusCode = businessError.getStatusCode();
    }

    HttpStatus getStatusCode() {
        return statusCode;
    }
}
