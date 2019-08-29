package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class IRobot extends DomainEntity {

	/* Attributes */

	private String title, ticker, description;
	private Double price;
	private boolean isDecommissioned, isDeleted;
	private Scientist scientist;

	/* Getters and setters */

	@NotBlank
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@NotBlank
	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	@NotBlank
	@Type(type="text")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@NotNull
	@Min(value = 0L)
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public boolean getIsDecommissioned() {
		return isDecommissioned;
	}

	public void setIsDecommissioned(boolean isDecommissioned) {
		this.isDecommissioned = isDecommissioned;
	}
	
	public boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Valid
	@ManyToOne(optional = true)
	public Scientist getScientist() {
		return scientist;
	}

	public void setScientist(Scientist scientist) {
		this.scientist = scientist;
	}
	
}
