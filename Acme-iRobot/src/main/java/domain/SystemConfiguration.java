package domain;

import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class SystemConfiguration extends DomainEntity {

	/* Attributes */

	private String systemName, banner, countryCode, makers;
	private Map<String, String> welcomeMessage, breachNotification;
	private Integer timeResultsCached, maxResults;

	/* Getters and setters */

	@NotBlank
	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	@URL
	@NotBlank
	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	}

	@NotBlank
	@Pattern(regexp = "[+]\\d{3}")
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	@NotBlank
	public String getMakers() {
		return this.makers;
	}

	public void setMakers(final String makers) {
		this.makers = makers;
	}

	@NotEmpty
	@ElementCollection
	public Map<String, String> getWelcomeMessage() {
		return welcomeMessage;
	}

	public void setWelcomeMessage(Map<String, String> welcomeMessage) {
		this.welcomeMessage = welcomeMessage;
	}
	
	@ElementCollection
	public Map<String, String> getBreachNotification() {
		return breachNotification;
	}

	public void setBreachNotification(Map<String, String> breachNotification) {
		this.breachNotification = breachNotification;
	}

	@NotNull
	@Range(min = 1, max = 24)
	public Integer getTimeResultsCached() {
		return timeResultsCached;
	}

	public void setTimeResultsCached(Integer timeResultsCached) {
		this.timeResultsCached = timeResultsCached;
	}

	@NotNull
	@Range(min = 0, max = 100)
	public Integer getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}
}
