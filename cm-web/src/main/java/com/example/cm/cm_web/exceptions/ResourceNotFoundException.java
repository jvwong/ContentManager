package com.example.cm.cm_web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Not Found")
public class ResourceNotFoundException extends RuntimeException {
	private final String className;
	
	public ResourceNotFoundException(String className){
		this.className = className;
	}

	public String getClassName(){
		return className;
	}
}
