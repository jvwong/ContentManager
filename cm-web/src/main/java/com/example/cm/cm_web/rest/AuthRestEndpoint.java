package com.example.cm.cm_web.rest;

import com.example.cm.cm_model.domain.CMSUser;
import com.example.cm.cm_repository.service.CMSUserService;
import com.example.cm.cm_web.config.annotation.RestEndpoint;
import com.example.cm.cm_web.exceptions.MissingCredentialsException;
import com.example.cm.cm_web.exceptions.MissingEntityException;
import com.example.cm.cm_web.security.TokenAuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;

@RestEndpoint
@RequestMapping(value="/rest/auth")
public class AuthRestEndpoint {

	private static final Logger logger
			= LoggerFactory.getLogger(AuthRestEndpoint.class);

	private TokenAuthenticationService tokenAuthenticationService;
	private AuthenticationManager authenticationManager;
	private CMSUserService cmsUserService;

	@Autowired
	public AuthRestEndpoint(
			TokenAuthenticationService tokenAuthenticationService,
			CMSUserService cmsUserService,
			AuthenticationManager authenticationManager){
		this.cmsUserService = cmsUserService;
		this.tokenAuthenticationService = tokenAuthenticationService;
		this.authenticationManager = authenticationManager;
	}

	@RequestMapping(
			value="/",
			method=RequestMethod.OPTIONS
	)
	public ResponseEntity<String> getOptions(HttpServletResponse httpResponse){
		httpResponse.setHeader("Allow", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(
		value="/",
		method=RequestMethod.POST
	)
	public CMSUser authenticate(@RequestBody CMSUser cmsUser,
			HttpServletResponse  httpResponse){

		logger.debug("cmsUser");
		logger.debug(cmsUser.toString());

		// Throws NullPointerException
		if(cmsUser.getUsername() == null){
			throw new MissingCredentialsException("username",  CMSUser.class.getSimpleName());
		}

		if(cmsUser.getPassword() == null){
			throw new MissingCredentialsException("password",  CMSUser.class.getSimpleName());
		}

		Authentication authRequest
				= new UsernamePasswordAuthenticationToken(cmsUser.getUsername(), cmsUser.getPassword());

		// Note: Implicitly this process throws a BadCredentialsException if
		// authentication fails, which is caught within our RestExceptionHandler.
		authenticationManager.authenticate(authRequest);

		// If it succeeds only then will the addAuthentication proceed.
		CMSUser saved = cmsUserService.getUser(cmsUser.getUsername());
		tokenAuthenticationService.addAuthentication(httpResponse, saved);

		if(saved == null) throw new MissingEntityException(CMSUser.class.getName());

		return saved;
	}
}



