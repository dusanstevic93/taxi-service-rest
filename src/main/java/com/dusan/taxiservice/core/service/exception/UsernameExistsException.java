package com.dusan.taxiservice.core.service.exception;

public class UsernameExistsException extends RuntimeException {

    private static final long serialVersionUID = 9094083946042350988L;
    
    public UsernameExistsException(String message) {
        super(message);
    }
}
