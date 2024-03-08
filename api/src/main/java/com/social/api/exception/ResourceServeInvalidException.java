package com.social.api.exception;

import org.springframework.http.HttpStatus;

public class ResourceServeInvalidException extends CustomException{
    public ResourceServeInvalidException(String message){
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    
}
