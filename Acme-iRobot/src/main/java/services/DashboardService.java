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
	
	public Double[] StatsIRobotsPerScientist() {
		return this.dashboardRepository.statsIRobotsPerScientist();
	}
	
	public List<Scientist> Top10ScientistByPurchases() {
		List<Scientist> result = new ArrayList<>();
		
		result =  this.dashboardRepository.top10ScientistByPurchases();
		
		if(result.size() > 10) {
			return result.subList(0, 10);
		} else {
			return result;
		}
	}
	
	public List<IRobot> Top10BestSellingIRobots() {
		List<IRobot> result = new ArrayList<>();
		
		result =  this.dashboardRepository.top10BestSellingIRobots();
		
		if(result.size() > 10) {
			return result.subList(0, 10);
		} else {
			return result;
		}
	}
	
	public List<IRobot> Top10IRobotByFinders() {
		List<IRobot> result = new ArrayList<>();
		
		result =  this.dashboardRepository.top10IRobotByFinders();
		
		if(result.size() > 10) {
			return result.subList(0, 10);
		} else {
			return result;
		}
	}
	
	public List<IRobot> Bottom10IRobotByFinders() {
		List<IRobot> result = new ArrayList<>();
		
		result =  this.dashboardRepository.bottom10IRobotByFinders();
		
		if(result.size() > 10) {
			return result.subList(0, 10);
		} else {
			return result;
		}
	}
	
	public Double AverageResultsPerFinder() {
		return this.dashboardRepository.averageResultsPerFinder();
	}
	

}
