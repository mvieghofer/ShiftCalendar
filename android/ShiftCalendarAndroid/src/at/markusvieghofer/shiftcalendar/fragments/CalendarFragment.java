package at.markusvieghofer.shiftcalendar.fragments;

import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import at.markusvieghofer.shiftcalendar.R;

import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.CalendarPickerView.OnDateSelectedListener;
import com.squareup.timessquare.CalendarPickerView.SelectionMode;

public class CalendarFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_calendar, container,
				false);
		CalendarPickerView calendar = (CalendarPickerView) view
				.findViewById(R.id.calendar);

		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, 1);
		calendar.init(today, cal.getTime()).inMode(SelectionMode.MULTIPLE);
		calendar.setOnDateSelectedListener(new OnDateSelectedListener() {

			@Override
			public void onDateSelected(Date date) {
				Toast.makeText(
						getActivity(),
						"date: "
								+ DateFormat.getDateFormat(getActivity())
										.format(date), Toast.LENGTH_LONG)
						.show();
			}
		});
		return view;
	}
}
