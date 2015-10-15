package com.example.cm.cm_web.rest;

import com.example.cm.cm_web.config.annotation.RestEndpointAdvice;
import com.example.cm.cm_web.exceptions.*;
import com.example.cm.cm_web.exceptions.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

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

//	@ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseEntity<Error> processValidationError(MethodArgumentNotValidException e) {
//		BindingResult result = e.getBindingResult();
//        List<FieldError> fieldErrors = result.getFieldErrors();
//        List<String> errors = new ArrayList<String>();
//
//        for (FieldError fieldError: fieldErrors) {
//            String localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);
//            errors.add(localizedErrorMessage);
//        }
//		return new Error(400, errors.toString());
//	}
//
//	private String resolveLocalizedErrorMessage(FieldError fieldError) {
//		Locale currentLocale =  LocaleContextHolder.getLocale();
//        String localizedErrorMessage = messageSource.getMessage(fieldError, currentLocale);
//        return localizedErrorMessage;
//    }

}