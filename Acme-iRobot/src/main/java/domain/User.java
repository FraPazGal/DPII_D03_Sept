package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class User extends Actor {
	
	/* Attributes */
	
	private String VATNumber;
	private CreditCard creditCard;
	
	/* Getters and setters */
	
	@NotBlank
	public String getVATNumber() {
		return VATNumber;
	}
	
	public void setVATNumber(String vATNumber) {
		VATNumber = vATNumber;
	}
	
	@Valid
	public CreditCard getCreditCard() {
		return creditCard;
	}
	
	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}
	
}
