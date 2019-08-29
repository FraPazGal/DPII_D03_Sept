package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CommentRepository;
import domain.Actor;
import domain.Comment;
import domain.IRobot;

@Transactional
@Service
public class CommentService {
	
	// Managed repository ------------------------------------

	@Autowired
	private CommentRepository commentRepository;
	
	// Supporting services -----------------------------------

	@Autowired
	private UtilityService utilityService;

	@Autowired
	private Validator validator;
	
	// CRUD Methods ------------------------------------------

	public Comment create() {
		Comment result = new Comment();
		Actor principal;

		principal = this.utilityService.findByPrincipal();
		Assert.notNull(principal, "not.allowed");

		result.setWriter(principal);
		result.setAuthor(principal.getUserAccount().getUsername());
		result.setPublishedDate(new Date(System.currentTimeMillis() - 1));
		
		return result;
	}
	
	public Collection<Comment> findAll() {
		
		return this.commentRepository.findAll();
	}

	public Comment findOne(final int commentId) {
		Comment result = this.commentRepository.findOne(commentId);
		Assert.notNull(result, "wrong.id");
		return result;
	}

	public Comment save(final Comment comment) {
		Comment result;

		Assert.isTrue(comment.getId() == 0, "not.allowed");
		Assert.notNull(comment.getIRobot(), "not.allowed");
		Assert.notNull(comment.getTitle(), "not.allowed");
		Assert.notNull(comment.getBody(), "not.allowed");
		
		result = this.commentRepository.save(comment);

		return result;
	}
	
	// Other business methods -------------------------------
	
	public Comment reconstruct(final Comment comment,BindingResult binding) {
		Comment result = null;

		result = this.createWithIRobot(comment.getIRobot());
		
		result.setBody(comment.getBody());
		result.setTitle(comment.getTitle());

		this.validator.validate(result, binding);

		return result;
	}

	public Collection<Comment> getCommentsOfIRobot(Integer iRobotid) {
		return this.commentRepository.getCommentsOfIRobot(iRobotid);
	}
	
	public Comment createWithIRobot(IRobot iRobot) {
		Assert.isTrue(!iRobot.getIsDeleted() && !iRobot.getIsDecommissioned(), "wrong.id");
		
		Comment result = this.create();
		result.setIRobot(iRobot);
		
		return result;
	}
	
	public Collection<Comment> findCommentByActorId(Integer userId) {
		
		return this.commentRepository.findCommentByActorId(userId);
	}
	
	public void deleteComments(int userId) {
		Collection<Comment> toDelete = this.commentRepository.findCommentByActorId(userId);
		this.commentRepository.deleteInBatch(toDelete);
	}

}
