package at.markusvieghofer.shiftcalendar.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import at.markusvieghofer.shiftcalendar.db.daos.UserDAO;
import at.markusvieghofer.shiftcalendar.models.User;
import at.markusvieghofer.shiftcalendar.models.api.Model;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		User user = getUserFromDatabase();
		if (user.getId() != -1) {
			login();
		} else {
			switchToMainActivity(user);
		}
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
		// TODO Auto-generated method stub

	}

	private void switchToMainActivity(User user) {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(User.KEY, user);
		startActivity(intent);
	}
}
