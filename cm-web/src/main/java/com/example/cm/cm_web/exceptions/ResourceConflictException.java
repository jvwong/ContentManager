package com.example.cm.cm_web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author jvwong
 */
@ResponseStatus(value= HttpStatus.CONFLICT, reason="Resource conflict")
public class ResourceConflictException extends RuntimeException {
    private final String className;

    public ResourceConflictException(String className){
        this.className = className;
    }

    public String getClassName(){
        return className;
    }
}

