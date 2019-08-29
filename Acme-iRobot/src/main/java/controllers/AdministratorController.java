package controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AdministratorService;
import services.UtilityService;
import domain.Actor;
import domain.Administrator;
import forms.ActorForm;
import forms.ActorRegistrationForm;

@Controller
@RequestMapping("/administrator")
public class AdministratorController extends AbstractController {

	/* Services */

	@Autowired
	private AdministratorService administratorService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private UtilityService utilityService;

	/* Methods */

	/* Display */
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView result = new ModelAndView("administrator/display");

		try {
			Actor actor = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService.checkAuthority(actor, "ADMIN"), "not.allowed");
			
			result.addObject("admin", actor);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}
	
	/* Registration */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView registerNewAdministrator() {
		final ActorRegistrationForm actorRegistrationForm = new ActorRegistrationForm();

		return this.createRegisterModelAndView(actorRegistrationForm);
	}

	/* Save Registration */
	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView register(@Valid ActorRegistrationForm actorRegistrationForm, BindingResult binding) {
		ModelAndView result = new ModelAndView("redirect:../welcome/index.do");
		try {
			Administrator administrator = this.administratorService.reconstruct(actorRegistrationForm, binding);

			if (binding.hasErrors())
				result = this.createRegisterModelAndView(actorRegistrationForm);
			else {
				this.administratorService.save(administrator);
			} 
		} catch (Throwable oops) {
			result = this.createRegisterModelAndView(actorRegistrationForm,oops.getMessage());
		}
		return result;
	}

	/* Edit */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editAdministrator() {
		ModelAndView result;
		try {
			Actor principal = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"), "not.allowed");
			
			final ActorForm actorForm = new ActorForm(principal);
			
			result = this.createEditModelAndView(actorForm);
			
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Save Edit */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(ActorForm actorForm, BindingResult binding) {
		ModelAndView result =  new ModelAndView("redirect:/administrator/display.do");
		
		try {
			Assert.isTrue(this.utilityService.findByPrincipal().getId() == actorForm.getId()
					&& this.actorService.findOne(this.utilityService.findByPrincipal().getId()) != null, "not.allowed");

			Administrator administrator = this.administratorService.reconstruct(actorForm, binding);

			if (binding.hasErrors()) {
				result = this.createEditModelAndView(actorForm);
			} else {
				this.administratorService.save(administrator);
			}
		} catch (Throwable oops) {
			result = this.createEditModelAndView(actorForm, oops.getMessage());
		}
		return result;
	}
	
	/* Delete */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final ActorForm actorForm, final BindingResult binding, final HttpSession session) {
		ModelAndView result = new ModelAndView("redirect:/welcome/index.do");
		
		try {
			Administrator administrator = this.administratorService.findOne(actorForm.getId());

			this.administratorService.delete(administrator);
			session.invalidate();
			
		} catch (Throwable oops) {
			result = this.createEditModelAndView(actorForm, oops.getMessage());
		}
		return result;
	}

	/* Auxiliary methods */

	/* Registration related */
	protected ModelAndView createRegisterModelAndView(final ActorRegistrationForm actorRegistrationForm) {

		return this.createRegisterModelAndView(actorRegistrationForm, null);
	}

	protected ModelAndView createRegisterModelAndView(final ActorRegistrationForm actorRegistrationForm, final String messageCode) {
		ModelAndView result = new ModelAndView("administrator/register");

		actorRegistrationForm.setTermsAndConditions(false);
		result.addObject("actorRegistrationForm", actorRegistrationForm);
		result.addObject("errMsg", messageCode);

		return result;
	}

	/* Edition related */
	protected ModelAndView createEditModelAndView(final ActorForm actorForm) {

		return this.createEditModelAndView(actorForm, null);
	}

	protected ModelAndView createEditModelAndView(final ActorForm actorForm, final String messageCode) {
		ModelAndView result = new ModelAndView("administrator/edit");

		result.addObject("actorForm", actorForm);
		result.addObject("errMsg", messageCode);

		return result;
	}

}