package at.markusvieghofer.shiftcalendar.fragments;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import at.markusvieghofer.shiftcalendar.R;
import at.markusvieghofer.shiftcalendar.activities.api.DateSelectionListener;
import at.markusvieghofer.shiftcalendar.models.GoogleCalendar;

import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.CalendarPickerView.OnDateSelectedListener;
import com.squareup.timessquare.CalendarPickerView.SelectionMode;

public class DateSelectionFragment extends Fragment {

    private GoogleCalendar calendar;

    String[] projection = new String[] { Events._ID, Events.TITLE,
            Events.DTSTART, Events.DTEND };

    private Calendar calDate;

    private CalendarPickerView calendarView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container,
                false);
        initView(view);
        if (calendar != null) {
            initCalendarEvents();
        }
        return view;
    }

    public void resetDates() {
        initCalendarEvents();
    }

    public void setCalendar(GoogleCalendar calendar) {
        this.calendar = calendar;
        if (getActivity() != null) {
            initCalendarEvents();
        }
    }

    public void setDates(List<Date> dates) {
        for (Date date : dates) {
            calendarView.selectDate(date);
        }
    }

    private Calendar getEventEndDate() {
        Calendar endDate = getEventStartDate();
        endDate.add(Calendar.MONTH, 1);
        return endDate;
    }

    private Calendar getEventStartDate() {
        Calendar startDate = Calendar.getInstance();
        int month = calDate.get(Calendar.MONTH);
        int year = calDate.get(Calendar.YEAR);
        startDate.set(year, month, 1, 0, 0, 0);
        return startDate;
    }

    private void initCalendarEvents() {
        calendarView.init(getEventStartDate().getTime(),
                getEventEndDate().getTime()).inMode(SelectionMode.MULTIPLE);
        // if (calendar != null) {
        // ContentResolver contentResolver = getActivity()
        // .getContentResolver();
        // Uri uri = Events.CONTENT_URI;
        // String selection = Events.CALENDAR_ID + " = ? AND "
        // + Events.DTSTART + " >= ? AND " + Events.DTEND + " <= ?";
        // Calendar startDate = getEventStartDate();
        //
        // Calendar endDate = getEventEndDate();
        //
        // String[] selectionArgs = new String[] {
        // String.valueOf(calendar.getId()),
        // String.valueOf(startDate.getTime().getTime()),
        // String.valueOf(endDate.getTime().getTime()) };
        // Cursor cursor = contentResolver.query(uri, projection, selection,
        // selectionArgs, null);
        // while (cursor.moveToNext()) {
        // System.out.println(cursor.getString(1));
        // }
        // cursor.close();
        // }
    }

    private void initView(View view) {
        calendarView = (CalendarPickerView) view.findViewById(R.id.calendar);
        calendarView.setOnDateSelectedListener(new OnDateSelectedListener() {

            @Override
            public void onDateSelected(Date date) {
                FragmentActivity activity = getActivity();
                if (activity instanceof DateSelectionListener) {
                    DateSelectionListener listener = (DateSelectionListener) activity;
                    listener.addDate(date);
                }

            }

            @Override
            public void onDateUnselected(Date date) {
                FragmentActivity activity = getActivity();
                if (activity instanceof DateSelectionListener) {
                    DateSelectionListener listener = (DateSelectionListener) activity;
                    listener.removeDate(date);
                }
            }
        });

        calDate = Calendar.getInstance();
        calDate.setTime(new Date());

        initCalendarEvents();

        Button btnNextMonth = (Button) view.findViewById(R.id.btnNextMonth);
        btnNextMonth.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                calDate.add(Calendar.MONTH, 1);
                initCalendarEvents();
            }
        });

        Button btnPrevMonth = (Button) view.findViewById(R.id.btnPrevMonth);
        btnPrevMonth.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                calDate.add(Calendar.MONTH, -1);
                initCalendarEvents();
            }
        });
    }
}
