package at.markusvieghofer.shiftcalendar.activities;

import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import at.markusvieghofer.shiftcalendar.R;
import at.markusvieghofer.shiftcalendar.db.daos.UserDAO;
import at.markusvieghofer.shiftcalendar.models.User;
import at.markusvieghofer.shiftcalendar.models.api.Model;

import com.google.android.gms.auth.GoogleAuthUtil;

public class AccountSelectionActivity extends ListActivity {

	private AccountManager accountManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		User user = getUserFromDatabase();
		if (user.getId() == User.INVALID_USER_ID) {
			login();
		} else {
			switchToMainActivity(user);
		}
	}

	private String[] getAccounts() {
		accountManager = AccountManager.get(this);
		Account[] accounts = accountManager
				.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
		String[] names = new String[accounts.length];
		for (int i = 0; i < names.length; i++) {
			names[i] = accounts[i].name;
		}
		return names;
	}

	private User getUserFromDatabase() {
		User user = new User();
		UserDAO userDAO = new UserDAO(getApplicationContext());
		List<? extends Model> readAll = userDAO.readAll();
		if (readAll.size() > 0) {
			Model model = readAll.get(0);
			if (model instanceof User) {
				user = (User) model;
			}
		}
		return user;
	}

	private void login() {
		String[] accounts = getAccounts();

		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_list_item_1, accounts);
		setListAdapter(arrayAdapter);
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				User user = new User();
				user.setEmail(arrayAdapter.getItem(position));
				switchToMainActivity(user);
			}
		});
	}

	private void switchToMainActivity(User user) {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(User.KEY, user);
		intent.putExtras(bundle);
		startActivity(intent);
	}
}
