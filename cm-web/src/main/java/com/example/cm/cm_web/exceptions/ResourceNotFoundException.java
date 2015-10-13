package com.example.cm.cm_web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Not Found")
public class ResourceNotFoundException extends RuntimeException {
	private final long id;
	private final String className;
	
	public ResourceNotFoundException(long id, String className){
		this.id = id;
		this.className = className;
	}
	
	public long getId(){
		return id;
	}
	
	public String getClassName(){
		return className;
	}
}
