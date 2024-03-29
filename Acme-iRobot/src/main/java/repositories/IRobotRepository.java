package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.IRobot;

@Repository
public interface IRobotRepository extends JpaRepository<IRobot, Integer> {
	
	@Query("select i from IRobot i where i.isDecommissioned = false and i.isDeleted = false")
	Collection<IRobot> findIRobotsNotDecommissioned();
	
	@Query("select i from IRobot i where i.isDecommissioned = true and i.isDeleted = false and i.scientist.id = ?1")
	Collection<IRobot> findIRobotsDecommissionedAndMine(Integer scientistId);
	
	@Query("select i from IRobot i where i.isDecommissioned = false and i.isDeleted = false and i.scientist.id = ?1")
	Collection<IRobot> findIRobotsNotDecommissionedAndMine(Integer scientistId);
	
	@Query("select i from IRobot i where i.scientist.id = ?1")
	Collection<IRobot> findIRobotsMine(Integer scientistId);
	
	@Query("select i from IRobot i where i.isDeleted = false and i.scientist.id = ?1")
	Collection<IRobot> findIRobotsNotDeleted(Integer scientistId);
	
	@Query("select case when (count(i) = 0) then true else false end from IRobot i where i.ticker = ?1")
	boolean uniqueTicket(String ticker);
}
