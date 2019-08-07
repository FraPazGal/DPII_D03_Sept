package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.IRobotRepository;
import domain.Actor;
import domain.IRobot;
import domain.Scientist;
import domain.User;

@Transactional
@Service
public class IRobotService {

	// Managed repository ------------------------------------

	@Autowired
	private IRobotRepository iRobotRepository;

	// Supporting services -----------------------------------

	@Autowired
	private UtilityService utilityService;
	
	@Autowired
	private ScientistService scientistService;

	@Autowired
	private Validator validator;

	// CRUD Methods ------------------------------------------

	public IRobot create() {
		User principal;
		IRobot result;

		principal = this.utilityService.findUserByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "SCIENTIST"), "not.allowed");
		Assert.isTrue(principal.getCreditCard() != null, "need.creditcard");

		result = new IRobot();
		result.setScientist((Scientist) principal);
		result.setIsDecomissioned(false);
		result.setIsDeleted(false);
		result.setTicker(this.utilityService.generateTicker((Scientist) principal));

		return result;
	}

	public Collection<IRobot> findAll() {
		return this.iRobotRepository.findAll();
	}

	public IRobot findOne(final int iRobotId) {
		IRobot result = this.iRobotRepository.findOne(iRobotId);
		Assert.notNull(result, "wrong.id");
		return result;
	}

	public IRobot save(final IRobot iRobot) {
		Actor principal;
		IRobot result;

		Assert.notNull(iRobot);
		Assert.notNull(iRobot.getScientist());
		Assert.notNull(iRobot.getTitle());
		Assert.notNull(iRobot.getDescription());
		Assert.notNull(iRobot.getPrice());

		principal = this.utilityService.findByPrincipal();

		Assert.isTrue(this.utilityService.checkAuthority(principal, "SCIENTIST"), "not.allowed");

		result = this.iRobotRepository.save(iRobot);
		Assert.notNull(result);

		return result;
	}

	public void delete(final IRobot iRobot) {
		Actor principal;

		Assert.notNull(iRobot);
		Assert.isTrue(iRobot.getId() != 0, "wrong.irobot.id");
		
		principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "SCIENTIST"), "not.allowed");

		Assert.isTrue(iRobot.getScientist().equals(principal), "not.allowed");

		this.iRobotRepository.delete(iRobot.getId());
	}

	// Other business methods -------------------------------

	public IRobot reconstruct(final IRobot iRobot,BindingResult binding) {
		IRobot result, aux = null;
		Actor principal = this.utilityService.findByPrincipal();

		result = this.create();

		if (iRobot.getId() != 0) {
			aux = this.findOne(iRobot.getId());
			Assert.isTrue(aux.getScientist().equals((Scientist) principal), "not.allowed");
			Assert.isTrue(!iRobot.getIsDeleted(), "wrong.irobot.id");

			result.setId(aux.getId());
			result.setVersion(aux.getVersion());
			result.setScientist(aux.getScientist());
			result.setIsDecomissioned(aux.getIsDecomissioned());
			result.setIsDeleted(aux.getIsDeleted());
		}

		result.setTitle(iRobot.getTitle());
		result.setDescription(iRobot.getDescription());
		result.setPrice(iRobot.getPrice());

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.iRobotRepository.flush();
	}
	
	public Collection<IRobot> findIRobotsNotDecomissioned() {
		return this.iRobotRepository.findIRobotsNotDecomissioned();
	}

	public Collection<IRobot> findIRobotsDecomissionedAndMine(Integer scientistId) {
		this.scientistService.findOne(scientistId);
		return this.iRobotRepository.findIRobotsDecomissionedAndMine(scientistId);
	}
	
	public Collection<IRobot> findIRobotsNotDecomissionedAndMine(Integer scientistId) {
		this.scientistService.findOne(scientistId);
		return this.iRobotRepository.findIRobotsNotDecomissionedAndMine(scientistId);
	}
	
	public Collection<IRobot> findIRobotsMine(Integer scientistId) {
		this.scientistService.findOne(scientistId);
		return this.iRobotRepository.findIRobotsMine(scientistId);
	}
	
	public Collection<IRobot> findIRobotsNotDeleted(Integer scientistId) {
		this.scientistService.findOne(scientistId);
		return this.iRobotRepository.findIRobotsNotDeleted(scientistId);
	}
	
	public void activate(IRobot iRobot) {
		Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(iRobot.getScientist().equals(principal), "not.allowed");
		Assert.isTrue(!iRobot.getIsDeleted(), "wrong.irobot.id");
		Assert.isTrue(iRobot.getIsDecomissioned(), "already.activated");
		
		iRobot.setIsDecomissioned(false);
		
		this.save(iRobot);
	}
	
	public void decomission(IRobot iRobot) {
		Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(iRobot.getScientist().equals(principal), "not.allowed");
		Assert.isTrue(!iRobot.getIsDeleted(), "wrong.irobot.id");
		Assert.isTrue(!iRobot.getIsDecomissioned(), "already.decomissioned");
		
		iRobot.setIsDecomissioned(true);
		
		this.save(iRobot);
	}
	
	public void deleteIRobot(IRobot iRobot) {
		Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(iRobot.getScientist().equals(principal), "not.allowed");
		Assert.isTrue(!iRobot.getIsDeleted(), "wrong.irobot.id");
		Assert.isTrue(iRobot.getIsDecomissioned(), "irobot.not.decomissioned");
		
		iRobot.setIsDeleted(true);
		
		this.save(iRobot);
	}
	
	public IRobot findOneToDisplay(int iRobotId) {
		IRobot result = this.findOne(iRobotId);
		
		Assert.isTrue(!result.getIsDeleted(), "wrong.irobot.id");
		Assert.isTrue(!result.getIsDecomissioned(), "irobot.isDecomissioned.true");
		
		return result;
	}
	
	public boolean uniqueTicket(String ticker) {

		return this.iRobotRepository.uniqueTicket(ticker);
	}
	
	public void subDeleteIRobots(int scientistId) {
		Collection<IRobot> toSubDelete = this.findIRobotsMine(scientistId);
		for(IRobot iRobot : toSubDelete) {
			iRobot.setIsDeleted(true);
			iRobot.setScientist(null);
			this.iRobotRepository.save(iRobot);
		}
		this.flush();
	}
}
