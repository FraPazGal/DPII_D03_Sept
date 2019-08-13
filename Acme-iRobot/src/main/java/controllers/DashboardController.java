package controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.DashboardService;
import services.UtilityService;
import domain.Actor;
import domain.IRobot;
import domain.Scientist;

@Controller
@RequestMapping("/statistics")
public class DashboardController extends AbstractController{
	
	/* Services */
	
	@Autowired
	private DashboardService dashboardService;	
	
	@Autowired
	private UtilityService utilityService;
	
	/* Display */
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		Actor principal;
		ModelAndView result = new ModelAndView("administrator/statistics");;
		
		try {
			principal = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"), "not.allowed");
			
			Double [] statsIRobotsPerScientist = this.dashboardService.StatsIRobotsPerScientist();
			List<Scientist> top10ScientistByPurchases = this.dashboardService.Top10ScientistByPurchases();
			List<IRobot> top10BestSellingIRobots = this.dashboardService.Top10BestSellingIRobots();
			List<IRobot> top10IRobotByFinders = this.dashboardService.Top10IRobotByFinders();
			List<IRobot> bottom10IRobotByFinders = this.dashboardService.Bottom10IRobotByFinders();
			Double averageResultsPerFinder = this.dashboardService.AverageResultsPerFinder();
			
			result.addObject("statsIRobotsPerScientist",statsIRobotsPerScientist);
			result.addObject("top10ScientistByPurchases",top10ScientistByPurchases);
			result.addObject("top10BestSellingIRobots",top10BestSellingIRobots);
			result.addObject("top10IRobotByFinders",top10IRobotByFinders);
			result.addObject("bottom10IRobotByFinders",bottom10IRobotByFinders);
			result.addObject("averageResultsPerFinder",averageResultsPerFinder);
			
		} catch (Throwable oops) {
			result.addObject("errMsg", oops.getMessage());
		}
		return result;
	}
}

