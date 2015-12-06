package com.example.cm.cm_web.rest;

import com.example.cm.cm_model.domain.CMSUser;
import com.example.cm.cm_model.domain.JsonPatch;
import com.example.cm.cm_repository.service.CMSUserService;
import com.example.cm.cm_web.config.annotation.RestEndpoint;
import com.example.cm.cm_web.exceptions.ResourceConflictException;
import com.example.cm.cm_web.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;


@RestEndpoint
@RequestMapping(value="/rest/users")
public class CMSUserRestEndpoint {
	private CMSUserService cmsUserService;
	private PasswordEncoder passwordEncoder;

	@Autowired
	public CMSUserRestEndpoint(
			CMSUserService cmsUserService,
			PasswordEncoder passwordEncoder){
		this.cmsUserService = cmsUserService;
		this.passwordEncoder = passwordEncoder;
	}

	@RequestMapping(
			value="/",
			method=RequestMethod.OPTIONS
	)
	public ResponseEntity<String> getOptions(HttpServletResponse httpResponse){
		httpResponse.setHeader("Allow", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	/**
	 * Retrieve a paged list of User instances
	 * @return
	 */
	@RequestMapping(value="/", method=RequestMethod.GET)
	public Page<CMSUser> cmsUserList(
			@RequestParam(value="page", defaultValue="1") Integer pageNumber,
			@RequestParam(value="size", defaultValue="10") Integer pageSize
	){
		return cmsUserService.cmsUserList(pageNumber, pageSize);
	}

	/**
	 * Retrieve a particular User instance
	 * @param username the unique username
	 * @return
	 */
	@RequestMapping(value="/{username}/", method=RequestMethod.GET)
	public CMSUser cmsUserDetail(
	      @PathVariable("username") String username) {

		CMSUser cmsUser = cmsUserService.cmsUser(username);
		if(cmsUser == null) throw new ResourceNotFoundException(CMSUser.class.getSimpleName());
		return cmsUser;
	}

	/**
	 * Create a User instance
	 * @param cmsUser The user instance to create
	 * @param ucb The uri component builder to return
	 * @return ResponseEntity<CMSUser>
	 */
	@RequestMapping(
			value="/",
			method=RequestMethod.POST
	)
	public ResponseEntity<CMSUser> saveCMSUser(
			@Valid @RequestBody CMSUser cmsUser,
			Errors errors,
			UriComponentsBuilder ucb){

		if(errors.hasErrors()){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		try{

			HttpHeaders headers = new HttpHeaders();

			// NullPointerException
			cmsUser.setPassword(passwordEncoder.encode(cmsUser.getPassword()));

			// DataIntegrityViolationException
			CMSUser CMSUserSaved = cmsUserService.save(cmsUser);

			URI locationUri =
					ucb.path("/services/rest/users/")
					   .path(String.valueOf(CMSUserSaved.getId()))
					   .build()
					   .toUri();
			headers.setLocation(locationUri);
			return new ResponseEntity<>(CMSUserSaved, headers, HttpStatus.CREATED);

		} catch (NullPointerException npe) {
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

		} catch (DataIntegrityViolationException dee) {
			throw new ResourceConflictException(CMSUser.class.toString());
		}
	}

	/**
	 * Delete an existing {@link CMSUser} instance
	 */
	@RequestMapping(
			value="/{username}/",
			method=RequestMethod.DELETE)
	public ResponseEntity<String> deleteCMSUser(
			@PathVariable("username") String username)
	{
		Boolean exists = cmsUserService.exists(username);

		if(!exists) {
			//404 Not Found
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// delete
		cmsUserService.delete(username);

		//204 No Content
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);

	}


	/**
	 * Update an existing {@link CMSUser} instance using the
	 * protocol laid out in (http://jsonpatch.com/)
	 */
	@RequestMapping(
			value="/{username}/",
			method=RequestMethod.PATCH)
	public ResponseEntity<CMSUser> updateCMSUser(
			@PathVariable("username") String username,
			@RequestBody List<JsonPatch> patches)
	{

		Boolean exists = cmsUserService.exists(username);

		if(!exists) {
			//404 Not Found
			throw new ResourceNotFoundException(CMSUser.class.getName());
		}

		// partial update
		CMSUser updated = cmsUserService.update(username, patches);

		//204 No Content
		return new ResponseEntity<>(updated, HttpStatus.OK);
	}



}
