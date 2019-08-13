/*
 * AbstractController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import services.SystemConfigurationService;

@Controller
public class AbstractController {
	
	@Autowired
	private SystemConfigurationService	systemConfigurationService;

	// Methods

	@ModelAttribute("banner")
	public String getBanner(final Model model) {

		final String urlBanner = this.systemConfigurationService.findMySystemConfiguration().getBanner();
		return urlBanner;
	}
	
//	@ModelAttribute("breachNotification")
//	public Map<String, String> getBreachNotification(final Model model) {
//		final Map<String, String> res = this.systemConfigurationService.findBreachNotification();
//
//		return res;
//	}
	

	// Panic handler ----------------------------------------------------------

	@ExceptionHandler(Throwable.class)
	public ModelAndView panic(final Throwable oops) {
		ModelAndView result = new ModelAndView("redirect:/welcome/index.do");

//		result = new ModelAndView("misc/panic");
//		result.addObject("name", ClassUtils.getShortName(oops.getClass()));
//		result.addObject("exception", oops.getMessage());
//		result.addObject("stackTrace", ExceptionUtils.getStackTrace(oops));

		return result;
	}

}
