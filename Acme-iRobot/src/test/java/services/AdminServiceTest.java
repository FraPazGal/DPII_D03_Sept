
package services;

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
import org.springframework.validation.Validator;

import utilities.AbstractTest;
import domain.Actor;
import domain.Administrator;
import domain.IRobot;
import domain.Scientist;
import forms.ActorForm;
import forms.ActorRegistrationForm;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class AdminServiceTest extends AbstractTest {

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
	 * Coverage of the total project (%): 14,4
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 2.434
	 * 
	 * ################################################################
	 * 
	 * Test 1: An admin registers a new admin to the system
	 * 
	 * Test 2: An admin display and edits their profile
	 * 
	 * Test 3: An admin exports their user data
	 * 
	 * Test 4: An admin deletes their user data
	 * 
	 * Test 5: An admin displays the system statistics
	 */
	
	@Autowired
	private AdministratorService	administratorService;
	
	@Autowired
	private UtilityService	utilityService;
	
	@Autowired
	private DashboardService dashboardService;
	
	@Autowired
	private Validator validator;	

	//Test 1: An admin registers a new admin to the system
	//Req. 8.1
	@Test
	public void driverRegisterAdmin() {
		final Object testingData[][] = {

			{
				"scientist1", "newAdmin", "password", "password", "Name", "Surname", "http://www.photo.com", "adminemail@", "64578621", "C/ Address nº 5", true, IllegalArgumentException.class
			},
			//Negative test case, a scientist tries to register a new admin

			{
				"admin", "newAdmin", "password", "differentPassword", "Name", "Surname", "http://www.photo.com", "adminemail@", "64578621", "C/ Address nº 5", true, IllegalArgumentException.class
			},
			//Negative test case, an admin tries to register a new admin but the confirmation password doesn't match
			
			{
				"admin", "newAdmin", "password", "password", "Name", "Surname", "http://www.photo.com", "adminemail@", "64578621", "C/ Address nº 5", true, null
			},
			//Positive test case, an admin registers a new admin to the system
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], 
					(String) testingData[i][4],(String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7],
					(String) testingData[i][8], (String) testingData[i][9], (Boolean) testingData[i][10],(Class<?>) testingData[i][11]);
		}
	}

	protected void template(final String user, final String newUser, final String password, final String confirmationPassword, 
			final String name, final String surname, final String photo, final String email, final String phoneNumber, 
			final String address, final Boolean terms, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user);
			this.registerAdmin(newUser, password, confirmationPassword, name, surname, photo, email, phoneNumber, address, terms);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public void registerAdmin(final String username, final String password, final String confirmationPassword, final String name, final String surname, 
			final String photo, final String email, final String phoneNumber, final String address, final Boolean terms) {
		
		ActorRegistrationForm actorRegistrationForm = new ActorRegistrationForm();
		
		actorRegistrationForm.setUsername(username);
		actorRegistrationForm.setPassword(password);
		actorRegistrationForm.setPasswordConfirmation(confirmationPassword);
		actorRegistrationForm.setName(name);
		actorRegistrationForm.setSurname(surname);
		actorRegistrationForm.setPhoto(photo);
		actorRegistrationForm.setEmail(email);
		actorRegistrationForm.setPhoneNumber(phoneNumber);
		actorRegistrationForm.setAddress(address);
		actorRegistrationForm.setTermsAndConditions(terms);
		
		final BindingResult binding = new BeanPropertyBindingResult(actorRegistrationForm, actorRegistrationForm.getClass().getName());
		final Administrator admin = this.administratorService.reconstruct(actorRegistrationForm, binding);
		this.validator.validate(actorRegistrationForm, binding);
		Assert.isTrue(!binding.hasErrors());
		this.administratorService.save(admin);
		
	}
	
	//Test 2: An admin displays and edits their profile
	//Req. 5.1, 5.2
	@Test
	public void driverEditAdmin() {
		final Object testingData[][] = {

			{
				"scientist1", "Name", "Surname", "http://www.photo.com", "adminemail@", "64578621", "C/ Address nº 5",  IllegalArgumentException.class
			},
			//Negative test case, a scientist tries to edit the profile of an admin

			{
				"admin", "Name", "Surname", "http://www.photo.com", "adminemail@notvalid.com", "64578621", "C/ Address nº 5", IllegalArgumentException.class
			},
			//Negative test case, an admin tries to edit their profile with an invalid email
			
			{
				"admin", "Name", "Surname", "http://www.photo.com", "adminemail@", "64578621", "C/ Address nº 5", null
			},
			//Positive test case, an admin edits their profile
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template2((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], 
					(String) testingData[i][4],(String) testingData[i][5], (String) testingData[i][6], (Class<?>) testingData[i][7]);
		}
	}

	protected void template2(final String user,	final String name, final String surname, final String photo, final String email, 
			final String phoneNumber, final String address, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user);
			this.editAdmin(name, surname, photo, email, phoneNumber, address);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public void editAdmin(final String name, final String surname, final String photo, final String email, final String phoneNumber, final String address) {
		
		//Display
		Actor principal = this.utilityService.findByPrincipal();
		
		ActorForm actorForm = new ActorForm();
		
		actorForm.setId(principal.getId());
		actorForm.setName(name);
		actorForm.setSurname(surname);
		actorForm.setPhoto(photo);
		actorForm.setEmail(email);
		actorForm.setPhoneNumber(phoneNumber);
		actorForm.setAddress(address);
		
		final BindingResult binding = new BeanPropertyBindingResult(actorForm, actorForm.getClass().getName());
		final Administrator admin = this.administratorService.reconstruct(actorForm, binding);
		this.validator.validate(actorForm, binding);
		Assert.isTrue(!binding.hasErrors());
		this.administratorService.save(admin);
		
	}
	
	//Test 3: An admin exports their user data
	//Req. 9
	@Test
	public void driverExportDataAdmin() {
		final Object testingData[][] = {

			{
				"scientist1", IllegalArgumentException.class
			},
			//Negative test case, a scientist tries to export the user data of an admin

			{
				"customer1", IllegalArgumentException.class
			},
			//Negative test case, a customer tries to export the user data of an admin
			
			{
				"admin", null
			},
			//Positive test case, an admin exports their user data
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
			this.administratorService.exportData();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}
	
	
	//Test 4: An admin deletes their account
	//Req. 9
	@Test
	public void driverDeleteAdmin() {
		final Object testingData[][] = {

			{
				"admin", "scientist1", IllegalArgumentException.class
			},
			//Negative test case, a scientist tries to delete the account of an admin

			{
				"admin", "customer1", IllegalArgumentException.class
			},
			//Negative test case, a customer tries to delete the account of an admin
			
			{
				"admin", "admin", null
			},
			//Positive test case, an admin deletes their account
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
			Administrator admin = (Administrator) this.utilityService.findByPrincipal();
			this.unauthenticate();
			this.authenticate(user_2);
			this.administratorService.delete(admin);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}
	
	//Test 5: An admin displays the system statistics
	//Req. 8.2, 15.1
	@Test
	public void driverSystemStatistics() {
		final Object testingData[][] = {

			{
				"scientist1", IllegalArgumentException.class
			},
			//Negative test case, a scientist tries to delete the user data of an admin

			{
				"customer1", IllegalArgumentException.class
			},
			//Negative test case, a customer tries to delete the user data of an admin
			
			{
				"admin", null
			},
			//Positive test case, an admin deletes their user data
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template5((String) testingData[i][0], (Class<?>) testingData[i][1]);
		}
	}

	@SuppressWarnings("unused")
	protected void template5(final String user, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user);
			
			Double [] statsIRobotsPerScientist = this.dashboardService.statsIRobotsPerScientist();
			List<Scientist> top10ScientistByPurchases = this.dashboardService.top10ScientistByPurchases();
			List<IRobot> top10BestSellingIRobots = this.dashboardService.top10BestSellingIRobots();
			List<IRobot> top10IRobotByFinders = this.dashboardService.top10IRobotByFinders();
			List<IRobot> bottom10IRobotByFinders = this.dashboardService.bottom10IRobotByFinders();
			Double averageResultsPerFinder = this.dashboardService.averageResultsPerFinder();
			
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

}
