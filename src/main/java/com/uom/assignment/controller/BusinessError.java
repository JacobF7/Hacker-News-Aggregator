package com.uom.assignment.controller;

import org.springframework.http.HttpStatus;

/**
 * An enumeration for all application errors.
 *
 * Created by jacobfalzon on 06/05/2017.
 */
public enum BusinessError {

    INVALID_USER("Invalid User", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS("Invalid Username or Password", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN("Invalid or Expired Token", HttpStatus.UNAUTHORIZED);

    private final String errorMessage;
    private final HttpStatus statusCode;

    BusinessError(final String errorMessage, final HttpStatus statusCode) {
        this.errorMessage = errorMessage;
        this.statusCode = statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
