package com.example.cm.cm_web.web;

import java.util.Locale;

import com.example.cm.cm_web.config.annotation.WebController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@WebController
@RequestMapping("/")
public class HomeController {
	private static final Logger logger = LogManager.getLogger();
	
	/*
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Home");
		return "home";
	}	
}
