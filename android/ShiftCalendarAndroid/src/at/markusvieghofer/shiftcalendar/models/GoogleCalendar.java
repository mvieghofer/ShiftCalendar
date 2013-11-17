package at.markusvieghofer.shiftcalendar.models;

import java.io.Serializable;

import at.markusvieghofer.shiftcalendar.models.api.Model;

public class GoogleCalendar implements Model, Serializable {
	private static final long serialVersionUID = 1221239658633915899L;

	public static final String KEY = "GoogleCalendar";

	private Long id;
	private String displayName;
	private String accountName;
	private String ownerName;

	public String getAccountName() {
		return accountName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public Long getId() {
		return id;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	@Override
	public String toString() {
		return displayName;
	}
}
