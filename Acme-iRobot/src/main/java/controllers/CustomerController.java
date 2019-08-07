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
import services.CustomerService;
import services.FinderService;
import services.UtilityService;
import domain.Actor;
import domain.Customer;
import domain.User;
import forms.UserForm;
import forms.UserRegistrationForm;

@Controller
@RequestMapping("/customer")
public class CustomerController extends AbstractController {

	/* Services */

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private UtilityService utilityService;
	
	@Autowired
	private FinderService finderService;

	/* Display */
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView result = new ModelAndView("customer/display");

		try {
			Customer customer = (Customer) this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService.checkAuthority(customer, "CUSTOMER"), "not.allowed");
			
			result.addObject("customer", customer);
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
			Customer customer = this.customerService.reconstruct(userRegistrationForm, binding);
	
			if (binding.hasErrors())
				result = this.createRegisterModelAndView(userRegistrationForm);
			else  {
				Customer saved = this.customerService.save(customer);
				
				this.finderService.createForCustomer(saved);
			}
		} catch (final Throwable oops) {
			result = this.createRegisterModelAndView(userRegistrationForm, oops.getMessage());
		}
		return result;
	}

	/* Edit */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editAuthor() {
		ModelAndView result = new ModelAndView("customer/display");;
		try {
			Actor principal = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService.checkAuthority(principal, "CUSTOMER"), "not.allowed");
			
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
		ModelAndView result = new ModelAndView("redirect:/customer/display.do");

		try {
			Assert.isTrue(this.utilityService.findByPrincipal().getId() == userForm.getId()
					&& this.actorService.findOne(this.utilityService.findByPrincipal().getId()) != null);

			Customer customer = this.customerService.reconstruct(userForm, binding);

			if (binding.hasErrors()) {
				result = this.createEditModelAndView(userForm);
			} else {
				this.customerService.save(customer);
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
		Customer customer = this.customerService.findOne(userForm.getId());

		if (binding.hasErrors())
			result = this.createEditModelAndView(userForm);
		else
			try {
				this.customerService.delete(customer);
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
		ModelAndView result = new ModelAndView("customer/register");

		result.addObject("userRegistrationForm", userRegistrationForm);
		result.addObject("errMsg", messageCode);

		return result;
	}

	protected ModelAndView createEditModelAndView(final UserForm userForm) {

		return this.createEditModelAndView(userForm, null);
	}

	protected ModelAndView createEditModelAndView(
			final UserForm userForm, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("customer/edit");
		result.addObject("userForm", userForm);
		result.addObject("errMsg", messageCode);

		return result;
	}

}