package at.markusvieghofer.shiftcalendar.activities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import at.markusvieghofer.shiftcalendar.R;
import at.markusvieghofer.shiftcalendar.activities.api.DateSelectionListener;
import at.markusvieghofer.shiftcalendar.adapter.SectionsPagerAdapter;
import at.markusvieghofer.shiftcalendar.fragments.ErrorDialogFragment;
import at.markusvieghofer.shiftcalendar.fragments.api.CalendarSelectionListener;
import at.markusvieghofer.shiftcalendar.models.GoogleCalendar;
import at.markusvieghofer.shiftcalendar.models.Type;
import at.markusvieghofer.shiftcalendar.models.User;

public class MainActivity extends FragmentActivity implements
        ActionBar.TabListener, CalendarSelectionListener, DateSelectionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    private GoogleCalendar calendar = null;
    private Type type = null;

    @Override
    public void addDate(Date date) {
        getDates().add(date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = true;
        switch (item.getItemId()) {
        case R.id.apply:
            applyDates();
            break;
        default:
            result = super.onOptionsItemSelected(item);
        }
        return result;
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab,
            FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab,
            FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab,
            FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void removeDate(Date date) {
        getDates().add(date);
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public void updateCalendar(GoogleCalendar calendar) {
        this.calendar = calendar;
        mSectionsPagerAdapter.updateCalendar(calendar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        Serializable userSer = extras.getSerializable(User.KEY);
        User user = null;
        if (userSer instanceof User) {
            user = (User) userSer;
        } else {
            throw new IllegalArgumentException(
                    "Passed object is not an instance of User");
        }

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setHomeButtonEnabled(true);

        mSectionsPagerAdapter = new SectionsPagerAdapter(
                getSupportFragmentManager(), getApplicationContext(), user);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager
                .setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        actionBar.setSelectedNavigationItem(position);
                    }
                });

        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab()
                    .setText(mSectionsPagerAdapter.getPageTitle(i))
                    .setTabListener(this));
        }
    }

    private void applyDates() {
        if (type == null || calendar == null || getDates().size() == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(ErrorDialogFragment.KEY,
                    getString(R.string.error_applying_dates));
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();
            errorFragment.setArguments(bundle);
            errorFragment.show(getSupportFragmentManager(), "ERROR");
        } else {
            ContentResolver contentResolver = getContentResolver();
            for (Date date : getDates()) {
                ContentValues values = new ContentValues();
                values.put(Events.CALENDAR_ID, calendar.getId());
                values.put(Events.TITLE, type.getName());
                values.put(Events.DTSTART, buildStartDate(date));
                values.put(Events.DTEND, buildEndDate(date));
                values.put(Events.ALL_DAY, 0);
                values.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
                contentResolver.insert(Events.CONTENT_URI, values);
            }
            resetDates();
            Toast.makeText(getApplicationContext(), R.string.events_added,
                    Toast.LENGTH_LONG).show();
        }

    }

    private Calendar buildDate(Calendar typeCal, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, typeCal.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, typeCal.get(Calendar.MINUTE));
        return calendar;
    }

    private long buildEndDate(Date date) {
        Calendar calendar = buildDate(type.getTo(), date);
        if (type.getFrom().after(type.getTo())) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        return calendar.getTimeInMillis();
    }

    private long buildStartDate(Date date) {
        return buildDate(type.getFrom(), date).getTimeInMillis();
    }

    private List<Date> getDates() {
        return mSectionsPagerAdapter.getDates();
    }

    private void resetDates() {
        mSectionsPagerAdapter.resetDates();
    }

}
