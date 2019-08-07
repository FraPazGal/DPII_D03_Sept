package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
	
	@Query("select c from Comment c where c.IRobot.id = ?1")
	Collection<Comment> getCommentsOfIRobot(int iRobotId);
	
	@Query("select c from Comment c where c.writer.id = ?1")
	Collection<Comment> findCommentByUserId(int userId);

}
