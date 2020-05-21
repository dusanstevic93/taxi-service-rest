package com.dusan.taxiservice.exception;

public class ConflictException extends RuntimeException {

    private static final long serialVersionUID = 3168232433764848070L;
    
    public ConflictException(String message) {
        super(message);
    }
}
