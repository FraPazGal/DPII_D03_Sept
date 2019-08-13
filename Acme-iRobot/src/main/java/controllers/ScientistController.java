package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.IRobotService;
import services.ScientistService;
import services.SystemConfigurationService;
import services.UtilityService;
import domain.Actor;
import domain.Scientist;
import domain.User;
import forms.UserForm;
import forms.UserRegistrationForm;

@Controller
@RequestMapping("/scientist")
public class ScientistController extends AbstractController {

	/* Services */

	@Autowired
	private ScientistService scientistService;

	@Autowired
	private ActorService actorService;
	
	@Autowired
	private IRobotService iRobotService;

	@Autowired
	private UtilityService utilityService;
	
	@Autowired
	private SystemConfigurationService systemConfigurationService;

	/* Display */
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam(required = false) final Integer id) {
		ModelAndView result = new ModelAndView("scientist/display");
		User scientist;
		boolean isPrincipal = false;

		try {
			if (id != null) {
				scientist = (User) this.actorService.findOne(id);
			} else {
				scientist =  this.utilityService.findUserByPrincipal();
				isPrincipal = true;
			}
			Assert.isTrue(this.utilityService.checkAuthority(scientist, "SCIENTIST"), "not.allowed");
			result.addObject("scientist", scientist);
			result.addObject("iRobots", this.iRobotService.findIRobotsNotDecomissionedAndMine(scientist.getId()));
			result.addObject("isPrincipal", isPrincipal);
		} catch (final Throwable oops) {
			result.addObject("errMsg", oops.getMessage());
		}

		return result;
	}
	
	/* Listing */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result = new ModelAndView("scientist/list");
		Collection<Scientist> scientists = new ArrayList<>();

		try {
			scientists = this.scientistService.findAll();
			
			result.addObject("scientists", scientists);

		} catch (final Throwable oops) {
			result.addObject("errMsg", oops.getMessage());
		}
		return result;
	}

	/* Registration */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView registerNewAuthor() {
		final UserRegistrationForm userRegistrationForm = new UserRegistrationForm();

		return this.createRegisterModelAndView(userRegistrationForm);
	}

	/* Save Registration */
	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView register(@Valid final UserRegistrationForm userRegistrationForm, final BindingResult binding) {
		ModelAndView result = new ModelAndView("redirect:/");
		
		try {
			Scientist scientist = this.scientistService.reconstruct(userRegistrationForm, binding);
	
			if (binding.hasErrors())
				result = this.createRegisterModelAndView(userRegistrationForm);
			else  {
				this.scientistService.save(scientist);
			}
		} catch (final Throwable oops) {
			result = this.createRegisterModelAndView(userRegistrationForm, oops.getMessage());
		}
		return result;
	}

	/* Edit */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editAuthor() {
		ModelAndView result = new ModelAndView("scientist/display");
		try {
			Actor principal = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService.checkAuthority(principal, "SCIENTIST"), "not.allowed");
			
			final UserForm userForm = new UserForm((User)principal);
			
			result = this.createEditModelAndView(userForm);
		} catch (Throwable oops) {
			result.addObject("errMsg", oops.getMessage());
		}
		return result;
	}

	/* Save Edit */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@Valid final UserForm userForm, final BindingResult binding) {
		ModelAndView result = new ModelAndView("redirect:/scientist/display.do");

		try {
			Assert.isTrue(this.utilityService.findByPrincipal().getId() == userForm.getId()
					&& this.actorService.findOne(this.utilityService.findByPrincipal().getId()) != null, "not.allowed");

			Scientist scientist = this.scientistService.reconstruct(userForm, binding);

			if (binding.hasErrors()) {
				result = this.createEditModelAndView(userForm);
			} else {
				this.scientistService.save(scientist);
			}
		} catch (Throwable oops) {
			result = this.createEditModelAndView(userForm, oops.getMessage());
		}
		return result;
	}
	
	/* Delete */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView deleteRookie(final UserForm userForm, final BindingResult binding, final HttpSession session) {
		
		ModelAndView result = new ModelAndView("redirect:/welcome/index.do");
		Scientist scientist = this.scientistService.findOne(userForm.getId());

		if (binding.hasErrors())
			result = this.createEditModelAndView(userForm);
		else
			try {
				this.scientistService.delete(scientist);
				session.invalidate();
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(userForm, oops.getMessage());
			}
		return result;
	}

	/* Auxiliary methods */
	protected ModelAndView createRegisterModelAndView(final UserRegistrationForm userRegistrationForm) {

		return this.createRegisterModelAndView(userRegistrationForm, null);
	}

	protected ModelAndView createRegisterModelAndView(final UserRegistrationForm userRegistrationForm,
			final String messageCode) {
		ModelAndView result = new ModelAndView("scientist/register");

		userRegistrationForm.setTermsAndConditions(false);
		result.addObject("userRegistrationForm", userRegistrationForm);
		result.addObject("errMsg", messageCode);
		String[] aux = this.systemConfigurationService.findMySystemConfiguration().getMakers().split(",");
		result.addObject("makers", Arrays.asList(aux));

		return result;
	}

	protected ModelAndView createEditModelAndView(final UserForm userForm) {

		return this.createEditModelAndView(userForm, null);
	}

	protected ModelAndView createEditModelAndView(
			final UserForm userForm, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("scientist/edit");
		result.addObject("userForm", userForm);
		result.addObject("errMsg", messageCode);
		String[] aux = this.systemConfigurationService.findMySystemConfiguration().getMakers().split(",");
		result.addObject("makers", Arrays.asList(aux));

		return result;
	}

}