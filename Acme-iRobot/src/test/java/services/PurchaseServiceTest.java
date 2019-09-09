
package services;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import utilities.AbstractTest;
import domain.Actor;
import domain.IRobot;
import domain.Purchase;
import forms.PurchaseForm;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class PurchaseServiceTest extends AbstractTest {

	/*
	 * Total coverage of all tests
	 * 
	 * 
	 * Coverage of the total project (%): 65.4
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 11.124
	 * 
	 * ################################################################
	 * 
	 * Total coverage by exclusively executing this test class
	 * 
	 * 
	 * Coverage of the total project (%): 9.3
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 1.588
	 * 
	 * ################################################################
	 * 
	 * Test 1: A customer displays iRobots and makes a new purchase
	 * 
	 * Test 2: A customer list and displays their purchases
	 * 
	 * Test 3: A customer exports their user data
	 * 
	 * Test 4: A customer deletes their account
	 */
	
	@Autowired
	private PurchaseService	purchaseService;
	
	@Autowired
	private IRobotService	iRobotService;
	
	@Autowired
	private UtilityService	utilityService;

	//Test 1: A customer displays iRobots and makes a new purchase
	//Req. 7.1
	@Test
	public void driverCreatePurchase() {
		final Object testingData[][] = {

			{
				"scientist1", "Name of holder", "VISA", "1111222233334444", 10, 18, 164, true, IllegalArgumentException.class
			},
			//Negative test case, a scientist tries to make a purchase

			{
				"customer1", "", "VISA", "1111222233334444", 10, 20, 164, true, IllegalArgumentException.class
			},
			//Negative test case, a customer tries to make a purchase with an invalid credit card (blank holder)
			
			{
				"customer1", "Name of holder", "VISA", "1111222233334444", 10, 20, 164, true, null
			},
			//Positive test case, a customer makes a purchase with a new credit card
			
			{
				"customer1", "Name of holder", "VISA", "1111222233334444", 10, 20, 164, false, null
			},
			//Positive test case, a customer makes a purchase with an existing credit card
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], 
					(Integer) testingData[i][4],(Integer) testingData[i][5],(Integer) testingData[i][6], 
					(Boolean) testingData[i][7],(Class<?>) testingData[i][8]);
		}
	}

	protected void template(final String user, final String holder, final String make, final String number, final Integer expirationMonth, 
			final Integer expirationYear, final Integer CVV, final Boolean newCC, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user);
			this.createPurchase(holder, make, number, expirationMonth, expirationYear, CVV, newCC);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public void createPurchase(final String holder, final String make, final String number, final Integer expirationMonth, 
			final Integer expirationYear, final Integer CVV, final Boolean newCC) {
		
		List<IRobot> iRobots = (List<IRobot>) this.iRobotService.findIRobotsNotDecommissioned();
		Assert.notEmpty(iRobots);
		
		PurchaseForm purchaseForm = new PurchaseForm();
		final BindingResult binding = new BeanPropertyBindingResult(purchaseForm, purchaseForm.getClass().getName());
		Purchase purchase = null;
		
		purchaseForm.setIRobot(iRobots.get(0));
		
		if(newCC) {
			purchaseForm.setHolder(holder);
			purchaseForm.setMake(make);
			purchaseForm.setNumber(number);
			purchaseForm.setExpirationMonth(expirationMonth);
			purchaseForm.setExpirationYear(expirationYear);
			purchaseForm.setCVV(CVV);
			
			purchase = this.purchaseService.reconstruct(purchaseForm, binding);
		} else {
			purchase = this.purchaseService.savedCC(purchaseForm, binding);
		}
		
		Assert.isTrue(!binding.hasErrors());
		this.purchaseService.save(purchase);
		
	}
	
	//Test 2: A customer list and displays their purchases
	//Req. 7.2
	@Test
	public void driverListAndDisplayPurchase() {
		final Object testingData[][] = {

			{
				"customer1", "scientist1", ClassCastException.class
			},
			//Negative test case, a scientist tries to  access the purchases of a customer

			{
				"customer1", "customer2", IllegalArgumentException.class
			},
			//Negative test case, a customer tries to access the purchases of another customer
			
			{
				"customer1", "customer1", null
			},
			//Positive test case, a customer rlist and displays their purchases
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template1((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
		}
	}

	@SuppressWarnings("unused")
	protected void template1(final String user_1, final String user_2, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user_1);
			Actor user1 = this.utilityService.findByPrincipal();
			this.unauthenticate();
			
			this.authenticate(user_2);
			Collection<Purchase> purchases = this.purchaseService.purchasesPerCustomer(user1.getId());
			for(Purchase p : purchases) {
				Purchase purchase = this.purchaseService.findOneAsCustomer(p.getId());
			}

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}	

}
