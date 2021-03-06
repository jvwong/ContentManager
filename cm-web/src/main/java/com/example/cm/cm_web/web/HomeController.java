package com.example.cm.cm_web.web;

import com.example.cm.cm_web.config.annotation.WebController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Locale;

/**
 * Handles requests for the application home page.
 */
@WebController
@RequestMapping("/")
public class HomeController {
	private static final Logger logger =
			LoggerFactory.getLogger(HomeController.class);
	/*
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		//logger.debug("Locale: {}-{}", locale.getDisplayCountry(), locale.getDisplayLanguage());
		return "home";
	}	
}
