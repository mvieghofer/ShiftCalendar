package at.markusvieghofer.shiftcalendar.db.daos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import at.markusvieghofer.shiftcalendar.db.DBHelper;
import at.markusvieghofer.shiftcalendar.db.daos.api.DAO;
import at.markusvieghofer.shiftcalendar.models.Type;
import at.markusvieghofer.shiftcalendar.models.api.Model;

public class TypeDAO implements DAO {
	public static final String TABLE_NAME = "Type";
	public static final String COL_ID = "id";
	public static final String COL_NAME = "name";
	public static final String COL_FROM = "from_time";
	public static final String COL_TO = "to_time";

	public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
			+ " ( " + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COL_NAME + " VARCHAR NOT NULL, " + COL_FROM
			+ " INTEGER NOT NULL, " + COL_TO + " INTEGER NOT NULL )";
	public static final String DROP_TABLE = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;
	private DBHelper dbHelper;

	public TypeDAO(Context context) {
		dbHelper = new DBHelper(context);
	}

	@Override
	public List<? extends Model> readAll() {
		List<Type> models = new ArrayList<Type>();
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		Cursor cursor = database.query(TABLE_NAME, new String[] { COL_ID,
				COL_NAME, COL_FROM, COL_TO }, null, null, null, null, null);

		while (cursor.moveToNext()) {
			Type type = new Type();
			type.setId(cursor.getLong(0));
			type.setName(cursor.getString(1));
			Calendar from = Calendar.getInstance();
			from.setTime(new Date(cursor.getInt(2)));
			type.setFrom(from);
			Calendar to = Calendar.getInstance();
			to.setTime(new Date(cursor.getInt(3)));
			type.setFrom(to);

			models.add(type);
		}
		return models;
	}

	@Override
	public long save(Model model) {
		if (model instanceof Type) {
			Type type = (Type) model;
			SQLiteDatabase database = dbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(COL_NAME, type.getName());
			values.put(COL_FROM, type.getFrom().getTime().getTime());
			values.put(COL_TO, type.getTo().getTime().getTime());
			return database.insert(TABLE_NAME, null, values);
		} else {
			throw new IllegalArgumentException(
					"Model passed is not of type 'Type'");
		}
	}
}
