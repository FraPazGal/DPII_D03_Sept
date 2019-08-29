package forms;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import domain.User;

public class UserForm {

	/* Attributes */

	private int id, version;
	private String name, surname, photo, email, phoneNumber,
			address, VATNumber;

	/* Credit Card attributes */
	private String holder;
	private String make;
	private String number;
	private Integer expirationMonth;
	private Integer expirationYear;
	private Integer CVV;

	/* Getters and setters */
	
	public UserForm() {
		
	}

	public UserForm(User user) {
		this.id = user.getId();
		this.version = user.getVersion();
		this.name = user.getName();
		this.surname = user.getSurname();
		this.photo = user.getPhoto();
		this.email = user.getEmail();
		this.phoneNumber = user.getPhoneNumber();
		this.address = user.getAddress();
		this.VATNumber = user.getVATNumber();
		
		if(user.getCreditCard() != null) {
			this.holder = user.getCreditCard().getHolder();
			this.make = user.getCreditCard().getMake();
			this.number = user.getCreditCard().getNumber();
			this.expirationMonth = user.getCreditCard().getExpirationMonth();
			this.expirationYear = user.getCreditCard().getExpirationYear();
			this.CVV = user.getCreditCard().getCVV();
		}
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

	@NotBlank
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotBlank
	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@URL
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@NotBlank
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@NotBlank
	@Pattern(regexp = "^ES\\d{8}[A-Z]{1}$")
	public String getVATNumber() {
		return VATNumber;
	}

	public void setVATNumber(String VATNumber) {
		this.VATNumber = VATNumber;
	}
	
	public String getHolder() {
		return holder;
	}

	public void setHolder(String holder) {
		this.holder = holder;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	@CreditCardNumber
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Range(min = 1, max = 12)
	public Integer getExpirationMonth() {
		return expirationMonth;
	}

	public void setExpirationMonth(Integer expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	@Range(min = 0, max = 99)
	public Integer getExpirationYear() {
		return expirationYear;
	}

	public void setExpirationYear(Integer expirationYear) {
		this.expirationYear = expirationYear;
	}

	@Range(min = 0, max = 999)
	public Integer getCVV() {
		return CVV;
	}

	public void setCVV(Integer CVV) {
		this.CVV = CVV;
	}

}
