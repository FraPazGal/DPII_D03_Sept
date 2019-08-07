package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Purchase;

@Repository
public interface PurchaseRepository extends	JpaRepository<Purchase, Integer> {
	
	@Query("select p from Purchase p where p.customer.id = ?1")
	Collection<Purchase> purchasesPerCustomer(Integer customerId);
	
	@Query("select p from Purchase p where p.iRobot.id = ?1")
	Collection<Purchase> purchasesPerIRobot(Integer iRobotId);

}
