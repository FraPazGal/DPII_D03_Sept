package services;

import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import repositories.UserRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Scientist;
import domain.User;

@Transactional
@Service
public class UtilityService {

	// Managed repository ------------------------------------
	
	@Autowired
	private ActorRepository actorRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private IRobotService iRobotService;	
	
	@Autowired
	private SystemConfigurationService systemConfigurationService;	
	
	// Other business methods -------------------------------

	public Boolean checkEmail(final String email, final String authority) {
		Boolean result;
		String emailLowerCase = email.toLowerCase();

		final Pattern pattern = Pattern
				.compile("(^(([a-z]|[0-9]){1,}[@]{1}([a-z]|[0-9]){1,}([.]{1}([a-z]|[0-9]){1,}){1,})$)|(^((([a-z]|[0-9]){1,}[ ]{1}){1,}<(([a-z]|[0-9]){1,}[@]{1}([a-z]|[0-9]){1,}([.]{1}([a-z]|[0-9]){1,}){1,})>)$)");
		final Matcher matcher = pattern.matcher(emailLowerCase);
		if (authority.equals("ADMIN")) {
			final Pattern patternAdmin = Pattern
					.compile("(^((([a-z]|[0-9]){1,}[@])$)|(^(([a-z]|[0-9]){1,}[ ]{1}){1,}<(([a-z]|[0-9]){1,}[@]>))$)");
			final Matcher matcherAdmin = patternAdmin.matcher(emailLowerCase);
			result = matcherAdmin.matches() ? true : false;
		} else
			result = matcher.matches() ? true : false;
		return result;
	}

	public Actor findByPrincipal() {
		Actor result = null;
		final UserAccount userAccount = LoginService.getPrincipal();
		result = this.actorRepository.findByUserAccountId(userAccount.getId());
		return result;
	}
	
	public User findUserByPrincipal() {
		User result = null;
		final UserAccount userAccount = LoginService.getPrincipal();
		result = this.userRepository.findByUserAccountId(userAccount.getId());
		return result;
	}

	public boolean checkAuthority(final Actor actor, final String authority) {
		Assert.notNull(actor, "not.allowed");
		boolean result = false;
		if (actor.getUserAccount().getAuthorities().iterator().next()
				.getAuthority().equals(authority))
			result = true;
		return result;
	}

	public Boolean existsUsername(String username) {
		return !(this.actorRepository.existsUsername(username) != null);
	}

	public boolean isAdmin() {
		boolean isAdmin = false;

		try {
			isAdmin = this.checkAuthority(this.findByPrincipal(), "ADMIN");
		} catch (Throwable oops) {

		}
		return isAdmin;
	}
	
	public String generateTicker(Scientist scientist) {
		String uniqueTicker = null;
		String surname = null, alphaNum;
		boolean unique = false;
		
		if(scientist.getSurname().length() >= 4) {
			surname = scientist.getSurname().substring(0, 4);
		} else {
			surname = scientist.getSurname().substring(0, scientist.getSurname().length());
		}
		if(surname.length() < 4) {
			for(int length = 4 - surname.length(); length != 0 ;length--) {
				surname = surname.concat("X");
			}
		}
		while (unique == false) {
			alphaNum = this.randomStringTicker();
			uniqueTicker = surname + "-" + alphaNum;
			unique = this.checkForUniqueTicket(uniqueTicker);
		}
		return uniqueTicker;
	}

	private String randomStringTicker() {

		final String possibleChars = "0123456789";
		final SecureRandom rnd = new SecureRandom();
		final int length = 4;

		final StringBuilder stringBuilder = new StringBuilder(length);

		for (int i = 0; i < length; i++)
			stringBuilder.append(possibleChars.charAt(rnd.nextInt(possibleChars
					.length())));
		return stringBuilder.toString();

	}
	
	private boolean checkForUniqueTicket(String ticker) {
		return this.iRobotService.uniqueTicket(ticker);
	}
	
	public boolean isValidCCMake (String toValidate) {
		boolean isValid = false;
		String[] aux = this.systemConfigurationService.findMySystemConfiguration().getMakers().split(",");
		for(String validMake : aux) {
			if(toValidate.contentEquals(validMake)) {
				isValid = true;
				break;
			}
		}
		return isValid;
	}
	
	public String addCountryCode (String phoneNumber) {
		String result = phoneNumber;
		final Pattern patternPN = Pattern.compile("(^(([0-9]{4}){1}([0-9]{0,}))$)");
		final Matcher matcher = patternPN.matcher(phoneNumber);
		if(matcher.matches()) {
			String countryCode = this.systemConfigurationService.findMySystemConfiguration().getCountryCode();
			result = countryCode + " " + result;
		}
		return result;
	}
}
