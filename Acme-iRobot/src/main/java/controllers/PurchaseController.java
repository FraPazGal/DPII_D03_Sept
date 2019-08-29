
package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.IRobotService;
import services.PurchaseService;
import services.SystemConfigurationService;
import services.UtilityService;
import domain.Actor;
import domain.Customer;
import domain.IRobot;
import domain.Purchase;
import forms.PurchaseForm;

@Controller
@RequestMapping("/purchase")
public class PurchaseController extends AbstractController {

	/* Services */
	
	@Autowired
	private UtilityService	utilityService;

	@Autowired
	private PurchaseService		purchaseService;
	
	@Autowired
	private IRobotService		iRobotService;
	
	@Autowired
	private SystemConfigurationService systemConfigurationService;

	/* Display */
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int purchaseId) {
		ModelAndView result = new ModelAndView("purchase/display");

		try {
			Purchase purchase = this.purchaseService.findOne(purchaseId);
			Actor principal = this.utilityService.findByPrincipal();
			
			Assert.isTrue(purchase.getCustomer().equals((Customer) principal), "not.allowed");
			
			result.addObject("purchase", purchase);
			
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Listing */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final Integer iRobotId) {
		ModelAndView result = new ModelAndView("purchase/list");
		Collection<Purchase> purchases = new ArrayList<>();

		try {
			Actor principal = this.utilityService.findByPrincipal();
			if (this.utilityService.checkAuthority(principal, "CUSTOMER")) {
				purchases = this.purchaseService.purchasesPerCustomer(principal.getId());
			} else if (this.utilityService.checkAuthority(principal, "SCIENTIST") && iRobotId != null) {
				purchases = this.purchaseService.purchasesPerIRobot(iRobotId);
			} else {
				Assert.notEmpty(purchases);
			}
			result.addObject("purchases", purchases);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Create */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createPurchase(@RequestParam final int iRobotId, @RequestParam (required = false) final String CC) {
		ModelAndView result;
		boolean visible = false;
		
		try {
			PurchaseForm purchaseForm = new PurchaseForm();
			IRobot iRobot = this.iRobotService.findOne(iRobotId);
			
			Assert.isTrue(!iRobot.getIsDeleted() && !iRobot.getIsDecommissioned(), "wrong.irobot.id");
			purchaseForm.setIRobot(iRobot);

			result = this.createEditModelAndView(purchaseForm);
			
			if(CC != null) {
				visible = true;
			}
			result.addObject("visible", visible);
			
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}
	
	/* Edit */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int purchaseId) {
		ModelAndView result;
		
		try {
			Actor principal = this.utilityService.findByPrincipal();
			Purchase purchase = this.purchaseService.findOne(purchaseId);
			
			Assert.isTrue(purchase.getCustomer().equals((Customer) principal), "not.allowed");
			
			final PurchaseForm editPurchaseFormObject = new PurchaseForm(purchase);
			result = this.createEditModelAndView(editPurchaseFormObject);
			
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}
	
	/* Save */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(PurchaseForm purchaseForm, BindingResult binding) {
		ModelAndView result = new ModelAndView("redirect:/purchase/list.do");

		try {
			Purchase purchase = this.purchaseService.reconstruct(purchaseForm, binding);
			
			if (binding.hasErrors()) {
				result = this.createEditModelAndView(purchaseForm);
				
			} else {
				this.purchaseService.save(purchase);
			}
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(purchaseForm, oops.getMessage());
		}
		result.addObject("visible", true);
		
		return result;
	}
	
	/* Save */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "savedCC")
	public ModelAndView editSavedCC(PurchaseForm purchaseForm, BindingResult binding) {
		ModelAndView result = new ModelAndView("redirect:/purchase/list.do");

		try {
			Purchase purchase = this.purchaseService.savedCC(purchaseForm, binding);
			
			if (binding.hasErrors()) {
				result = this.createEditModelAndView(purchaseForm);
				
			} else {
				this.purchaseService.save(purchase);
			}
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(purchaseForm, oops.getMessage());
		}
		return result;
	}
	
	/* Redirect to create with new CC */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "otherCC")
	public ModelAndView editOtherCC(PurchaseForm purchaseForm, BindingResult binding) {
		ModelAndView result;

		try {
			result = new ModelAndView("redirect:/purchase/create.do?iRobotId=" + purchaseForm.getIRobot().getId() + "&CC=new");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}
	
	/* Ancillary methods */
	protected ModelAndView createEditModelAndView(final PurchaseForm purchaseForm) {
		return this.createEditModelAndView(purchaseForm, null);
	}

	protected ModelAndView createEditModelAndView(final PurchaseForm purchaseForm, final String messageCode) {
		ModelAndView result = new ModelAndView("purchase/edit");
		
		result.addObject("purchaseForm", purchaseForm);
		try {
			result.addObject("customer", this.utilityService.findUserByPrincipal());
			String[] aux = this.systemConfigurationService.findMySystemConfiguration().getMakers().split(",");
			result.addObject("makers", Arrays.asList(aux));
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		result.addObject("errMsg", messageCode);
		
		return result;
	}
}
