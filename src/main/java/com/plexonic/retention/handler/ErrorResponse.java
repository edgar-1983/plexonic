package com.plexonic.retention.handler;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;


@Getter
public class ErrorResponse {
    private HttpStatus code;
    private String message;
    @Setter
    private List<FieldErrorResource> fieldErrors;

    public ErrorResponse(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return String.format("%s %s", code.value(), code.getReasonPhrase());
    }
}
