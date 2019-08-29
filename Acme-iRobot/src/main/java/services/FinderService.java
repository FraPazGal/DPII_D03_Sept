package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.FinderRepository;
import domain.Actor;
import domain.Customer;
import domain.Finder;
import domain.IRobot;

@Transactional
@Service
public class FinderService {

	// Managed repository ------------------------------------
	@Autowired
	private FinderRepository finderRepository;

	// Supporting services -----------------------------------

	@Autowired
	private UtilityService utilityService;

	@Autowired
	private SystemConfigurationService systemConfigurationService;
	
	@Autowired
	private Validator validator;


	// CRUD Methods ------------------------------------------

	public Finder create() {
		Finder result;
		Actor principal = this.utilityService.findByPrincipal();

		result = new Finder();
		result.setResults(new ArrayList<IRobot>());
		result.setCustomer((Customer) principal);
		return result;
	}

	public Finder findOne(final int finderId) {
		Finder result = this.finderRepository.findOne(finderId);
		Assert.notNull(result, "wrong.id");
		return result;
	}

	public Collection<Finder> findAll() {
		return this.finderRepository.findAll();
	}

	public Finder save(Finder finder) {
		Finder result;
		Actor principal;

		if (finder.getId() == 0) {
			result = this.finderRepository.save(finder);
		} else {
			principal = this.utilityService.findByPrincipal();
			
			Assert.isTrue(principal.equals(finder.getCustomer()), "not.allowed");
			Assert.notNull(finder, "not.allowed");

			finder.setSearchMoment(new Date(System.currentTimeMillis() - 1));
			result = this.finderRepository.save(finder);
			Assert.notNull(result, "not.null");
		}

		return result;
	}

	public void delete(Finder finder) {
		Actor principal;
		Finder aux;
		
		Assert.isTrue(finder.getId() != 0, "not.allowed");
		
		principal = this.utilityService.findByPrincipal();
		aux = this.findOne(finder.getId());
		
		Assert.isTrue(principal.equals(aux.getCustomer()), "not.allowed");
		
		finder.setCustomer(aux.getCustomer());
		finder.setVersion(aux.getVersion());
		finder.setResults(new ArrayList<IRobot>());
		finder.setKeyWord(null);
		finder.setMaximumPrice(null);
		finder.setMinimumPrice(null);

		this.save(finder);
	}

	// Other business methods -------------------------------

	public void deleteExpiredFinder(Finder finder) {
		Date maxLivedMoment = new Date();
		int timeChachedFind;

		timeChachedFind = this.systemConfigurationService
				.findMySystemConfiguration().getTimeResultsCached();
		maxLivedMoment = DateUtils.addHours(new Date(System.currentTimeMillis() - 1), -timeChachedFind);
		if (finder.getSearchMoment().before(maxLivedMoment)) {

			finder.setResults(new ArrayList<IRobot>());
			finder.setKeyWord(null);
			finder.setMaximumPrice(null);
			finder.setSearchMoment(null);
			finder.setMinimumPrice(null);

			this.save(finder);
		}
	}

	public Collection<IRobot> search(Finder finder) {

		Collection<IRobot> results = new ArrayList<IRobot>();
		String keyWord;
		Double maximumPrice, minimumPrice;
		int nResults;
		int count = 0;

		Collection<IRobot> resultsPageables = new ArrayList<IRobot>();

		nResults = this.systemConfigurationService.findMySystemConfiguration()
				.getMaxResults();
		
		keyWord = (finder.getKeyWord() == null || finder.getKeyWord().isEmpty()) ? ""
				: finder.getKeyWord();

		maximumPrice = (finder.getMaximumPrice() == null) ? 100000 : finder
				.getMaximumPrice();
		
		minimumPrice = (finder.getMinimumPrice() == null) ? 0 : finder
				.getMinimumPrice();

		if ((finder.getKeyWord() == null || finder.getKeyWord().isEmpty())
				&& finder.getMaximumPrice() == null
				&& finder.getMinimumPrice() == null) {
			results = this.allIRobotsNotDecommissioned();
			
		} else {
			results = this.search(keyWord, maximumPrice, minimumPrice);
		}

		for (IRobot p : results) {
			resultsPageables.add(p);
			count++;
			if (count >= nResults) {
				break;
			}
		}
		finder.setResults(resultsPageables);

		this.save(finder);

		return resultsPageables;
	}
	
	public Collection<IRobot> searchAnon (String keyword) {
		
		return this.finderRepository.searchAnon(keyword);
	}
	
	private Collection<IRobot> search(String keyword, Double maximumPrice, Double minimumPrice) {
		
		return this.finderRepository.search(keyword, maximumPrice, minimumPrice);
	}
	
	public Finder reconstruct (Finder finder, BindingResult binding) {
		Finder aux;
		Actor principal = this.utilityService.findByPrincipal();
		
		aux = this.findOne(finder.getId());
		
		Assert.isTrue(aux.getCustomer().equals(principal), "not.allowed");
		
		finder.setVersion(aux.getVersion());
		finder.setCustomer(aux.getCustomer());
		finder.setResults(aux.getResults());
		finder.setSearchMoment(new Date (System.currentTimeMillis() - 1));

		this.validator.validate(finder, binding);
		
		return finder;
	}

	private Collection<IRobot> allIRobotsNotDecommissioned() {
		return this.finderRepository.allIRobotsNotDecommissioned();
	}
	
	public Finder findFinderByCustomerId(int customerId) {
		
		return this.finderRepository.findFinderByCustomerId(customerId);
	}
	
	public Finder checkIfExpired() {
		Date maxLivedMoment = new Date();
		Actor principal = this.utilityService.findByPrincipal();
		Finder finder = this.findFinderByCustomerId(principal.getId());
		
		
		if (finder.getSearchMoment() != null) {
			final int timeChachedFind = this.systemConfigurationService.findMySystemConfiguration().getTimeResultsCached();
			maxLivedMoment = DateUtils.addHours(maxLivedMoment, -timeChachedFind);

			if (finder.getSearchMoment().before(maxLivedMoment))
				this.deleteExpiredFinder(finder);
		}
		
		return finder;
	}
	
	public void createForCustomer(Customer customer) {
		Finder finder = new Finder();
		
		finder.setResults(new ArrayList<IRobot>());
		finder.setCustomer(customer);
		finder = this.save(finder);
	}
	
	public void deleteFinder(int customerId){
		Finder toDelete = this.findFinderByCustomerId(customerId);
		this.finderRepository.delete(toDelete);
	}
}