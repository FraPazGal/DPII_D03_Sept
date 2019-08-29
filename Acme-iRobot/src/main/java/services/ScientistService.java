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

import repositories.ScientistRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.CreditCard;
import domain.Scientist;
import forms.UserForm;
import forms.UserRegistrationForm;

@Transactional
@Service
public class ScientistService {

	// Managed repository ------------------------------------

	@Autowired
	private ScientistRepository scientistRepository;

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
	private IRobotService iRobotService;

	// CRUD Methods ------------------------------------------

	public Scientist create() {
		Scientist res;

		UserAccount userAccount;
		Authority auth;
		Collection<Authority> authority;

		auth = new Authority();
		authority = new ArrayList<Authority>();
		userAccount = new UserAccount();
		res = new Scientist();

		auth.setAuthority(Authority.SCIENTIST);
		authority.add(auth);
		userAccount.setAuthorities(authority);
		res.setUserAccount(userAccount);

		return res;
	}

	public Scientist findOne(final Integer scientistId) {

		Scientist result = this.scientistRepository.findOne(scientistId);
		Assert.notNull(result, "wrong.id");
		return result;
	}

	public List<Scientist> findAll() {
		return this.scientistRepository.findAll();
	}

	public Scientist save(final Scientist scientist) {
		Scientist res;
		Actor principal;

		Assert.notNull(scientist, "not.allowed");

		if (scientist.getId() != 0) {
			principal = this.utilityService.findByPrincipal();

			scientist.setUserAccount(principal.getUserAccount());

			res = this.scientistRepository.save(scientist);
		} else {
			res = this.scientistRepository.save(scientist);
		}
		return res;
	}

	public void delete(final Scientist scientist) {
		Actor principal = this.utilityService.findByPrincipal();
		
		Assert.notNull(scientist, "not.allowed");
		Assert.isTrue(principal.getId() == scientist.getId(), "not.allowed");
		Assert.isTrue(this.utilityService.checkAuthority(principal, "SCIENTIST"), "not.allowed");
		
		this.commentService.deleteComments(scientist.getId());
		this.iRobotService.subDeleteIRobots(scientist.getId());

		this.scientistRepository.delete(scientist.getId());
	}
	
	// Other business methods -------------------------------

	public Scientist reconstruct(final UserRegistrationForm form, final BindingResult binding) {

		final Scientist res = this.create();

		res.setName(form.getName());
		res.setSurname(form.getSurname());
		res.setPhoto(form.getPhoto());
		res.setEmail(form.getEmail());
		res.setPhoneNumber(this.utilityService.addCountryCode(form.getPhoneNumber()));
		res.setAddress(form.getAddress());
		res.setVATNumber(form.getVATNumber());

		/* Creating user account */
		final UserAccount userAccount = new UserAccount();

		final List<Authority> authorities = new ArrayList<Authority>();
		final Authority authority = new Authority();
		authority.setAuthority(Authority.SCIENTIST);
		authorities.add(authority);
		userAccount.setAuthorities(authorities);

		userAccount.setUsername(form.getUsername());

		Md5PasswordEncoder encoder;
		encoder = new Md5PasswordEncoder();
		userAccount
				.setPassword(encoder.encodePassword(form.getPassword(), null));

		res.setUserAccount(userAccount);
		
		/* CreditCard */
		if(!form.getHolder().isEmpty() || !form.getNumber().isEmpty() || form.getCVV() != null ||
				form.getExpirationMonth() != null || form.getExpirationYear() != null) {
			
			try {
				Assert.isTrue(form.getHolder() != "" && form.getMake() != "" && form.getNumber() != "" &&form.getCVV() != null &&
						form.getExpirationMonth() != null && form.getExpirationYear() != null);
				
				try {
					Assert.isTrue(this.utilityService.isValidCCMake(form.getMake()));
				} catch (Throwable oops) {
					binding.rejectValue("make", "invalid.make");
				}
				
				try {
					Assert.isTrue(!this.creditCardService.checkIfExpired(form.getExpirationMonth(), form.getExpirationYear()));
					
					CreditCard cc = new CreditCard();
					
					cc.setHolder(form.getHolder());
					cc.setMake(form.getMake());
					cc.setNumber(form.getNumber());
					cc.setCVV(form.getCVV());
					cc.setExpirationMonth(form.getExpirationMonth());
					cc.setExpirationYear(form.getExpirationYear());
					
					res.setCreditCard(cc);
					
				} catch (Throwable oops) {
					binding.rejectValue("expirationYear", "card.date.error");
				}
				
			} catch (Throwable oops) {
				binding.rejectValue("CVV", "card.invalid");
			}
		}
			
		/* Username */
		if (form.getUsername() != null) {
			try {
				Assert.isTrue(this.utilityService.existsUsername(form.getUsername()));
			} catch (final Throwable oops) {
				binding.rejectValue("username", "username.error");
			}
		}
		
		/* Password confirmation */
		if (!form.getPassword().isEmpty() && !form.getPasswordConfirmation().isEmpty()) {
			try {
				Assert.isTrue(form.getPassword().equals(form.getPasswordConfirmation()));
			} catch (final Throwable oops) {
				binding.rejectValue("passwordConfirmation", "password.confirmation.error");
			}
		}

		/* Terms&Conditions */
		if (form.getTermsAndConditions() != null) {
			try {
				Assert.isTrue((form.getTermsAndConditions()));
			} catch (final Throwable oops) {
				binding.rejectValue("termsAndConditions", "terms.error");
			}
		}
			
		/* Email */
		if (!form.getEmail().isEmpty()) {
			try {
				Assert.isTrue(this.utilityService.checkEmail(form.getEmail(),"SCIENTIST"));
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
		
		return res;
	}

	public Scientist reconstruct(final UserForm form, final BindingResult binding) {

		final Scientist res = this.create();
		
		Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(principal.getId() == form.getId(), "not.allowed");
		
		res.setId(form.getId());
		res.setVersion(form.getVersion());
		res.setName(form.getName());
		res.setSurname(form.getSurname());
		res.setPhoto(form.getPhoto());
		res.setEmail(form.getEmail());
		res.setPhoneNumber(this.utilityService.addCountryCode(form.getPhoneNumber()));
		res.setAddress(form.getAddress());
		res.setVATNumber(form.getVATNumber());
		
		/* CreditCard */
		if(!form.getHolder().isEmpty() || !form.getNumber().isEmpty() || form.getCVV() != null ||
				form.getExpirationMonth() != null || form.getExpirationYear() != null) {
			
			try {
				Assert.isTrue(form.getHolder() != "" && form.getMake() != "" && form.getNumber() != "" &&form.getCVV() != null &&
						form.getExpirationMonth() != null && form.getExpirationYear() != null);
				
				try {
					Assert.isTrue(this.utilityService.isValidCCMake(form.getMake()));
				} catch (Throwable oops) {
					binding.rejectValue("make", "invalid.make");
				}
				
				try {
					Assert.isTrue(!this.creditCardService.checkIfExpired(form.getExpirationMonth(), form.getExpirationYear()));
					
					CreditCard cc = new CreditCard();
					
					cc.setHolder(form.getHolder());
					cc.setMake(form.getMake());
					cc.setNumber(form.getNumber());
					cc.setCVV(form.getCVV());
					cc.setExpirationMonth(form.getExpirationMonth());
					cc.setExpirationYear(form.getExpirationYear());
					
					res.setCreditCard(cc);
					
				} catch (Throwable oops) {
					binding.rejectValue("expirationYear", "card.date.error");
				}
				
			} catch (Throwable oops) {
				binding.rejectValue("CVV", "card.invalid");
			}
		}
			
		/* Email */
		if (!form.getEmail().isEmpty()) {
			try {
				Assert.isTrue(this.utilityService.checkEmail(form.getEmail(),"SCIENTIST"));
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
		
		return res;
	}

	public void flush() {
		this.scientistRepository.flush();
	}

}
