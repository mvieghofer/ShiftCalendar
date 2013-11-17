package at.markusvieghofer.shiftcalendar.models;

import java.io.Serializable;

import at.markusvieghofer.shiftcalendar.models.api.Model;

public class User implements Model, Serializable {
	public static final long INVALID_USER_ID = -1L;
	private static final long serialVersionUID = 1195992695904424703L;
	public static final String KEY = "User";
	private Long id = INVALID_USER_ID;
	private String firstName = "";
	private String lastName = "";
	private String email = "";

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public Long getId() {
		return id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
