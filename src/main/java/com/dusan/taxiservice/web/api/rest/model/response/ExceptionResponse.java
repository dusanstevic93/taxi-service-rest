package com.dusan.taxiservice.web.api.rest.model.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExceptionResponse {

    private LocalDateTime timestamp;
    private int error;
    private String message;

    public ExceptionResponse(HttpStatus status, String message) {
        this.timestamp = LocalDateTime.now();
        this.error = status.value();
        this.message = message;
    }
}
