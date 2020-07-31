package com.dusan.taxiservice.web.api.websocket.controller;

import com.dusan.taxiservice.core.service.exception.ConflictException;
import com.dusan.taxiservice.core.service.exception.ResourceNotFoundException;
import com.dusan.taxiservice.web.api.websocket.model.ExceptionMessage;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;

import static com.dusan.taxiservice.web.api.websocket.SocketDestinations.*;

@ControllerAdvice
@AllArgsConstructor
@SendToUser(QUEUE_PREFIX + ERROR)
public class WebSocketGlobalExceptionHandler {

    @MessageExceptionHandler
    public ExceptionMessage handleException(ResourceNotFoundException e) {
        return new ExceptionMessage(e.getMessage());
    }

    @MessageExceptionHandler
    public ExceptionMessage handleException(MethodArgumentNotValidException e) {
        return new ExceptionMessage(e.getMessage());
    }

    @MessageExceptionHandler
    public ExceptionMessage handleException(ConflictException e) {
        return new ExceptionMessage(e.getMessage());
    }
}
