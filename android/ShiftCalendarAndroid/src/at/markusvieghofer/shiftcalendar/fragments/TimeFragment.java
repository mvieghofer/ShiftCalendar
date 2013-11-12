package at.markusvieghofer.shiftcalendar.fragments;

import java.util.Calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import at.markusvieghofer.shiftcalendar.fragments.api.TimeChangeListener;

public class TimeFragment extends DialogFragment implements
		TimePickerDialog.OnTimeSetListener {

	public static final String CALENDAR = "calendar";
	public static final String TIME_CHANGE_LISTENER = "timeChangeListener";
	private TimeChangeListener timeChangeListener;

	public TimeFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle arguments = getArguments();
		Calendar cal = null;
		if (arguments.getSerializable(CALENDAR) != null
				&& arguments.getSerializable(CALENDAR) instanceof Calendar) {
			cal = (Calendar) arguments.getSerializable(CALENDAR);
		} else {
			cal = Calendar.getInstance();
		}

		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		if (arguments.getSerializable(TIME_CHANGE_LISTENER) != null
				&& arguments.getSerializable(TIME_CHANGE_LISTENER) instanceof TimeChangeListener) {
			timeChangeListener = (TimeChangeListener) arguments
					.getSerializable(TIME_CHANGE_LISTENER);
		}
		return new TimePickerDialog(getActivity(), this, hour, minute,
				DateFormat.is24HourFormat(getActivity()));
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		timeChangeListener.timeChanged(getTag(), hourOfDay, minute);
	}
}
