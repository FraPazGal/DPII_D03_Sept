package services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.DashboardRepository;
import domain.IRobot;
import domain.Scientist;

@Transactional
@Service
public class DashboardService {
	
	// Managed repository ------------------------------
	
	@Autowired
	private DashboardRepository dashboardRepository;
	
	// Other business methods -------------------------------
	
	public Double[] statsIRobotsPerScientist() {
		return this.dashboardRepository.statsIRobotsPerScientist();
	}
	
	public List<Scientist> top10ScientistByPurchases() {
		List<Scientist> result = new ArrayList<>();
		
		result =  this.dashboardRepository.top10ScientistByPurchases();
		
		if(result.size() > 10) {
			return result.subList(0, 10);
		} else {
			return result;
		}
	}
	
	public List<IRobot> top10BestSellingIRobots() {
		List<IRobot> result = new ArrayList<>();
		
		result =  this.dashboardRepository.top10BestSellingIRobots();
		
		if(result.size() > 10) {
			return result.subList(0, 10);
		} else {
			return result;
		}
	}
	
	public List<IRobot> top10IRobotByFinders() {
		List<IRobot> result = new ArrayList<>();
		
		result =  this.dashboardRepository.top10IRobotByFinders();
		
		if(result.size() > 10) {
			return result.subList(0, 10);
		} else {
			return result;
		}
	}
	
	public List<IRobot> bottom10IRobotByFinders() {
		List<IRobot> result = new ArrayList<>();
		
		result =  this.dashboardRepository.bottom10IRobotByFinders();
		
		if(result.size() > 10) {
			return result.subList(0, 10);
		} else {
			return result;
		}
	}
	
	public Double averageResultsPerFinder() {
		return this.dashboardRepository.averageResultsPerFinder();
	}
	

}
