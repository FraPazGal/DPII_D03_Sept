package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.PurchaseRepository;
import domain.Actor;
import domain.CreditCard;
import domain.Customer;
import domain.IRobot;
import domain.Purchase;
import domain.User;
import forms.PurchaseForm;

@Service
@Transactional
public class PurchaseService {

	// Managed repository ------------------------------------

	@Autowired
	private PurchaseRepository purchaseRepository;

	// Supporting services -----------------------------------

	@Autowired
	private UtilityService utilityService;

	@Autowired
	private CreditCardService creditCardService;
	
	@Autowired
	private IRobotService iRobotService;

	@Autowired
	private Validator validator;

	// CRUD Methods ------------------------------------------

	public Purchase create() {
		Purchase result;
		Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "CUSTOMER"), "not.allowed");

		result = new Purchase();
		result.setCustomer((Customer) principal);
		result.setPurchaseMoment(new Date(System.currentTimeMillis() - 1));
		
		return result;
	}

	public Collection<Purchase> findAll() {
		
		return this.purchaseRepository.findAll();
	}

	public Purchase findOne(final int purchaseId) {
		
		Purchase result = this.purchaseRepository.findOne(purchaseId);
		Assert.notNull(result, "wrong.id");
		return result;
	}

	public Purchase save(final Purchase purchase) {
		Purchase result;
		Actor principal;

		Assert.notNull(purchase, "not.allowed");
		Assert.isTrue(purchase.getId() == 0, "not.allowed");

		Assert.notNull(purchase.getiRobot(), "not.allowed");
		Assert.notNull(purchase.getCreditCard(), "not.allowed");
		Assert.notNull(purchase.getCustomer(), "not.allowed");

		principal = this.utilityService.findByPrincipal();
		Assert.isTrue(purchase.getCustomer().equals(principal), "not.allowed");

		result = this.purchaseRepository.save(purchase);
		Assert.notNull(result);

		return result;
	}

	// Other business methods -------------------------------

	public Collection<Purchase> purchasesPerCustomer(int customerId) {

		return this.purchaseRepository.purchasesPerCustomer(customerId);
	}
	
	public Collection<Purchase> purchasesPerIRobot(int iRobotId) {
		Actor principal = this.utilityService.findByPrincipal();
		IRobot iRobot = this.iRobotService.findOne(iRobotId);
		Assert.isTrue(!iRobot.getIsDeleted(), "wrong.irobot.id");
		Assert.isTrue(principal.equals(iRobot.getScientist()), "not.allowed");

		return this.purchaseRepository.purchasesPerIRobot(iRobotId);
	}

	public Purchase reconstruct(PurchaseForm form, BindingResult binding) {
		Purchase purchase = this.create();

		Assert.isTrue(!form.getIRobot().getIsDecommissioned() && !form.getIRobot().getIsDeleted(), "wrong.irobot.id");
		purchase.setiRobot(form.getIRobot());
		purchase.setPrice(form.getIRobot().getPrice());

		/* Creating credit card */
		CreditCard creditCard = new CreditCard();

		creditCard.setHolder(form.getHolder());
		creditCard.setMake(form.getMake());
		creditCard.setNumber(form.getNumber());
		creditCard.setExpirationMonth(form.getExpirationMonth());
		creditCard.setExpirationYear(form.getExpirationYear());
		creditCard.setCVV(form.getCVV());

		this.validator.validate(creditCard, binding);
		
		/* Credit card */
		if (creditCard.getExpirationMonth() != null
				&& creditCard.getExpirationYear() != null) {

			try {
				Assert.isTrue(
						!this.creditCardService.checkIfExpired(
								creditCard.getExpirationMonth(),
								creditCard.getExpirationYear()));
			} catch (Throwable oops) {
				binding.rejectValue("expirationYear", "card.date.error");
			}
		}
		
		try {
			Assert.isTrue(this.utilityService.isValidCCMake(form.getMake()));
		} catch (Throwable oops) {
			binding.rejectValue("make", "invalid.make");
		}
		
		if (!binding.hasErrors()) {

			purchase.setCreditCard(creditCard);
		}

		this.validator.validate(purchase, binding);
		
		return purchase;
	}
	
	public Purchase savedCC(PurchaseForm form, BindingResult binding) {
		Purchase result = this.create();
		User principal = this.utilityService.findUserByPrincipal();
		
		Assert.isTrue(!form.getIRobot().getIsDecommissioned() && !form.getIRobot().getIsDeleted(), "wrong.irobot.id");
		Assert.notNull(principal.getCreditCard(), "no.creditcard");
		
		result.setiRobot(form.getIRobot());
		result.setCreditCard(principal.getCreditCard());
		result.setPrice(form.getIRobot().getPrice());
		
		this.validator.validate(result, binding);
		
		return result;
	}
	
	public void anonymizePurchases(int customerId) {
		Collection<Purchase> toAnonymize = this.purchasesPerCustomer(customerId);
		
		for(Purchase purchase : toAnonymize) {
			purchase.setCreditCard(null);
			purchase.setCustomer(null);
			this.purchaseRepository.save(purchase);
		}
	}
	
	public Purchase findOneAsCustomer(Integer purchaseId) {
		Purchase result = this.findOne(purchaseId);
		Actor principal = this.utilityService.findByPrincipal();
		
		Assert.isTrue(result.getCustomer().equals((Customer) principal), "not.allowed");
		
		return result;
	}
}
