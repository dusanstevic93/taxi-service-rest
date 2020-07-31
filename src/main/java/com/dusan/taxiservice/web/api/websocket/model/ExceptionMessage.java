package com.dusan.taxiservice.web.api.websocket.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExceptionMessage {

    private LocalDateTime timestamp;
    private String message;

    public ExceptionMessage(String message) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
    }
}
