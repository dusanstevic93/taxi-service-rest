package com.dusan.taxiservice.exception;

public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 2069665508572318760L;
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
