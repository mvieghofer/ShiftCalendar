package at.markusvieghofer.shiftcalendar.activities;

import java.io.Serializable;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import at.markusvieghofer.shiftcalendar.R;
import at.markusvieghofer.shiftcalendar.adapter.SectionsPagerAdapter;
import at.markusvieghofer.shiftcalendar.fragments.api.CalendarSelectionListener;
import at.markusvieghofer.shiftcalendar.models.GoogleCalendar;
import at.markusvieghofer.shiftcalendar.models.User;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener, CalendarSelectionListener {

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
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
	public void updateCalendar(GoogleCalendar calendar) {
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

}
