package com.social.api.exception;

import org.springframework.http.HttpStatus;

public class ResourceServeInvalidException extends RuntimeException{
    public ResourceServeInvalidException(String message){
        super(message);
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    
}
