package at.markusvieghofer.shiftcalendar.activities;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import at.markusvieghofer.shiftcalendar.db.daos.UserDAO;
import at.markusvieghofer.shiftcalendar.models.User;
import at.markusvieghofer.shiftcalendar.models.api.Model;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getUserFromDatabase();
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
}
