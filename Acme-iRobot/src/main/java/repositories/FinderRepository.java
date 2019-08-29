package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Finder;
import domain.IRobot;

@Repository
public interface FinderRepository extends JpaRepository<Finder, Integer> {

	@Query("select distinct i from IRobot i where ( i.title like %?1% or i.ticker like %?1% or i.description like %?1%) and  i.price <= ?2 and i.price >= ?3 and i.isDecommissioned = false")
	Collection<IRobot> search(String keyWord, Double maximumPrice, Double minimumPrice);
	
	@Query("select distinct i from IRobot i where ( i.title like %?1% or i.ticker like %?1% or i.description like %?1%) and i.isDecommissioned = false")
	Collection<IRobot> searchAnon(String keyWord);

	@Query("select i from IRobot i where i.isDecommissioned = false")
	Collection<IRobot> allIRobotsNotDecommissioned();
	
	@Query("select f from Finder f where f.customer.id = ?1")
	Finder findFinderByCustomerId(int customerId);
	
	

}
