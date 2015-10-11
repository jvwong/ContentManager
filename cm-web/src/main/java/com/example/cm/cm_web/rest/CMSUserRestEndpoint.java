package com.example.cm.cm_web.rest;

import java.net.URI;
import java.util.List;

import com.example.cm.cm_web.config.annotation.RestEndpoint;
import com.example.cm.cm_repository.repository.CMSUserRepository;
import com.example.cm.cm_model.domain.CMSUser;
import com.example.cm.cm_web.exceptions.GenericNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;


@RestEndpoint
@RequestMapping(value="/rest/user")
public class CMSUserRestEndpoint {
	private static final Logger logger = LoggerFactory.getLogger(CMSUserRestEndpoint.class);

	private CMSUserRepository cmsUserRepository;
	private PasswordEncoder passwordEncoder;

	@Autowired
	public CMSUserRestEndpoint(
			CMSUserRepository CMSUserRepository,
			PasswordEncoder passwordEncoder){
		this.cmsUserRepository = CMSUserRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@RequestMapping(value="", method=RequestMethod.GET)
	public List<CMSUser> cmsUserList(){
		return cmsUserRepository.findAll();
	}

	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public CMSUser cmsUserDetail(
	      @PathVariable("id") Long id) {

		CMSUser cmsUser = cmsUserRepository.findOne(id);
		if(cmsUser == null) throw new GenericNotFoundException(id, CMSUser.class.getSimpleName());
		return cmsUser;
	}

	@RequestMapping(
			value="",
			method=RequestMethod.POST,
			consumes="application/json")
	public ResponseEntity<CMSUser> saveCMSUser(
			@RequestBody CMSUser cmsUser,
			UriComponentsBuilder ucb){

		logger.info("Saving CMSUSer: {}", cmsUser.toString());

		try{

			HttpHeaders headers = new HttpHeaders();

			// NullPointerException
			cmsUser.setPassword(passwordEncoder.encode(cmsUser.getPassword()));

			// DataIntegrityViolationException
			CMSUser CMSUserSaved = cmsUserRepository.save(cmsUser);

			URI locationUri =
					ucb.path("/services/rest/user/")
					   .path(String.valueOf(CMSUserSaved.getId()))
					   .build()
					   .toUri();
			headers.setLocation(locationUri);
			return  new ResponseEntity<>(CMSUserSaved, headers, HttpStatus.CREATED);

		} catch (NullPointerException npe) {
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

		} catch (DataIntegrityViolationException dee) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}
}
