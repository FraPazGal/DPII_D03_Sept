package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import repositories.CustomerRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.CreditCard;
import domain.Customer;
import forms.UserForm;
import forms.UserRegistrationForm;

@Transactional
@Service
public class CustomerService {

	// Managed repository ------------------------------------

	@Autowired
	private CustomerRepository customerRepository;

	// Supporting services -----------------------------------

	@Autowired
	private SystemConfigurationService systemConfigurationService;

	@Autowired
	private UtilityService utilityService;
	
	@Autowired
	private CreditCardService creditCardService;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private FinderService finderService;
	
	@Autowired
	private PurchaseService purchaseService;
	
	// CRUD Methods ------------------------------------------

	public Customer create() {
		Customer res;

		UserAccount userAccount;
		Authority auth;
		Collection<Authority> authority;

		auth = new Authority();
		authority = new ArrayList<Authority>();
		userAccount = new UserAccount();
		res = new Customer();

		auth.setAuthority(Authority.SCIENTIST);
		authority.add(auth);
		userAccount.setAuthorities(authority);
		res.setUserAccount(userAccount);

		return res;
	}

	public Customer findOne(final Integer customerId) {

		Customer result = this.customerRepository.findOne(customerId);
		Assert.notNull(result, "wrong.id");
		return result;
	}

	public List<Customer> findAll() {
		return this.customerRepository.findAll();
	}

	public Customer save(final Customer customer) {
		Customer res;
		Actor principal;

		Assert.notNull(customer);

		if (customer.getId() != 0) {
			principal = this.utilityService.findByPrincipal();

			customer.setUserAccount(principal.getUserAccount());

			res = this.customerRepository.save(customer);
		} else {
			res = this.customerRepository.save(customer);
		}
		return res;
	}

	public void delete(final Customer customer) {
		Actor principal = this.utilityService.findByPrincipal();
		
		Assert.notNull(customer);
		Assert.isTrue(principal.getId() == customer.getId(), "not.allowed");
		Assert.isTrue(this.utilityService.checkAuthority(principal, "CUSTOMER"), "not.allowed");
		
		this.commentService.deleteComments(customer.getId());
		this.finderService.deleteFinder(customer.getId());
		this.purchaseService.anonymizePurchases(customer.getId());

		this.customerRepository.delete(customer.getId());
	}
	
	// Other business methods -------------------------------

	public Customer reconstruct(final UserRegistrationForm form, final BindingResult binding) {

		final Customer res = this.create();

		res.setName(form.getName());
		res.setSurname(form.getSurname());
		res.setPhoto(form.getPhoto());
		res.setEmail(form.getEmail());
		res.setPhoneNumber(form.getPhoneNumber());
		res.setAddress(form.getAddress());
		res.setVATNumber(form.getVATNumber());

		/* Creating user account */
		final UserAccount userAccount = new UserAccount();

		final List<Authority> authorities = new ArrayList<Authority>();
		final Authority authority = new Authority();
		authority.setAuthority(Authority.CUSTOMER);
		authorities.add(authority);
		userAccount.setAuthorities(authorities);

		userAccount.setUsername(form.getUsername());

		Md5PasswordEncoder encoder;
		encoder = new Md5PasswordEncoder();
		userAccount
				.setPassword(encoder.encodePassword(form.getPassword(), null));

		res.setUserAccount(userAccount);
		
		if(!binding.hasErrors()) {
			
			if(form.getHolder() != "" || form.getMake() != "" || form.getNumber() != "" || form.getCVV() != null ||
					form.getExpirationMonth() != null || form.getExpirationYear() != null) {
				
				try {
					Assert.isTrue(form.getHolder() != "" && form.getMake() != "" && form.getNumber() != "" &&form.getCVV() != null &&
							form.getExpirationMonth() != null && form.getExpirationYear() != null);
					
					try {
						Assert.isTrue(!this.creditCardService.checkIfExpired(form.getExpirationMonth(), form.getExpirationYear()));
					} catch (Throwable oops) {
						binding.rejectValue("expirationMonth", "card.date.error");
					}
					
				} catch (Throwable oops) {
					binding.rejectValue("cvv", "card.invalid");
				}
				
				CreditCard cc = new CreditCard();
				
				cc.setHolder(form.getHolder());
				cc.setMake(form.getMake());
				cc.setNumber(form.getNumber());
				cc.setCVV(form.getCVV());
				cc.setExpirationMonth(form.getExpirationMonth());
				cc.setExpirationYear(form.getExpirationYear());
				
				res.setCreditCard(cc);
			}
			
			if (form.getEmail() != null) {
				try {
					Assert.isTrue(this.utilityService.checkEmail(form.getEmail(),"CUSTOMER"));
				} catch (Throwable oops) {
					binding.rejectValue("email", "email.error");
				}
			}
			
			/* Managing phone number */
			if (form.getPhoneNumber() != null) {
				try {
					final char[] phoneArray = form.getPhoneNumber().toCharArray();
					if ((!form.getPhoneNumber().equals(null) && !form
							.getPhoneNumber().equals("")))
						if (phoneArray[0] != '+'
								&& Character.isDigit(phoneArray[0])) {
							final String sc = this.systemConfigurationService
									.findMySystemConfiguration().getCountryCode();
							form.setPhoneNumber(sc + " " + form.getPhoneNumber());
						}
				} catch (Throwable oops) {
					binding.rejectValue("phoneNumber", "phone.error");
				}
			}
		}
		
		return res;
	}

	public Customer reconstruct(final UserForm form, final BindingResult binding) {

		final Customer res = this.create();
		
		Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(principal.getId() == form.getId(), "not.allowed");

		res.setId(form.getId());
		res.setVersion(form.getVersion());
		res.setName(form.getName());
		res.setSurname(form.getSurname());
		res.setPhoto(form.getPhoto());
		res.setEmail(form.getEmail());
		res.setPhoneNumber(form.getPhoneNumber());
		res.setAddress(form.getAddress());
		res.setVATNumber(form.getVATNumber());
		
		if(!binding.hasErrors()) {
			
			if(form.getHolder() != "" || form.getMake() != "" || form.getNumber() != "" || form.getCVV() != null ||
					form.getExpirationMonth() != null || form.getExpirationYear() != null) {
				
				try {
					Assert.isTrue(form.getHolder() != "" && form.getMake() != "" && form.getNumber() != "" &&form.getCVV() != null &&
							form.getExpirationMonth() != null && form.getExpirationYear() != null);
					
					try {
						Assert.isTrue(!this.creditCardService.checkIfExpired(form.getExpirationMonth(), form.getExpirationYear()));
					} catch (Throwable oops) {
						binding.rejectValue("expirationMonth", "card.date.error");
					}
					
				} catch (Throwable oops) {
					binding.rejectValue("CVV", "card.invalid");
				}
				
				CreditCard cc = new CreditCard();
				
				cc.setHolder(form.getHolder());
				cc.setMake(form.getMake());
				cc.setNumber(form.getNumber());
				cc.setCVV(form.getCVV());
				cc.setExpirationMonth(form.getExpirationMonth());
				cc.setExpirationYear(form.getExpirationYear());
				
				res.setCreditCard(cc);
			}
			
			if (form.getEmail() != null) {
				try {
					Assert.isTrue(this.utilityService.checkEmail(form.getEmail(),"CUSTOMER"));
				} catch (Throwable oops) {
					binding.rejectValue("email", "email.error");
				}
			}
			
			/* Managing phone number */
			if (form.getPhoneNumber() != null) {
				try {
					final char[] phoneArray = form.getPhoneNumber().toCharArray();
					if ((!form.getPhoneNumber().equals(null) && !form
							.getPhoneNumber().equals("")))
						if (phoneArray[0] != '+'
								&& Character.isDigit(phoneArray[0])) {
							final String sc = this.systemConfigurationService
									.findMySystemConfiguration().getCountryCode();
							form.setPhoneNumber(sc + " " + form.getPhoneNumber());
						}
				} catch (Throwable oops) {
					binding.rejectValue("phoneNumber", "phone.error");
				}
			}
		}
		return res;
	}

	public void flush() {
		this.customerRepository.flush();
	}

}
