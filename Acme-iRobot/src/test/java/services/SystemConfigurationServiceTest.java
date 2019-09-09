
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
import domain.SystemConfiguration;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SystemConfigurationServiceTest extends AbstractTest {

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
	 * Coverage of the total project (%): 5.2
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 892
	 * 
	 * ################################################################
	 * 
	 * Test 1: Edit system configuration
	 * 
	 */

	@Autowired
	private SystemConfigurationService	systemConfigurationService;

	//Test 1: Edit system configuration
	//Req.12
	@Test
	public void editDriver() {
		final Object testingData[][] = {
			
			{
				"admin", "customer", "systemName", "http://us.es", "+035", "makers", 10, 5, IllegalArgumentException.class
			},
			//negative: invalid authenticated
			
			{
				"admin", "admin", null, "http://us.es", "+35", "makers", 10, 5, IllegalArgumentException.class
			},
			//negative: invalid data
			
			{
				"admin", "admin", "systemName", "http://us.es", "+035", "makers", 10, 5, null
			}
			//positive
			
		};

		for (int i = 0; i < testingData.length; i++)
			this.editTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], 
					(String) testingData[i][5], (Integer) testingData[i][6], (Integer) testingData[i][7], (Class<?>) testingData[i][8]);
	}

	private void editTemplate(final String userList, final String user, final String systemName, final String banner, 
			final String countryCode, final String makers, final Integer time, final Integer max, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userList);
			final SystemConfiguration sys = this.systemConfigurationService.findMySystemConfiguration();
			this.unauthenticate();
			this.authenticate(user);
			this.editRoom(sys, systemName, banner, countryCode, makers, time, max);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}
		super.checkExceptions(expected, caught);

	}

	public void editRoom(final SystemConfiguration sys, final String systemName, final String banner, final String countryCode, final String makers, final Integer time, final Integer max) {
		final SystemConfiguration s = new SystemConfiguration();
		s.setBanner(banner);
		s.setBreachNotification(sys.getBreachNotification());
		s.setCountryCode(countryCode);
		s.setId(sys.getId());
		s.setMakers(makers);
		s.setMaxResults(max);
		s.setSystemName(systemName);
		s.setTimeResultsCached(time);
		s.setWelcomeMessage(sys.getWelcomeMessage());
		final BindingResult binding = new BeanPropertyBindingResult(s, s.getClass().getName());
		final String wes = sys.getWelcomeMessage().get("Español");
		final String wen = sys.getWelcomeMessage().get("English");
		final SystemConfiguration system = this.systemConfigurationService.reconstruct(s, wes, wen, "", "", binding);
		Assert.isTrue(binding.hasErrors() == false);
		this.systemConfigurationService.save(system);
	}
}
