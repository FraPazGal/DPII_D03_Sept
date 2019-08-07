package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Purchase extends DomainEntity {

	/* Attributes */

	private Customer customer;
	private IRobot iRobot;
	private CreditCard creditCard;
	private Date purchaseMoment;
	private Double price;

	/* Getters and setters */

	@Valid
	@ManyToOne(optional = true)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	@Valid
	@ManyToOne(optional = true)
	public IRobot getiRobot() {
		return iRobot;
	}

	public void setiRobot(IRobot iRobot) {
		this.iRobot = iRobot;
	}

	@Valid
	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@NotNull
	public Date getPurchaseMoment() {
		return purchaseMoment;
	}

	public void setPurchaseMoment(Date purchaseMoment) {
		this.purchaseMoment = purchaseMoment;
	}
	
	@NotNull
	@Min(value = 0L, message = "The value must be positive")
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	
}
