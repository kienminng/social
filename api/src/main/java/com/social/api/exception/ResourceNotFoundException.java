package com.social.api.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message) {
        super(message);
    }

    
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
 
}
