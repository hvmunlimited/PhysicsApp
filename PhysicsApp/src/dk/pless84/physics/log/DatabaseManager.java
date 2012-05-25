package dk.pless84.physics.log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseManager {
	// the Activity or Application that is creating an object from this class.
	Context context;

	// a reference to the database used by this application/object
	private SQLiteDatabase db;

	// These constants are specific to the database. They should be
	// changed to suit your needs.
	public static final String DB_NAME = "physicsapp.db";
	public static final int DB_VERSION = 1;

	// These constants are specific to the database table. They should be
	// changed to suit your needs.
	public static final String TABLE_NAME_EXPERIMENTS = "experiments";
	public static final String TABLE_ROW_ID = "id_";
	public static final String TABLE_ROW_TYPE = "type";
	public static final String TABLE_ROW_DATE = "date";
	public static final String TABLE_ROW_RATE = "rate";

	public static final String TABLE_NAME_LOGS = "logs";
	public static final String TABLE_ROW_EXPID = "expId";
	public static final String TABLE_ROW_TIME = "time";
	public static final String TABLE_ROW_XVAL = "xVal";
	public static final String TABLE_ROW_YVAL = "yVal";
	public static final String TABLE_ROW_ZVAL = "zVal";

	public DatabaseManager(Context context) {
		this.context = context;

		// create or open the database
		CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(context);
		db = helper.getWritableDatabase();
	}

	/**********************************************************************
	 * ADDING A ROW TO THE DATABASE TABLE
	 * 
	 * This is an example of how to add a row to a database table using this
	 * class. You should edit this method to suit your needs.
	 * 
	 * the key is automatically assigned by the database
	 * 
	 * @param type
	 *            the value for the row's first column
	 * @param date
	 *            the value for the row's second column
	 */
	public long addExperiment(String type, long rate) {
		long rowId = -1;
		// get the current date
		SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		String date = s.format(new Date());

		// this is a key value pair holder used by android's SQLite functions
		ContentValues values = new ContentValues();
		values.put(TABLE_ROW_TYPE, type);
		values.put(TABLE_ROW_DATE, date);
		values.put(TABLE_ROW_RATE, rate);

		// ask the database object to insert the new data
		try {
			rowId = db.insert(TABLE_NAME_EXPERIMENTS, null, values);
		} catch (SQLException e) {
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
		return rowId;
	}

	/**********************************************************************
	 * ADDING A ROW TO THE DATABASE TABLE
	 * 
	 * This is an example of how to add a row to a database table using this
	 * class. You should edit this method to suit your needs.
	 * 
	 * the key is automatically assigned by the database
	 * 
	 * @param type
	 *            the value for the row's first column
	 * @param date
	 *            the value for the row's second column
	 */
	public void addLogRow(long expId, float x, float y, float z) {
		// this is a key value pair holder used by android's SQLite functions
		ContentValues values = new ContentValues();

		SimpleDateFormat s = new SimpleDateFormat("hh:mm:ss.SSS");
		String time = s.format(new Date());

		values.put(TABLE_ROW_EXPID, expId);
		values.put(TABLE_ROW_TIME, time);
		values.put(TABLE_ROW_XVAL, x);
		values.put(TABLE_ROW_YVAL, y);
		values.put(TABLE_ROW_ZVAL, z);

		// ask the database object to insert the new data
		try {
			db.insert(TABLE_NAME_LOGS, null, values);
		} catch (SQLException e) {
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
	}

	public void deleteExperiment(long rowID) {
		// ask the database manager to delete the row of given id
		try {
			db.delete(TABLE_NAME_EXPERIMENTS, TABLE_ROW_ID + "=" + rowID, null);
		} catch (SQLException e) {
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
	}

	public void deleteExpLog(long expId) {
		// ask the database manager to delete the row of given id
		try {
			db.delete(TABLE_NAME_LOGS, TABLE_ROW_EXPID + "=" + expId, null);
		} catch (SQLException e) {
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
	}

	public List<Experiment> getAllExperiments() {
		List<Experiment> experiments = new ArrayList<Experiment>();

		Cursor cursor = db.query(TABLE_NAME_EXPERIMENTS, new String[] {
				TABLE_ROW_ID, TABLE_ROW_TYPE, TABLE_ROW_DATE, TABLE_ROW_RATE },
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Experiment experiment = cursorToExperiment(cursor);
			experiments.add(experiment);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return experiments;
	}

	private Experiment cursorToExperiment(Cursor cursor) {
		Experiment experiment = new Experiment();
		experiment.setId(cursor.getLong(0));
		experiment.setType(cursor.getString(1));
		experiment.setDate(cursor.getString(2));
		experiment.setRate(cursor.getLong(3));
		return experiment;
	}

	public List<ExpLog> getAllLogs(long expId) {
		List<ExpLog> logs = new ArrayList<ExpLog>();

		Cursor cursor = db.query(TABLE_NAME_LOGS, new String[] { TABLE_ROW_ID,
				TABLE_ROW_EXPID, TABLE_ROW_TIME, TABLE_ROW_XVAL,
				TABLE_ROW_YVAL, TABLE_ROW_ZVAL },
				TABLE_ROW_EXPID + "=" + expId, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ExpLog log = cursorToLog(cursor);
			logs.add(log);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return logs;
	}

	private ExpLog cursorToLog(Cursor cursor) {
		ExpLog log = new ExpLog();
		log.setId(cursor.getLong(0));
		log.setExpId(cursor.getLong(1));
		log.setTime(cursor.getString(2));
		log.setxVal(cursor.getFloat(3));
		log.setyVal(cursor.getFloat(4));
		log.setzVal(cursor.getFloat(5));
		return log;
	}

	public void closeDb() {
		if (db.isOpen()) {
			db.close();
		}
	}

	/**********************************************************************
	 * THIS IS THE BEGINNING OF THE INTERNAL SQLiteOpenHelper SUBCLASS.
	 * 
	 * I MADE THIS CLASS INTERNAL SO I CAN COPY A SINGLE FILE TO NEW APPS AND
	 * MODIFYING IT - ACHIEVING DATABASE FUNCTIONALITY. ALSO, THIS WAY I DO NOT
	 * HAVE TO SHARE CONSTANTS BETWEEN TWO FILES AND CAN INSTEAD MAKE THEM
	 * PRIVATE AND/OR NON-STATIC. HOWEVER, I THINK THE INDUSTRY STANDARD IS TO
	 * KEEP THIS CLASS IN A SEPARATE FILE.
	 *********************************************************************/

	/**
	 * This class is designed to check if there is a database that currently
	 * exists for the given program. If the database does not exist, it creates
	 * one. After the class ensures that the database exists, this class will
	 * open the database for use. Most of this functionality will be handled by
	 * the SQLiteOpenHelper parent class. The purpose of extending this class is
	 * to tell the class how to create (or update) the database.
	 * 
	 * @author Randall Mitchell
	 * 
	 */
	private class CustomSQLiteOpenHelper extends SQLiteOpenHelper {
		public CustomSQLiteOpenHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// This string is used to create the database. It should
			// be changed to suit your needs.
			db.execSQL("create table " + TABLE_NAME_EXPERIMENTS + " ("
					+ TABLE_ROW_ID
					+ " integer primary key autoincrement not null,"
					+ TABLE_ROW_TYPE + " text," + TABLE_ROW_DATE + " text,"
					+ TABLE_ROW_RATE + " long" + ");");
			db.execSQL("create table " + TABLE_NAME_LOGS + " (" + TABLE_ROW_ID
					+ " integer primary key autoincrement not null,"
					+ TABLE_ROW_EXPID + " text," + TABLE_ROW_TIME + " text,"
					+ TABLE_ROW_XVAL + " real," + TABLE_ROW_YVAL + " real,"
					+ TABLE_ROW_ZVAL + " real" + ");");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}
	}
}
