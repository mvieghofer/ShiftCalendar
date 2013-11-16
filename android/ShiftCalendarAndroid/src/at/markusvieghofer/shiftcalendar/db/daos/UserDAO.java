package at.markusvieghofer.shiftcalendar.db.daos;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import at.markusvieghofer.shiftcalendar.R;
import at.markusvieghofer.shiftcalendar.db.DBHelper;
import at.markusvieghofer.shiftcalendar.db.daos.api.DAO;
import at.markusvieghofer.shiftcalendar.models.User;
import at.markusvieghofer.shiftcalendar.models.api.Model;

public class UserDAO implements DAO {

	private static final long INVALID_USER_ID = -1L;
	private static final String TABLE_NAME = "user";
	private static final String COL_ID = "id";
	private static final String COL_FIRSTNAME = "firstName";
	private static final String COL_LASTNAME = "lastName";
	private static final String COL_EMAIL = "email";

	public static final String CREATE_TABLE = "";
	private DBHelper dbHelper;
	private Context context;

	public UserDAO(Context context) {
		this.context = context;
		dbHelper = new DBHelper(context);
	}

	public Long getUserId(Model model) {
		if (model instanceof User) {
			User user = (User) model;
			SQLiteDatabase database = dbHelper.getReadableDatabase();
			String selection = COL_FIRSTNAME + " = ? AND " + COL_LASTNAME
					+ " = ? AND " + COL_EMAIL + " = ?";
			String[] selectionArgs = new String[] { user.getFirstName(),
					user.getLastName(), user.getEmail() };
			Cursor cursor = database.query(TABLE_NAME, new String[] { COL_ID },
					selection, selectionArgs, null, null, null);
			Long id = INVALID_USER_ID;
			if (cursor.moveToFirst()) {
				id = cursor.getLong(0);
			}
			return id;
		} else {
			throw new IllegalArgumentException(
					context.getString(R.string.passed_model_not_of_type_user));
		}
	}

	@Override
	public List<? extends Model> readAll() {
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		String[] columns = new String[] { COL_ID, COL_FIRSTNAME, COL_LASTNAME,
				COL_EMAIL };
		Cursor cursor = database.query(TABLE_NAME, columns, null, null, null,
				null, null);

		List<User> users = new ArrayList<User>();
		while (cursor.moveToNext()) {
			User user = new User();
			user.setId(cursor.getLong(0));
			user.setFirstName(cursor.getString(1));
			user.setLastName(cursor.getString(2));
			user.setEmail(cursor.getString(3));
			users.add(user);
		}
		return users;
	}

	@Override
	public long save(Model model) {
		if (model instanceof User) {
			User user = (User) model;
			Long userId = getUserId(model);
			SQLiteDatabase database = dbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(COL_FIRSTNAME, user.getFirstName());
			values.put(COL_LASTNAME, user.getLastName());
			values.put(COL_EMAIL, user.getEmail());
			if (userId == INVALID_USER_ID) {
				userId = database.insert(TABLE_NAME, null, values);
			} else {
				String whereClause = COL_ID + " = ?";
				String[] whereArgs = new String[] { String.valueOf(userId) };
				database.update(TABLE_NAME, values, whereClause, whereArgs);
			}
			return userId;
		} else {
			throw new IllegalArgumentException(
					context.getString(R.string.passed_model_not_of_type_user));
		}
	}
}
