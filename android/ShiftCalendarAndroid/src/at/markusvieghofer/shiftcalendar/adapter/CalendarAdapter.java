package at.markusvieghofer.shiftcalendar.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import at.markusvieghofer.shiftcalendar.R;
import at.markusvieghofer.shiftcalendar.fragments.api.CalendarSelectionListener;
import at.markusvieghofer.shiftcalendar.models.GoogleCalendar;
import at.markusvieghofer.shiftcalendar.views.CalendarView;
import at.markusvieghofer.shiftcalendar.views.TypeEntryView;

public class CalendarAdapter extends ArrayAdapter<GoogleCalendar> {

    private Map<Long, CalendarView> views = new HashMap<Long, CalendarView>();
    private CalendarSelectionListener calendarSelectionListener;

    public CalendarAdapter(Context context, List<GoogleCalendar> calendars,
            CalendarSelectionListener calendarSelectionListener) {
        super(context, R.layout.view_calendar, calendars);
        this.calendarSelectionListener = calendarSelectionListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CalendarView view = null;
        GoogleCalendar calendar = getItem(position);
        if (convertView != null && convertView instanceof TypeEntryView) {
            view = (CalendarView) convertView;
        } else if (views.get(calendar.getId()) != null) {
            view = views.get(calendar.getId());
        } else {
            view = new CalendarView(getContext(), calendar, this);
        }
        view.setCalendarSelectionListener(calendarSelectionListener);
        views.put(calendar.getId(), view);
        return view;
    }

    public void setActiveCalendar(GoogleCalendar calendar) {
        for (CalendarView calendarView : views.values()) {
            if (!calendarView.getCalendar().getId().equals(calendar.getId())) {
                calendarView.setChecked(false);
            }
        }
    }
}
