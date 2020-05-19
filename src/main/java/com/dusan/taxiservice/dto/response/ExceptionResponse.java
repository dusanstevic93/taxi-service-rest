package com.dusan.taxiservice.dto.response;

import java.time.Instant;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionResponse {
    
    private Instant timestamp;
    private int status;
    private String error;
    private String message;
    
    public ExceptionResponse(HttpStatus status, String message) {
        timestamp = Instant.now();
        this.status = status.value();
        this.error = status.name();
        this.message = message;
    }
}
