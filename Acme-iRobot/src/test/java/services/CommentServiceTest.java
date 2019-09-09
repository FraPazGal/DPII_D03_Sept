
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

import utilities.AbstractTest;
import domain.Comment;
import domain.IRobot;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CommentServiceTest extends AbstractTest {

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
	 * Coverage of the total project (%): 5.5%
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 927
	 * 
	 * ################################################################
	 * 
	 * Test 1: A user displays iRobots and makes a comment
	 * 
	 */
	
	@Autowired
	private CommentService	commentService;
	
	@Autowired
	private IRobotService	iRobotService;

	
	//Test 1: A user displays iRobots and makes a comment
	//Req. 5.3
	@Test
	public void driverCreateComment() {
		final Object testingData[][] = {

			{
				"scientist1", "Title", "", false, IllegalArgumentException.class
			},
			//Negative test case, a user tries to create a comment with a blank body

			{
				"customer1", "Title", "Body of comment", true, IllegalArgumentException.class
			},
			//Negative test case, a user tries to create a comment in a decommissioned iRobot
			
			{
				"admin", "Title", "Body of comment", false, null
			},
			//Positive test case, a customer makes a purchase with an existing credit card
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], 
					(Boolean) testingData[i][3],(Class<?>) testingData[i][4]);
		}
	}

	protected void template(final String user, final String title, final String body, final Boolean decommissionedIRobot, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user);
			this.createComment(title, body, decommissionedIRobot);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public void createComment(final String title, final String body, final Boolean decommissionedIRobot) {
		
		List<IRobot> iRobots = (List<IRobot>) this.iRobotService.findAll();
		Comment comment = null;
		
		for(IRobot iRobot : iRobots) {
			if(iRobot.getIsDecommissioned() == decommissionedIRobot) {
				comment = this.commentService.createWithIRobot(iRobot);
				
				comment.setTitle(title);
				comment.setBody(body);
			}
		}
		final BindingResult binding = new BeanPropertyBindingResult(comment, comment.getClass().getName());
		final Comment result = this.commentService.reconstruct(comment, binding);
		Assert.isTrue(!binding.hasErrors());
		this.commentService.save(result);
	}
	

}
