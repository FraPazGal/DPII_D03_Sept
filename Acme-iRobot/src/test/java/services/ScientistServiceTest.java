
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import utilities.AbstractTest;
import domain.Actor;
import domain.Scientist;
import forms.UserForm;
import forms.UserRegistrationForm;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ScientistServiceTest extends AbstractTest {

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
	 * Coverage of the total project (%): 19.1
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 3.244
	 * 
	 * ################################################################
	 * 
	 * Test 1: A scientist registers to the system
	 * 
	 * Test 2: A scientist displays and edits their profile
	 * 
	 * Test 3: A scientist exports their user data
	 * 
	 * Test 4: A scientist deletes their account
	 */
	
	@Autowired
	private ScientistService	scientistService;
	
	@Autowired
	private UtilityService	utilityService;
	
	@Autowired
	private Validator validator;

	//Test 1: A scientist registers to the system
	//Req. 4.1
	@Test
	public void driverRegisterScientist() {
		final Object testingData[][] = {

			{
				"scientistUsername", "password", "password", "Name", "Surname", "http://www.photo.com", "scientist@email.com", "64578621", "C/ Address nº 5",
					"ES64872168N", "Name of holder", "VISA", "1111222233334444", 10, 18, 164, true, IllegalArgumentException.class
			},
			//Negative test case, a user tries to register to the system as a scientist with an invalid credit card (expired)

			{
				"scientistUsername", "password", "password", "Name", "Surname", "http://www.photo.com", "scientist@email.com", "64578621", "C/ Address nº 5",
				"872168N", "Name of holder", "VISA", "1111222233334444", 10, 20, 164, true, IllegalArgumentException.class
			},
			//Negative test case, a user tries to register to the system as a scientist with an invalid VATNumber
			
			{
				"scientistUsername", "password", "password", "Name", "Surname", "http://www.photo.com", "scientist@email.com", "64578621", "C/ Address nº 5",
				"ES64872168N", "Name of holder", "VISA", "1111222233334444", 10, 20, 164, true, null
			},
			//Positive test case, a scientist registers to the system
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], 
					(String) testingData[i][4],(String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7],
					(String) testingData[i][8], (String) testingData[i][9], (String) testingData[i][10], (String) testingData[i][11],
					(String) testingData[i][12],(Integer) testingData[i][13],(Integer) testingData[i][14],(Integer) testingData[i][15],
					(Boolean) testingData[i][16],(Class<?>) testingData[i][17]);
		}
	}

	protected void template(final String username, final String password, final String confirmationPassword, 
			final String name, final String surname, final String photo, final String email, final String phoneNumber, 
			final String address, final String VATNumber, final String holder, final String make, final String number,
			final Integer expirationMonth, final Integer expirationYear, final Integer CVV, final Boolean terms, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.registerScientist(username, password, confirmationPassword, name, surname, photo, email, phoneNumber, address,
					VATNumber, holder, make, number, expirationMonth, expirationYear, CVV,terms);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public void registerScientist(final String username, final String password, final String confirmationPassword, final String name, final String surname, 
			final String photo, final String email, final String phoneNumber, final String address, final String VATNumber, final String holder, 
			final String make, final String number,	final Integer expirationMonth, final Integer expirationYear, final Integer CVV, final Boolean terms) {
		
		UserRegistrationForm userRegistrationForm = new UserRegistrationForm();
		
		userRegistrationForm.setUsername(username);
		userRegistrationForm.setPassword(password);
		userRegistrationForm.setPasswordConfirmation(confirmationPassword);
		userRegistrationForm.setName(name);
		userRegistrationForm.setSurname(surname);
		userRegistrationForm.setPhoto(photo);
		userRegistrationForm.setEmail(email);
		userRegistrationForm.setPhoneNumber(phoneNumber);
		userRegistrationForm.setAddress(address);
		userRegistrationForm.setVATNumber(VATNumber);
		userRegistrationForm.setHolder(holder);
		userRegistrationForm.setMake(make);
		userRegistrationForm.setNumber(number);
		userRegistrationForm.setExpirationMonth(expirationMonth);
		userRegistrationForm.setExpirationYear(expirationYear);
		userRegistrationForm.setCVV(CVV);
		userRegistrationForm.setTermsAndConditions(terms);
		
		final BindingResult binding = new BeanPropertyBindingResult(userRegistrationForm, userRegistrationForm.getClass().getName());
		final Scientist scientist = this.scientistService.reconstruct(userRegistrationForm, binding);
		this.validator.validate(userRegistrationForm, binding);
		Assert.isTrue(!binding.hasErrors());
		this.scientistService.save(scientist);
		
	}
	
	//Test 2: A scientist displays and edits their profile
	//Req. 5.2
	@Test
	public void driverEditScientist() {
		final Object testingData[][] = {

			{
				"customer1", "Name", "Surname", "http://www.photo.com", "scientist@email.com", "64578621", "C/ Address nº 5",
					"ES64872168N", "Name of holder", "VISA", "1111222233334444", 10, 18, 164, IllegalArgumentException.class
			},
			//Negative test case, a customer tries to edit the profile of a scientist

			{
				"scientist1", "Name", "Surname", "http://www.photo.com", "scientist@email.com", "64578621", "C/ Address nº 5",
				"ES64872168N", "Name of holder", "VISA", "1111111111111", 10, 20, 164, IllegalArgumentException.class
			},
			//Negative test case, a scientist tries to edit their profile with an credit card (number)
			
			{
				"scientist1", "Name", "Surname", "http://www.photo.com", "scientist@email.com", "64578621", "C/ Address nº 5",
				"ES64872168N", "", "", "", null, null, null, null
			},
			//Positive test case, a scientist edits their profile
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template2((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], 
					(String) testingData[i][4],(String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7],
					(String) testingData[i][8], (String) testingData[i][9], (String) testingData[i][10], (Integer) testingData[i][11], 
					(Integer) testingData[i][12],(Integer) testingData[i][13], (Class<?>) testingData[i][14]);
		}
	}

	protected void template2(final String user, final String name, final String surname, final String photo, final String email, final String phoneNumber, 
			final String address, final String VATNumber, final String holder, final String make, final String number,
			final Integer expirationMonth, final Integer expirationYear, final Integer CVV, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user);
			this.editScientist(name, surname, photo, email, phoneNumber, address,
					VATNumber, holder, make, number, expirationMonth, expirationYear, CVV);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public void editScientist(final String name, final String surname, final String photo, final String email, final String phoneNumber, 
			final String address, final String VATNumber, final String holder, final String make, final String number,	final Integer expirationMonth, 
			final Integer expirationYear, final Integer CVV) {
		
		UserForm userForm = new UserForm();
		
		Actor principal = this.utilityService.findByPrincipal();
		
		userForm.setId(principal.getId());
		userForm.setName(name);
		userForm.setSurname(surname);
		userForm.setPhoto(photo);
		userForm.setEmail(email);
		userForm.setPhoneNumber(phoneNumber);
		userForm.setAddress(address);
		userForm.setVATNumber(VATNumber);
		userForm.setHolder(holder);
		userForm.setMake(make);
		userForm.setNumber(number);
		userForm.setExpirationMonth(expirationMonth);
		userForm.setExpirationYear(expirationYear);
		userForm.setCVV(CVV);
		
		final BindingResult binding = new BeanPropertyBindingResult(userForm, userForm.getClass().getName());
		final Scientist scientist = this.scientistService.reconstruct(userForm, binding);
		this.validator.validate(userForm, binding);
		Assert.isTrue(!binding.hasErrors());
		this.scientistService.save(scientist);
		
	}
	
	//Test 3: A scientist exports their user data
	//Req. 9
	@Test
	public void driverExportDataScientist() {
		final Object testingData[][] = {

			{
				"customer1", ClassCastException.class
			},
			//Negative test case, a customer tries to export the user data of a scientist

			{
				"admin", ClassCastException.class
			},
			//Negative test case, an admin tries to export the user data of a scientist
			
			{
				"scientist1", null
			},
			//Positive test case, a scientist exports their user data
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template3((String) testingData[i][0], (Class<?>) testingData[i][1]);
		}
	}

	protected void template3(final String user, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user);
			this.scientistService.exportData();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}
	
	
	//Test 4: A scientist deletes their account
	//Req. 9
	@Test
	public void driverDeleteScientist() {
		final Object testingData[][] = {

				{
					"scientist1", "admin", IllegalArgumentException.class
				},
				//Negative test case, an admin tries to delete the account of a scientist

				{
					"scientist1", "customer1", IllegalArgumentException.class
				},
				//Negative test case, a customer tries to delete the account of a scientist
				
				{
					"scientist1", "scientist1", null
				},
				//Positive test case, a scientist deletes their account
			};

			for (int i = 0; i < testingData.length; i++) {
				this.template4((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			}
		}

		protected void template4(final String user_1, final String user_2, final Class<?> expected) {
			Class<?> caught;

			caught = null;

			try {
				this.authenticate(user_1);
				Scientist scientist = (Scientist) this.utilityService.findByPrincipal();
				this.unauthenticate();
				this.authenticate(user_2);
				this.scientistService.delete(scientist);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

}
