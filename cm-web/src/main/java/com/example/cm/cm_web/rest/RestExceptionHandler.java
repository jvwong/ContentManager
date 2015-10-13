package com.example.cm.cm_web.rest;

import com.example.cm.cm_web.config.annotation.RestEndpointAdvice;
import com.example.cm.cm_web.exceptions.Error;
import com.example.cm.cm_web.exceptions.ResourceNotFoundException;
import com.example.cm.cm_web.exceptions.MissingCredentialsException;
import com.example.cm.cm_web.exceptions.MissingEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestEndpointAdvice
public class RestExceptionHandler {

	private MessageSource messageSource;

    @Autowired
    public RestExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
    public Error resourceNotFound(ResourceNotFoundException e) {
		return new Error(e.getId(), "Resource could not be located");
	}

	@ExceptionHandler(BadCredentialsException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Error badCredentials(BadCredentialsException e) {
		return new Error(401, e.getMessage());
	}

	@ExceptionHandler(MissingEntityException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
    public Error missingEntity(MissingEntityException e) {
		return new Error(400, "Bad/missing " + e.getClassName());
	}

	@ExceptionHandler(MissingCredentialsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error missingCrendentials(MissingCredentialsException e) {
		return new Error(400, e.getClassName() + " missing credential [" + e.getMessage() + "]");
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error processValidationError(MethodArgumentNotValidException e) {
		BindingResult result = e.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        List<String> errors = new ArrayList<String>();

        for (FieldError fieldError: fieldErrors) {
            String localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);
            errors.add(localizedErrorMessage);
        }
		return new Error(400, errors.toString());
	}

	private String resolveLocalizedErrorMessage(FieldError fieldError) {
		Locale currentLocale =  LocaleContextHolder.getLocale();
        String localizedErrorMessage = messageSource.getMessage(fieldError, currentLocale);
        return localizedErrorMessage;
    }

}