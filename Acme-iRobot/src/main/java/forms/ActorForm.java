package forms;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import domain.Actor;

public class ActorForm {

	/* Attributes */

	private int id, version;
	private String name, surname, photo, email, phoneNumber,
			address;

	/* Getters and setters */

	public ActorForm() {

	}

	public ActorForm(Actor actor) {
		this.id = actor.getId();
		this.version = actor.getVersion();
		this.name = actor.getName();
		this.surname = actor.getSurname();
		this.photo = actor.getPhoto();
		this.email = actor.getEmail();
		this.phoneNumber = actor.getPhoneNumber();
		this.address = actor.getAddress();
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

}
