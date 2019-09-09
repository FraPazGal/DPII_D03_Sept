package services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.DashboardRepository;
import domain.Actor;
import domain.IRobot;
import domain.Scientist;

@Transactional
@Service
public class DashboardService {
	
	// Managed repository ------------------------------
	
	@Autowired
	private DashboardRepository dashboardRepository;
	
	@Autowired
	private UtilityService utilityService;
	
	// Other business methods -------------------------------
	
	public Double[] statsIRobotsPerScientist() {
		Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"), "not.allowed");
		
		return this.dashboardRepository.statsIRobotsPerScientist();
	}
	
	public List<Scientist> top10ScientistByPurchases() {
		List<Scientist> result = new ArrayList<>();
		
		Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"), "not.allowed");
		
		result =  this.dashboardRepository.top10ScientistByPurchases();
		
		if(result.size() > 10) {
			return result.subList(0, 10);
		} else {
			return result;
		}
	}
	
	public List<IRobot> top10BestSellingIRobots() {
		List<IRobot> result = new ArrayList<>();
		
		Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"), "not.allowed");
		
		result =  this.dashboardRepository.top10BestSellingIRobots();
		
		if(result.size() > 10) {
			return result.subList(0, 10);
		} else {
			return result;
		}
	}
	
	public List<IRobot> top10IRobotByFinders() {
		List<IRobot> result = new ArrayList<>();
		
		Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"), "not.allowed");
		
		result =  this.dashboardRepository.top10IRobotByFinders();
		
		if(result.size() > 10) {
			return result.subList(0, 10);
		} else {
			return result;
		}
	}
	
	public List<IRobot> bottom10IRobotByFinders() {
		List<IRobot> result = new ArrayList<>();
		
		Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"), "not.allowed");
		
		result =  this.dashboardRepository.bottom10IRobotByFinders();
		
		if(result.size() > 10) {
			return result.subList(0, 10);
		} else {
			return result;
		}
	}
	
	public Double averageResultsPerFinder() {
		Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"), "not.allowed");
		
		return this.dashboardRepository.averageResultsPerFinder();
	}
	

}
