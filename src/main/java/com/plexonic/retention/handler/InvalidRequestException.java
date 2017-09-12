package com.plexonic.retention.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;

@Getter
public class InvalidRequestException extends RuntimeException {
    private Errors errors;
    private HttpStatus httpStatus;

    public InvalidRequestException(String message, Errors errors, HttpStatus httpStatus) {
        super(message);
        this.errors = errors;
        this.httpStatus = httpStatus;
    }

    public InvalidRequestException(String message, Errors errors) {
        this(message, errors, HttpStatus.BAD_REQUEST);
    }

    public InvalidRequestException(String message) {
        this(message, (Errors)null, HttpStatus.BAD_REQUEST);
    }

    public InvalidRequestException(String message, HttpStatus httpStatus) {
        this(message, (Errors)null, httpStatus);
    }
}