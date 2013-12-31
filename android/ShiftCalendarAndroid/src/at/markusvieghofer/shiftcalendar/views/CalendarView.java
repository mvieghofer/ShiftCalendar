package at.markusvieghofer.shiftcalendar.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import at.markusvieghofer.shiftcalendar.R;
import at.markusvieghofer.shiftcalendar.adapter.CalendarAdapter;
import at.markusvieghofer.shiftcalendar.fragments.api.CalendarSelectionListener;
import at.markusvieghofer.shiftcalendar.models.GoogleCalendar;

public class CalendarView extends LinearLayout {

    private GoogleCalendar calendar;
    private CheckedTextView rbCalendar;
    private CalendarSelectionListener calendarSelectionListener;
    private CalendarAdapter calendarAdapter;

    public CalendarView(Context context, GoogleCalendar calendar,
            CalendarAdapter calendarAdapter) {
        super(context);
        this.calendar = calendar;
        this.calendarAdapter = calendarAdapter;
        initView(context);
    }

    public GoogleCalendar getCalendar() {
        return calendar;
    }

    public void setCalendarSelectionListener(
            CalendarSelectionListener calendarSelectionListener) {
        this.calendarSelectionListener = calendarSelectionListener;
    }

    public void setChecked(boolean checked) {
        rbCalendar.setChecked(checked);
    }

    public void toggle() {
        rbCalendar.toggle();
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_calendar, this);
        rbCalendar = (CheckedTextView) findViewById(R.id.rbCalendar);
        rbCalendar.setText(calendar.getDisplayName());
        rbCalendar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!rbCalendar.isChecked()) {
                    // this calendar was clicked
                    calendarSelectionListener.updateCalendar(calendar);
                    calendarAdapter.setActiveCalendar(calendar);
                }
                rbCalendar.toggle();
            }
        });
    }
}
