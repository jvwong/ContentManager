package com.example.cm.cm_web.rest;

import com.example.cm.cm_web.config.annotation.RestEndpointAdvice;
import com.example.cm.cm_web.exceptions.Error;
import com.example.cm.cm_web.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.access.AccessDeniedException;

@RestEndpointAdvice
public class RestExceptionHandler {

	private MessageSource messageSource;

    @Autowired
    public RestExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

	@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Error> resourceNotFound(ResourceNotFoundException e) {
		Error error = new Error(null, "Could not identify resource");
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ResourceConflictException.class)
	public ResponseEntity<Error> resourceConflict(ResourceConflictException e) {
		Error error = new Error(null, "Resource conflict: " + e.getClassName());
		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Error> badCredentials(BadCredentialsException e) {
		Error error = new Error(e.getMessage(), e.getMessage());
		return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(MissingEntityException.class)
    public ResponseEntity<Error> missingEntity(MissingEntityException e) {
		Error error = new Error(e.getMessage(), e.getMessage());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MissingCredentialsException.class)
    public ResponseEntity<Error> missingCrendentials(MissingCredentialsException e) {
		Error error = new Error(e.getMessage(), e.getMessage());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Error> accessDenied(AccessDeniedException e) {
		Error error = new Error(e.getMessage(), e.getMessage());
		return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
	}

}