/**
 * 
 */
package at.markusvieghofer.shiftcalendar.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import at.markusvieghofer.shiftcalendar.R;
import at.markusvieghofer.shiftcalendar.fragments.CalendarFragment;
import at.markusvieghofer.shiftcalendar.fragments.TypeFragment;

/**
 * @author markusvieghofer
 * 
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragments = new ArrayList<Fragment>();
	private List<Integer> pageTitles = new ArrayList<Integer>();
	private Context context;

	public SectionsPagerAdapter(FragmentManager fm, Context context) {
		super(fm);
		this.context = context;
		initFragments();
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = fragments.get(position);
		return fragment;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return context.getString(pageTitles.get(position));
	}

	private void initFragments() {
		fragments.add(new TypeFragment());
		fragments.add(new CalendarFragment());
		fragments.add(new TypeFragment());

		pageTitles.add(R.string.title_type);
		pageTitles.add(R.string.title_date);
		pageTitles.add(R.string.title_summary);
	}

}
