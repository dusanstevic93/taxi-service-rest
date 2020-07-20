package com.dusan.taxiservice.api.rest;

import com.dusan.taxiservice.service.exception.ConflictException;
import com.dusan.taxiservice.service.exception.EmailExistsException;
import com.dusan.taxiservice.service.exception.ResourceNotFoundException;
import com.dusan.taxiservice.service.exception.UsernameExistsException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dusan.taxiservice.dto.response.ExceptionResponse;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse handleException(UsernameExistsException e) {
        return new ExceptionResponse(HttpStatus.CONFLICT, e.getMessage());
    }
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse handleException(EmailExistsException e) {
        return new ExceptionResponse(HttpStatus.CONFLICT, e.getMessage());
    }
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleException(ResourceNotFoundException e) {
        return new ExceptionResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleException(DataIntegrityViolationException e) {
        return new ExceptionResponse(HttpStatus.BAD_REQUEST, e.getRootCause().getMessage());
    }
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleException(HttpMessageNotReadableException e) {
        return new ExceptionResponse(HttpStatus.BAD_REQUEST, "Http message not readable");
    }
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleException(MethodArgumentNotValidException e) {
        return new ExceptionResponse(HttpStatus.BAD_REQUEST, "Required field is invalid or missing");
    }
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse handleException(ConflictException e) {
        return new ExceptionResponse(HttpStatus.CONFLICT, e.getMessage());
    }
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponse handleException(BadCredentialsException e) {
        return new ExceptionResponse(HttpStatus.UNAUTHORIZED, "Bad credentials");
    }
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse handleException(AccessDeniedException e) {
        return new ExceptionResponse(HttpStatus.FORBIDDEN, "Unauthorized access");
    }
}
