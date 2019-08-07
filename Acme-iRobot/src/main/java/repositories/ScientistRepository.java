package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Scientist;

@Repository
public interface ScientistRepository extends JpaRepository<Scientist, Integer> {

	@Query("select a from Scientist a where a.userAccount.username = ?1")
	Scientist findByUsername(String username);

}
