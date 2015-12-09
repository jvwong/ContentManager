package com.example.cm.cm_web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNPROCESSABLE_ENTITY, reason="Unprocessable Entity")
public class UnprocessableEntityException extends RuntimeException {
    private String message;
    private String className;

    public UnprocessableEntityException (String message, String className){
        this.message = message;
        this.className = className;
    }

    public String getMessage(){
        return message;
    }

    public String getClassName(){
        return className;
    }
}
