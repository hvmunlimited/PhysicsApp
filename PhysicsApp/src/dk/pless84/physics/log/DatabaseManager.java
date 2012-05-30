package dk.pless84.physics.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class DatabaseManager {
	private CustomSQLiteOpenHelper helper;

	public static final String DB_NAME = "physicsapp.db";
	public static final int DB_VERSION = 2;

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
		helper = new CustomSQLiteOpenHelper(context);
	}

	public long addExperiment(long type, long rate) {
		SQLiteDatabase db = helper.getWritableDatabase();
		long rowId = -1;
		SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		String date = s.format(new Date());

		ContentValues values = new ContentValues();
		values.put(TABLE_ROW_TYPE, type);
		values.put(TABLE_ROW_DATE, date);
		values.put(TABLE_ROW_RATE, rate);

		try {
			rowId = db.insert(TABLE_NAME_EXPERIMENTS, null, values);
		} catch (SQLException e) {
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
		db.close();
		return rowId;
	}

	public void addLogRow(long expId, float x, float y, float z) {
		SQLiteDatabase db = helper.getWritableDatabase();

		ContentValues values = new ContentValues();

		SimpleDateFormat s = new SimpleDateFormat("hh:mm:ss.SSS");
		String time = s.format(new Date());

		values.put(TABLE_ROW_EXPID, expId);
		values.put(TABLE_ROW_TIME, time);
		values.put(TABLE_ROW_XVAL, x);
		values.put(TABLE_ROW_YVAL, y);
		values.put(TABLE_ROW_ZVAL, z);

		try {
			db.insert(TABLE_NAME_LOGS, null, values);
		} catch (SQLException e) {
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
		db.close();
	}

	public void deleteExperiment(long rowID) {
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			db.delete(TABLE_NAME_EXPERIMENTS, TABLE_ROW_ID + "=" + rowID, null);
		} catch (SQLException e) {
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
		db.close();
	}

	public void deleteExpLog(long expId) {
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			db.delete(TABLE_NAME_LOGS, TABLE_ROW_EXPID + "=" + expId, null);
		} catch (SQLException e) {
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
		db.close();
	}

	public List<Experiment> getAllExperiments() {
		SQLiteDatabase db = helper.getWritableDatabase();
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
		cursor.close();
		db.close();
		return experiments;
	}
	
	public Experiment getExperiment(long id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		Experiment experiment = new Experiment();

		Cursor cursor = db.query(TABLE_NAME_EXPERIMENTS, new String[] {
				TABLE_ROW_ID, TABLE_ROW_TYPE, TABLE_ROW_DATE, TABLE_ROW_RATE },
				TABLE_ROW_ID + "=" + id, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			experiment = cursorToExperiment(cursor);
			cursor.moveToNext();
		}
		cursor.close();
		db.close();
		return experiment;
	}

	private Experiment cursorToExperiment(Cursor cursor) {
		Experiment experiment = new Experiment();
		experiment.setId(cursor.getLong(0));
		experiment.setType(cursor.getLong(1));
		experiment.setDate(cursor.getString(2));
		experiment.setRate(cursor.getLong(3));
		return experiment;
	}

	public List<ExpLog> getAllLogs(long expId) {
		SQLiteDatabase db = helper.getWritableDatabase();
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
		cursor.close();
		db.close();
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

	public Uri genCSVFile(Context context, long id) {
		List<ExpLog> list = getAllLogs(id);
		File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + getExperiment(id).getType() + " " + getExperiment(id).getDate() + ".csv");
		try {
			FileOutputStream writer = new FileOutputStream(file);
			
			writer.write(("time,x,y,z\n").getBytes());
			for (int i = 0; i < list.size(); i++) {
				ExpLog log = list.get(i);
				String str = log.getTime() + "," + log.getxVal() + ","
						+ log.getyVal() + "," + log.getzVal() + "\n";
				writer.write(str.getBytes());
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Uri.fromFile(file);
	}
	
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
					+ TABLE_ROW_TYPE + " long," + TABLE_ROW_DATE + " text,"
					+ TABLE_ROW_RATE + " long" + ");");
			db.execSQL("create table " + TABLE_NAME_LOGS + " (" + TABLE_ROW_ID
					+ " integer primary key autoincrement not null,"
					+ TABLE_ROW_EXPID + " text," + TABLE_ROW_TIME + " text,"
					+ TABLE_ROW_XVAL + " real," + TABLE_ROW_YVAL + " real,"
					+ TABLE_ROW_ZVAL + " real" + ");");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("drop table if exists " + TABLE_NAME_EXPERIMENTS + ";");
			db.execSQL("drop table if exists " + TABLE_NAME_LOGS + ";");
			onCreate(db);
		}
	}
}
