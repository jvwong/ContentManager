package com.example.cm.cm_web.rest;

import com.example.cm.cm_model.domain.CMSUser;
import com.example.cm.cm_model.domain.JsonPatch;
import com.example.cm.cm_repository.service.CMSUserService;
import com.example.cm.cm_web.config.annotation.RestEndpoint;
import com.example.cm.cm_web.exceptions.ResourceConflictException;
import com.example.cm.cm_web.exceptions.ResourceNotFoundException;
import com.example.cm.cm_web.exceptions.UnprocessableEntityException;
import com.example.cm.cm_web.form.CMSUserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;


@RestEndpoint
@RequestMapping(value="/rest/users")
public class CMSUserRestEndpoint {
	private static final Logger logger
			= LoggerFactory.getLogger(CMSUserRestEndpoint.class);

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
	 * @param cmsUserForm The user instance to create
	 * @param ucb The uri component builder to return
	 * @return ResponseEntity<CMSUser>
	 */
	@RequestMapping(
			value="/",
			method=RequestMethod.POST
	)
	public ResponseEntity<CMSUser> saveCMSUser(
			@Valid CMSUserForm cmsUserForm,
			Errors errors,
			UriComponentsBuilder ucb){

		if(errors.hasErrors()){
			logger.error("CMSUserForm Errors");
			for(FieldError error: errors.getFieldErrors())
			{
				logger.error(error.getField());
				logger.error(error.getDefaultMessage());
			}

			throw new UnprocessableEntityException(
					errors.getFieldErrors().toString(),
					CMSUserForm.class.getName());
		}

		try{

			HttpHeaders headers = new HttpHeaders();

			// NullPointerException
			CMSUser cmsUser = cmsUserForm.toCMSUser();
			cmsUser.setPassword(passwordEncoder.encode(cmsUserForm.getPassword()));

			// DataIntegrityViolationException
			CMSUser CMSUserSaved = cmsUserService.save(cmsUser);

			if(cmsUserForm.getImage() != null){
//				logger.info("name: " + cmsUserForm.getImage().getName());
//				logger.info("Original name: " + cmsUserForm.getImage().getOriginalFilename());
//				logger.info("size: " + String.valueOf(cmsUserForm.getImage().getSize()));

				// Do some type checking
				if( !cmsUserForm.getImage().getContentType()
						.equalsIgnoreCase(MediaType.IMAGE_JPEG_VALUE) &&
					!cmsUserForm.getImage().getContentType()
							.equalsIgnoreCase(MediaType.IMAGE_PNG_VALUE))
				{
					throw new UnprocessableEntityException(
							"Invalid file type",
							CMSUserForm.class.getName());
				}

				String destination
						= "/home/jeffrey/Projects/ContentManager/uploads/" +
						CMSUserSaved.getUsername() + "/" +
						cmsUserForm.getImage().getOriginalFilename();
				File file = new File(destination);
				boolean exists = file.getParentFile().mkdir();
				if(exists)
				{
					cmsUserForm
							.getImage()
							.transferTo(file);
				}
			}

			URI locationUri =
					ucb.path("/services/rest/users/")
							.path(String.valueOf(CMSUserSaved.getId()))
							.build()
							.toUri();
			headers.setLocation(locationUri);
			return new ResponseEntity<>(CMSUserSaved, headers, HttpStatus.CREATED);

		} catch (NullPointerException npe) {
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

		} catch (IOException ioe) {
			logger.error("IOE: ", ioe);
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
