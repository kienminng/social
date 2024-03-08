package com.social.api.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends CustomException{

    public ResourceNotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
 
}
