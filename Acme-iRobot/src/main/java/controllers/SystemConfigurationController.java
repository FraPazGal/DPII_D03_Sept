package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.SystemConfigurationService;
import services.UtilityService;
import domain.SystemConfiguration;

@Controller
@RequestMapping("/config/admin")
public class SystemConfigurationController extends AbstractController {

	@Autowired
	private SystemConfigurationService sysconfigService;

	@Autowired
	private UtilityService utilityService;

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView result = new ModelAndView("config/display");

		try {
			Assert.isTrue(this.utilityService.checkAuthority(this.utilityService.findByPrincipal(), "ADMIN"), "not.allowed");

			SystemConfiguration config = this.sysconfigService.findMySystemConfiguration();

			result.addObject("config", config);
			result.addObject("welcome", config.getWelcomeMessage());
			result.addObject("breach", config.getBreachNotification());
		} catch (Throwable oops) {
			result.addObject("errMsg", oops.getMessage());
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result = new ModelAndView("config/edit");

		try {
			Assert.isTrue(this.utilityService.checkAuthority(this.utilityService.findByPrincipal(), "ADMIN"), "not.allowed");

			SystemConfiguration config = this.sysconfigService.findMySystemConfiguration();

			result.addObject("systemConfiguration", config);
			result.addObject("welcome", config.getWelcomeMessage());
			result.addObject("breach", config.getBreachNotification());
		} catch (Throwable oops) {
			result.addObject("errMsg", oops.getMessage());
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(SystemConfiguration config,
			@RequestParam(value = "welcomeES") String welcomeES,
			@RequestParam(value = "welcomeEN") String welcomeEN,
			@RequestParam(value = "breachES") String breachES,
			@RequestParam(value = "breachEN") String breachEN,
			BindingResult binding) {
		
		ModelAndView result = new ModelAndView("config/edit");

		try {
			SystemConfiguration reconstructed = this.sysconfigService.reconstruct(config, welcomeES, welcomeEN, breachES, breachEN, binding);

			if (binding.hasErrors()) {

				result.addObject("systemConfiguration", config);
				result.addObject("welcome", reconstructed.getWelcomeMessage());
				result.addObject("breach", reconstructed.getBreachNotification());
			} else {
				this.sysconfigService.save(reconstructed);

				result = new ModelAndView("redirect:display.do");
			}
		} catch (Throwable oops) {
			result.addObject("errMsg", oops.getMessage());
		}
		return result;
	}
}
