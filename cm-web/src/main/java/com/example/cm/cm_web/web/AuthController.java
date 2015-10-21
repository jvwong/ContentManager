package com.example.cm.cm_web.web;

import com.example.cm.cm_model.domain.CMSUser;
import com.example.cm.cm_repository.repository.CMSUserRepository;
import com.example.cm.cm_web.config.annotation.WebController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Locale;

/**
 * Handles requests for the application home page.
 */
@WebController
@RequestMapping(value = "/auth")
public class AuthController {	

	private PasswordEncoder passwordEncoder;	
	private CMSUserRepository cmsUserRepository;

	@Autowired
	public AuthController(CMSUserRepository cmsUserRepository,
			PasswordEncoder passwordEncoder) {
	    this.cmsUserRepository = cmsUserRepository;
	    this.passwordEncoder = passwordEncoder;
	}
	
	/*
	 * Show the login page 
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Locale locale, Model model) {
		return "/auth/login";
	}
	
	/*
	 * Show the registration page 
	 */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String showRegistrationForm(Locale locale, Model model) {		
		model.addAttribute("registerForm", new RegisterForm());
		return "/auth/registerForm";
	}
	
	
	/*
	 * Process the registration - forget about the avatar for now
	 * */
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String processRegistration(
			@RequestPart("profilePicture") MultipartFile profilePicture,
			RedirectAttributes model, //Must precede validate
		    @Valid RegisterForm registerForm,		    
		    Errors errors) {
		
		if (errors.hasErrors()) {
			return "/auth/registerForm";
		}
			
		CMSUser cmsUser = registerForm.toCMSUser();
		cmsUser.setPassword(passwordEncoder.encode(cmsUser.getPassword()));
		cmsUser.setRole("ROLE_CMSUSER");
		
		try{
			cmsUserRepository.save(cmsUser);
			
			model.addAttribute("username", cmsUser.getUsername());
			model.addFlashAttribute("cmsUser", cmsUser);

			// When InternalResourceViewResolver sees the "redirect: " prefix
			// on the view specification, it will redirect 
			return "redirect:/auth/profile";
			
		} catch (DataIntegrityViolationException dee) {			
			String message = "Error: " + dee.getMostSpecificCause().getMessage();
			
			model.addFlashAttribute("message", message);
			return "redirect:/auth/register";
		}	
	}
	
	@RequestMapping(value="/profile", method=RequestMethod.GET)
	public String showSpitterProfile(Model model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String uname = auth.getName(); //get logged in username

		if(!model.containsAttribute("username")){
			CMSUser user = cmsUserRepository.findByUsername(uname);
		    model.addAttribute("user", user);
		}
		
		return "/auth/profile";	 
	}
}
