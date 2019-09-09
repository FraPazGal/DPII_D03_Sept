
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

import utilities.AbstractTest;
import domain.Finder;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class FinderServiceTest extends AbstractTest {

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
	 * Coverage of the total project (%): 7.7
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 1.310
	 * 
	 * ################################################################
	 * 
	 * Test 1: A customer search for iRobots to cached
	 * 
	 * Test 2: A customer delete his cached finder
	 */

	@Autowired
	private FinderService	finderService;

	@Autowired
	private UtilityService	utilityService;


	//Test 1: A customer search for iRobots to cached
	//Req. 14.1
	@Test
	public void finderDriver() {
		final Object testingData[][] = {
				
			{
				"customer1", "admin", "iRobot", 25.0, 150.50, ClassCastException.class
			},
			//Negative test case, an admin tries to search with the finder of a customer
			
			{
				"customer1", "customer2", "iRobot", 25.0, 150.50, IllegalArgumentException.class
			},
			//Negative test case, a customer deletes their account
			
			{
				"customer1", "customer1", "", null, null, null
			}
			//Positive test case, a customer deletes their account
			
		};

		for (int i = 0; i < testingData.length; i++)
			this.finderTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], 
					(Double) testingData[i][3], (Double) testingData[i][4],(Class<?>) testingData[i][5]);
	}

	private void finderTemplate(final String userList, final String user, final String keyWord, final Double minimumPrice, 
			final Double maximumPrice, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userList);
			final Finder finder = this.finderService.findFinderByCustomerId(this.utilityService.findByPrincipal().getId());
			this.unauthenticate();
			this.authenticate(user);
			this.finderRoom(finder, keyWord, minimumPrice, maximumPrice);
			
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}
		super.checkExceptions(expected, caught);

	}

	public void finderRoom(final Finder finder, final String keyWord, final Double minimumPrice, final Double maximumPrice) {
		final Finder f = this.finderService.create();
		
		f.setId(finder.getId());
		f.setKeyWord(keyWord);
		f.setMaximumPrice(maximumPrice);
		f.setMinimumPrice(minimumPrice);
		
		final BindingResult binding = new BeanPropertyBindingResult(f, f.getClass().getName());
		final Finder fin = this.finderService.reconstruct(f, binding);
		Assert.isTrue(binding.hasErrors() == false);
		f.setResults(this.finderService.search(fin));
		Assert.notEmpty(f.getResults());
	}
	
	
	//Test 2: A customer delete his cached finder
	//Req. 14.1
	@Test
	public void finderDeleteDriver() {
		final Object testingData[][] = {
			{
				"customer1", "customer1", null
			},//positive
			{
				"customer1", "admin", IllegalArgumentException.class
			},//negative: invalid authenticated
			{
				"customer1", "customer2", IllegalArgumentException.class
			}
		//negative: invalid data
		};

		for (int i = 0; i < testingData.length; i++)
			this.finderDeleteTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	private void finderDeleteTemplate(final String userList, final String user, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userList);
			final Finder finder = this.finderService.findFinderByCustomerId(this.utilityService.findByPrincipal().getId());
			this.unauthenticate();
			this.authenticate(user);
			this.finderDeleteRoom(finder);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}
		super.checkExceptions(expected, caught);

	}

	public void finderDeleteRoom(final Finder finder) {
		final BindingResult binding = new BeanPropertyBindingResult(finder, finder.getClass().getName());
		final Finder fin = this.finderService.reconstruct(finder, binding);
		Assert.isTrue(binding.hasErrors() == false);
		Assert.isTrue(finder.getCustomer().equals(fin.getCustomer()));
		this.finderService.delete(fin);
	}
}
