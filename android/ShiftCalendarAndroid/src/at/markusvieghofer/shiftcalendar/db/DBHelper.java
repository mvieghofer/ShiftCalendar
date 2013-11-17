package at.markusvieghofer.shiftcalendar.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import at.markusvieghofer.shiftcalendar.db.daos.TypeDAO;
import at.markusvieghofer.shiftcalendar.db.daos.UserDAO;

public class DBHelper extends SQLiteOpenHelper {
	private static final int DB_VERSION = 4;
	private static final String DB_NAME = "shiftCal.db";

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		dropTable(db);
		createTable(db);
	}

	private void createTable(SQLiteDatabase db) {
		db.execSQL(TypeDAO.CREATE_TABLE);
		db.execSQL(UserDAO.CREATE_TABLE);
	}

	private void dropTable(SQLiteDatabase db) {
		db.execSQL(TypeDAO.DROP_TABLE);
		db.execSQL(UserDAO.DROP_TABLE);
	}

}
