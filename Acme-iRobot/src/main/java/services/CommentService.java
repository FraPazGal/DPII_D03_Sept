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
import domain.User;

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
		Assert.isTrue(this.utilityService.checkAuthority(principal, "SCIENTIST") || 
				this.utilityService.checkAuthority(principal, "CUSTOMER"), "not.allowed");

		result.setWriter((User) principal);
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

		Assert.isTrue(comment.getId() == 0);
		Assert.notNull(comment.getIRobot());
		Assert.notNull(comment.getTitle());
		Assert.notNull(comment.getBody());
		
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
		Assert.isTrue(!iRobot.getIsDeleted() && !iRobot.getIsDecomissioned(), "wrong.id");
		
		Comment result = this.create();
		result.setIRobot(iRobot);
		
		return result;
	}
	
	public Collection<Comment> findCommentByUserId(Integer userId) {
		
		return this.commentRepository.findCommentByUserId(userId);
	}
	
	public void deleteComments(int userId) {
		Collection<Comment> toDelete = this.commentRepository.findCommentByUserId(userId);
		this.commentRepository.deleteInBatch(toDelete);
	}

}
