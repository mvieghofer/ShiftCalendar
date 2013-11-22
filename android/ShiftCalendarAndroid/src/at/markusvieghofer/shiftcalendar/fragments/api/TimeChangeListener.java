package at.markusvieghofer.shiftcalendar.fragments.api;

public interface TimeChangeListener {
	static final String FROM = "from";
	static final String TO = "to";

	void cancelTimeChange();

	void timeChanged(String type, int hour, int minute);
}
