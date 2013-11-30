package at.markusvieghofer.shiftcalendar.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract.Calendars;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import at.markusvieghofer.shiftcalendar.fragments.api.CalendarSelectionListener;
import at.markusvieghofer.shiftcalendar.models.GoogleCalendar;
import at.markusvieghofer.shiftcalendar.models.User;

public class CalendarSelectionFragment extends ListFragment {
    // Projection array. Creating indices for this array instead of doing
    // dynamic lookups improves performance.
    public static final String[] EVENT_PROJECTION = new String[] {
            Calendars._ID, // 0
            Calendars.ACCOUNT_NAME, // 1
            Calendars.CALENDAR_DISPLAY_NAME, // 2
            Calendars.OWNER_ACCOUNT // 3
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    private User user;

    private ArrayAdapter<GoogleCalendar> arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (user != null) {
            initCalendars();
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getListView().setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                GoogleCalendar calendar = arrayAdapter.getItem(position);
                FragmentActivity activity = getActivity();
                if (activity instanceof CalendarSelectionListener) {
                    CalendarSelectionListener listener = (CalendarSelectionListener) activity;
                    listener.updateCalendar(calendar);
                }
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    public void setUser(User user) {
        this.user = user;
        if (getActivity() != null) {
            initCalendars();
        }
    }

    private void initCalendars() {
        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri uri = Calendars.CONTENT_URI;
        String selection = Calendars.ACCOUNT_NAME + " = ?";
        String[] selectionArgs = new String[] { user.getEmail(), };
        Cursor cursor = contentResolver.query(uri, EVENT_PROJECTION, selection,
                selectionArgs, null);
        List<GoogleCalendar> calendars = new ArrayList<GoogleCalendar>();
        while (cursor.moveToNext()) {
            GoogleCalendar calendar = new GoogleCalendar();
            calendar.setId(cursor.getLong(PROJECTION_ID_INDEX));
            calendar.setDisplayName(cursor
                    .getString(PROJECTION_DISPLAY_NAME_INDEX));
            calendar.setAccountName(cursor
                    .getString(PROJECTION_ACCOUNT_NAME_INDEX));
            calendar.setOwnerName(cursor
                    .getString(PROJECTION_OWNER_ACCOUNT_INDEX));
            calendars.add(calendar);
        }
        cursor.close();
        arrayAdapter = new ArrayAdapter<GoogleCalendar>(getActivity(),
                android.R.layout.simple_list_item_single_choice, calendars);
        setListAdapter(arrayAdapter);
    }
}
