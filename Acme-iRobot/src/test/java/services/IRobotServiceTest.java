
package services;

import java.util.Collection;

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
import domain.IRobot;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class IRobotServiceTest extends AbstractTest {

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
	 * Coverage of the total project (%): 14.2
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 2.413
	 * 
	 * ################################################################
	 * 
	 * Test 1: A user displays iRobots and makes a iRobot
	 * 
	 * Test 2: A user lists and edits an iRobot
	 * 
	 * Test 3: A user displays an iRobot
	 * 
	 * Test 4: A scientist (De)Commission an iRobot
	 * 
	 * Test 5: A scientist deletes an iRobot
	 * 
	 */
	
	@Autowired
	private IRobotService	iRobotService;
	
	@Autowired
	private UtilityService utilityService;

	
	//Test 1: A scientist creates an iRobot
	//Req. 6.1
	@Test
	public void driverCreateIRobot() {
		final Object testingData[][] = {

			{
				"customer1", "Title", "Description", 20.5, IllegalArgumentException.class
			},
			//Negative test case, a customer tries to create an iRobot

			{
				"scientist1", "Title", "Body of iRobot", null, IllegalArgumentException.class
			},
			//Negative test case, a scientist tries to create an iRobot with a null price
			
			{
				"scientist1", "Title", "Body of iRobot", 20.5, null
			},
			//Positive test case, a customer creates an iRobot
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], 
					(Double) testingData[i][3],(Class<?>) testingData[i][4]);
		}
	}

	protected void template(final String user, final String title, final String description, 
			final Double price, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user);
			this.createIRobot(title, description, price);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public void createIRobot(final String title, final String description, final Double price) {
		
		IRobot iRobot = this.iRobotService.create();
		
		iRobot.setTitle(title);
		iRobot.setDescription(description);
		iRobot.setPrice(price);
		
		final BindingResult binding = new BeanPropertyBindingResult(iRobot, iRobot.getClass().getName());
		final IRobot result = this.iRobotService.reconstruct(iRobot, binding);
		Assert.isTrue(!binding.hasErrors());
		this.iRobotService.save(result);
	}
	
	//Test 2: A scientist lists and edits an iRobot
	//Req. 6.1, 6.2
	@Test
	public void driverEditIRobot() {
		final Object testingData[][] = {

			{
				"scientist1", "customer1", "Title", "Description", 20.5, "ACTIVE", IllegalArgumentException.class
			},
			//Negative test case, a customer tries to edit an iRobot of a scientist
			
			{
				"scientist1", "admin", "Title", "Description", 20.5, "ACTIVE", IllegalArgumentException.class
			},
			//Negative test case, an admin tries to edit an iRobot of a scientist
			
			{
				"scientist1", "scientist2", "Title", "Description", 20.5, "ACTIVE", IllegalArgumentException.class
			},
			//Negative test case, a scientist tries to edit an iRobot of another scientist
			
			{
				"scientist1", "scientist1", "", "Description", 20.5, "DECOMMISSIONED", IllegalArgumentException.class
			},
			//Negative test case, a scientist tries to edit an iRobot with a blank title
			
			{
				"scientist1", "scientist1", "Title", "", 20.5, "DECOMMISSIONED", IllegalArgumentException.class
			},
			//Negative test case, a scientist tries to edit an active iRobot with a blank description
			
			{
				"scientist1", "scientist1", "Title", "Body of iRobot", null, "ACTIVE", IllegalArgumentException.class
			},
			//Negative test case, a scientist tries to edit an iRobot with a null price
			
			{
				"scientist1", "scientist1", "Title", "Body of iRobot", 20.5, "DELETED", IllegalArgumentException.class
			},
			//Negative test case, a scientist tries to edit a deleted iRobot
			
			{
				"scientist1", "scientist1", "Title", "", 20.5, "DECOMMISSIONED", IllegalArgumentException.class
			},
			//Negative test case, a scientist tries to edit a decommissioned iRobot with a blank description
			
			{
				"scientist1", "scientist1", "Title", "Body of iRobot", 20.5, "DECOMMISSIONED", null
			},
			//Positive test case, a scientists edits a decommissioned iRobot
			
			{
				"scientist1", "scientist1", "Title", "Body of iRobot", 20.5, "ACTIVE", null
			},
			//Positive test case, a scientists edits an active iRobot
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template2((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], 
					(String) testingData[i][3], (Double) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
		}
	}

	protected void template2(final String user_1, final String user_2, final String title, final String description, 
			final Double price, final String mode, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user_1);
			Collection<IRobot> iRobots = this.iRobotService.findIRobotsMine(this.utilityService.findByPrincipal().getId());
			IRobot iRobot = null;
			for(IRobot robot : iRobots) {
				if(mode == "ACTIVE" ) {
					if(!robot.getIsDecommissioned()) {
						iRobot = robot;
						break;
					}
				} else if (mode == "DECOMMISSIONED") {
					if(robot.getIsDecommissioned()) {
						iRobot = robot;
						break;
					}
				} else if(mode == "DELETED") {
					if(robot.getIsDeleted()) {
						iRobot = robot;
					}
				}
			}
			this.unauthenticate();
			this.authenticate(user_2);
			this.editIRobot(iRobot, title, description, price);
			
			if(mode == "ACTIVE") {
				@SuppressWarnings("unused")
				IRobot toDisplay = this.iRobotService.findOneToDisplay(iRobot.getId());
			}

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public void editIRobot(final IRobot iRobot, final String title, final String description, final Double price) {
		
		IRobot aux = this.iRobotService.create();
		aux.setTitle(title);
		aux.setDescription(description);
		aux.setPrice(price);
		aux.setId(iRobot.getId());
		aux.setIsDecommissioned(iRobot.getIsDecommissioned());
		aux.setIsDeleted(iRobot.getIsDeleted());
		
		final BindingResult binding = new BeanPropertyBindingResult(aux, iRobot.getClass().getName());
		final IRobot result = this.iRobotService.reconstruct(aux, binding);
		Assert.isTrue(!binding.hasErrors());
		this.iRobotService.save(result);
	}
	
	//Test 3: A user displays an iRobot
	//Req. 4.2
	@Test
	public void driverDisplayIRobot() {
		final Object testingData[][] = {

			{
				"scientist2", "customer1", "DECOMMISSIONED", IllegalArgumentException.class
			},
			//Negative test case, a customer tries to display a decommissioned iRobot
			
			{
				"scientist1", "admin", "DECOMMISSIONED", IllegalArgumentException.class
			},
			//Negative test case, an admin tries to display a decommissioned iRobot
			
			{
				"scientist1", "customer1", "DELETED", IllegalArgumentException.class
			},
			//Negative test case, a customer tries to display a deleted iRobot
			
			{
				"scientist1", "scientist1", "DELETED", IllegalArgumentException.class
			},
			//Negative test case, a scientist tries to display a deleted iRobot
			
			{
				"scientist1", "customer1", "ACTIVE", null
			},
			//Positive test case, a customer displays an active iRobot
			
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template3((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
		}
	}

	protected void template3(final String user_1, final String user_2, final String mode, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user_1);
			Collection<IRobot> iRobots = this.iRobotService.findIRobotsMine(this.utilityService.findByPrincipal().getId());
			IRobot iRobot = null;
			for(IRobot robot : iRobots) {
				if(mode == "ACTIVE" ) {
					if(!robot.getIsDecommissioned()) {
						iRobot = robot;
						break;
					}
				} else if (mode == "DECOMMISSIONED") {
					if(robot.getIsDecommissioned()) {
						iRobot = robot;
						break;
					}
				} else if(mode == "DELETED") {
					if(robot.getIsDeleted()) {
						iRobot = robot;
					}
				}
			}
			
			this.unauthenticate();
			this.authenticate(user_2);
			
			@SuppressWarnings("unused")
			IRobot toDisplay = this.iRobotService.findOneToDisplay(iRobot.getId());
			
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}
	
	//Test 4: A scientist (De)Commission an iRobot
	//Req. 6.3
	@Test
	public void driverDe_CommissionIRobot() {
		final Object testingData[][] = {
			
			{
				"scientist2", "customer1", "DECOMMISSIONED", "COMMISSION", IllegalArgumentException.class
			},
			//Negative test case, a customer tries to commision a decommissioned iRobot
			
			{
				"scientist1", "admin", "DECOMMISSIONED", "COMMISSION", IllegalArgumentException.class
			},
			//Negative test case, an admin tries to commision a decommissioned iRobot
			
			{
				"scientist1", "customer1", "DELETED", "DECOMMISSION", IllegalArgumentException.class
			},
			//Negative test case, a customer tries to commision a deleted iRobot
			
			{
				"scientist1", "scientist1", "DELETED", "COMMISSION", IllegalArgumentException.class
			},
			//Negative test case, a scientist tries to commision a deleted iRobot
			
			{
				"scientist1", "scientist1", "ACTIVE", "DECOMMISSION", null
			},
			//Positive test case, a scientist change to decommissioned an active iRobot
			
			{
				"scientist1", "scientist1", "DECOMMISSIONED", "COMMISSION", null
			},
			//Positive test case, a scientist change to commissioned a decommissioned iRobot
			
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template4((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], 
					(String) testingData[i][3], (Class<?>) testingData[i][4]);
		}
	}

	protected void template4(final String user_1, final String user_2, final String mode, 
			final String action, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user_1);
			Collection<IRobot> iRobots = this.iRobotService.findIRobotsMine(this.utilityService.findByPrincipal().getId());
			IRobot iRobot = null;
			for(IRobot robot : iRobots) {
				if(mode == "ACTIVE" ) {
					if(!robot.getIsDecommissioned()) {
						iRobot = robot;
						break;
					}
				} else if (mode == "DECOMMISSIONED") {
					if(robot.getIsDecommissioned()) {
						iRobot = robot;
						break;
					}
				} else if(mode == "DELETED") {
					if(robot.getIsDeleted()) {
						iRobot = robot;
					}
				}
			}
			
			this.unauthenticate();
			this.authenticate(user_2);
			
			if(action == "COMMISSION") {
				this.iRobotService.activate(iRobot);
			} else if(action == "DECOMMISSION") {
				this.iRobotService.decommission(iRobot);
			}
			
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}
	
	//Test 5: A scientist deletes an iRobot
	//Req. 6.2
	@Test
	public void driverDeleteIRobot() {
		final Object testingData[][] = {
			
			{
				"scientist2", "customer1", "ACTIVE", IllegalArgumentException.class
			},
			//Negative test case, a customer tries to delete an active iRobot
			
			{
				"scientist1", "admin", "DECOMMISSIONED", IllegalArgumentException.class
			},
			//Negative test case, an admin tries to delete an iRobot
			
			{
				"scientist1", "scientist1", "DELETED", IllegalArgumentException.class
			},
			//Negative test case, a scientist tries to delete a deleted iRobot
			
			{
				"scientist1", "scientist1", "ACTIVE", IllegalArgumentException.class
			},
			//Positive test case, a scientist tries to delete an active iRobot
			
			{
				"scientist1", "scientist1", "DECOMMISSIONED", null
			},
			//Positive test case, a scientist deletes a decommissioned iRobot
			
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template5((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
		}
	}

	protected void template5(final String user_1, final String user_2, final String mode, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user_1);
			Collection<IRobot> iRobots = this.iRobotService.findIRobotsMine(this.utilityService.findByPrincipal().getId());
			IRobot iRobot = null;
			for(IRobot robot : iRobots) {
				if(mode == "ACTIVE" ) {
					if(!robot.getIsDecommissioned()) {
						iRobot = robot;
						break;
					}
				} else if (mode == "DECOMMISSIONED") {
					if(robot.getIsDecommissioned()) {
						iRobot = robot;
						break;
					}
				} else if(mode == "DELETED") {
					if(robot.getIsDeleted()) {
						iRobot = robot;
					}
				}
			}
			
			this.unauthenticate();
			this.authenticate(user_2);
			
			this.iRobotService.deleteIRobot(iRobot);
			
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

}
