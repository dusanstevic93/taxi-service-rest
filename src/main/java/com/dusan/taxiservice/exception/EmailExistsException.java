package com.dusan.taxiservice.exception;

public class EmailExistsException extends RuntimeException {

    private static final long serialVersionUID = -1929795374263770040L;
    
    public EmailExistsException(String message) {
        super(message);
    }

}
