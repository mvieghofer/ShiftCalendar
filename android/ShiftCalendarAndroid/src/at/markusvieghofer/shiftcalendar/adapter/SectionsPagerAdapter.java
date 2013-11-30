/**
 * 
 */
package at.markusvieghofer.shiftcalendar.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import at.markusvieghofer.shiftcalendar.R;
import at.markusvieghofer.shiftcalendar.fragments.CalendarSelectionFragment;
import at.markusvieghofer.shiftcalendar.fragments.DateSelectionFragment;
import at.markusvieghofer.shiftcalendar.fragments.TypeFragment;
import at.markusvieghofer.shiftcalendar.fragments.api.CalendarSelectionListener;
import at.markusvieghofer.shiftcalendar.models.GoogleCalendar;
import at.markusvieghofer.shiftcalendar.models.User;

/**
 * @author markusvieghofer
 * 
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter implements
        CalendarSelectionListener {

    private List<Fragment> fragments = new ArrayList<Fragment>();
    private List<Integer> pageTitles = new ArrayList<Integer>();
    private Context context;
    private User user;
    private GoogleCalendar calendar;
    private TypeFragment typeFragment;
    private CalendarSelectionFragment calendarSelectionFragment;
    private DateSelectionFragment dateSelectionFragment;
    private List<Date> dates = new ArrayList<Date>();

    public SectionsPagerAdapter(FragmentManager fm, Context context, User user) {
        super(fm);
        this.context = context;
        initFragments();
        this.setUser(user);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public List<Date> getDates() {
        return dates;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragments.get(position);
        if (fragment == dateSelectionFragment) {
            dateSelectionFragment.setDates(getDates());
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(pageTitles.get(position));
    }

    public void resetDates() {
        dates.clear();
        dateSelectionFragment.resetDates();
    }

    public void setDates(List<Date> dates) {
        this.dates = dates;
    }

    public void setUser(User user) {
        calendarSelectionFragment.setUser(user);
    }

    @Override
    public void updateCalendar(GoogleCalendar calendar) {
        dateSelectionFragment.setCalendar(calendar);
    }

    private void initFragments() {
        typeFragment = new TypeFragment();
        calendarSelectionFragment = new CalendarSelectionFragment();
        dateSelectionFragment = new DateSelectionFragment();

        fragments.add(typeFragment);
        fragments.add(calendarSelectionFragment);
        fragments.add(dateSelectionFragment);

        pageTitles.add(R.string.title_type);
        pageTitles.add(R.string.title_calendar_selction);
        pageTitles.add(R.string.title_date);
    }
}
