package com.plexonic.retention.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleJsonMapping(JsonMappingException ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getOriginalMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleJsonParse(JsonParseException ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getOriginalMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgument(IllegalArgumentException ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public ErrorResponse handleIllegalState(IllegalStateException ex) {
        return new ErrorResponse(HttpStatus.PRECONDITION_FAILED, ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoSuchElement(NoSuchElementException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({InvalidRequestException.class})
    public ResponseEntity<Object> handleInvalidRequest(RuntimeException e, WebRequest request) {
        InvalidRequestException ire = (InvalidRequestException) e;
        List<FieldErrorResource> fieldErrorResources = new ArrayList<>();
        if (ire.getErrors() != null) {
            List<FieldError> fieldErrors = ire.getErrors().getFieldErrors();
            fieldErrorResources.addAll(fieldErrors.stream().map((fieldError) -> {
                return new FieldErrorResource(fieldError.getObjectName(), fieldError.getField(), fieldError.getCode(),
                        fieldError.getDefaultMessage());
            }).collect(Collectors.toList()));
        }

        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, ire.getMessage());
        error.setFieldErrors(fieldErrorResources);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return this.handleExceptionInternal(e, error, headers, ire.getHttpStatus(), request);
    }

    private ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                           HttpStatus status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute("javax.servlet.error.exception", ex, 0);
        }

        return new ResponseEntity<>(body, headers, status);
    }
}
