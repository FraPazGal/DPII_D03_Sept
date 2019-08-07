package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Administrator;
import domain.IRobot;
import domain.Scientist;

@Repository
public interface DashboardRepository extends
		JpaRepository<Administrator, Integer> {

	@Query("select max(1.0*(select count(*) from IRobot i where i.scientist = s)),min(1.0*(select count(*) from IRobot i where i.scientist = s)),avg(1.0*(select count(*) from IRobot i where i.scientist = s)),stddev(1.0*(select count(*) from IRobot i where i.scientist = s)) from Scientist s")
	Double[] statsIRobotsPerScientist();

	@Query("select s from Scientist s order by ((1.0*(select count(*) from Purchase p join p.iRobot i where i.scientist = s))) desc")
	List<Scientist> top10ScientistByPurchases();
	
	@Query("select i from IRobot i where i.isDeleted = false order by ((1.0*(select count(*) from Purchase p where p.iRobot = i))) desc")
	List<IRobot> top10BestSellingIRobots();
	
	@Query("select i from IRobot i where i.isDeleted = false order by ((1.0*(select count(*) from Finder f join f.results r where r = i))) desc")
	List<IRobot> top10IRobotByFinders();
	
	@Query("select i from IRobot i where i.isDeleted = false order by ((1.0*(select count(*) from Finder f join f.results r where r = i))) asc")
	List<IRobot> bottom10IRobotByFinders();
	
	@Query("select avg(f.results.size) from Finder f")
	Double averageResultsPerFinder();
	
}
