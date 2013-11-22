package at.markusvieghofer.shiftcalendar.models;

import java.io.Serializable;
import java.util.Calendar;

import at.markusvieghofer.shiftcalendar.models.api.Model;

public class Type implements Model, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1495214082127315619L;
	public static final String KEY = "Type";
	private String name;
	private Calendar from;
	private Calendar to;
	private long id;

	public Type() {
	}

	public Type(String name, Calendar from, Calendar to) {
		this.name = name;
		this.from = from;
		this.to = to;
	}

	public Calendar getFrom() {
		return from;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Calendar getTo() {
		return to;
	}

	public void setFrom(Calendar from) {
		this.from = from;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTo(Calendar to) {
		this.to = to;
	}

	@Override
	public String toString() {
		return name;
	}
}
