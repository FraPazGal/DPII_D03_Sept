package forms;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import domain.IRobot;
import domain.Purchase;

public class PurchaseForm {

	/* Attributes */

	/* Purchase attributes */
	private int id;
	private int version;
	private IRobot iRobot;

	/* Credit Card attributes */
	private String holder;
	private String make;
	private String number;
	private Integer expirationMonth;
	private Integer expirationYear;
	private Integer CVV;

	public PurchaseForm() {

	}
	
	public PurchaseForm(Purchase purchase) {
		this.id = purchase.getId();
		this.version = purchase.getVersion();
		this.iRobot = purchase.getiRobot();
		this.holder = purchase.getCreditCard().getHolder();
		this.make = purchase.getCreditCard().getMake();
		this.number = purchase.getCreditCard().getNumber();
		this.expirationMonth = purchase.getCreditCard().getExpirationMonth();
		this.expirationYear = purchase.getCreditCard().getExpirationYear();
		this.CVV = purchase.getCreditCard().getCVV();
	}

	@Valid 
	@NotNull
	public IRobot getIRobot() {
		return iRobot;
	}
	public void setIRobot(IRobot iRobot) {
		this.iRobot = iRobot;
	}

	@NotBlank
	public String getHolder() {
		return holder;
	}

	public void setHolder(String holder) {
		this.holder = holder;
	}

	@NotBlank
	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	@NotBlank
	@CreditCardNumber
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@NotNull
	@Range(min = 1, max = 12)
	public Integer getExpirationMonth() {
		return expirationMonth;
	}

	public void setExpirationMonth(Integer expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	@NotNull
	@Range(min = 0, max = 99)
	public Integer getExpirationYear() {
		return expirationYear;
	}

	public void setExpirationYear(Integer expirationYear) {
		this.expirationYear = expirationYear;
	}

	@NotNull
	@Range(min = 0, max = 999)
	public Integer getCVV() {
		return CVV;
	}

	public void setCVV(Integer CVV) {
		this.CVV = CVV;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}