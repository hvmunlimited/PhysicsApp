package dk.pless84.physics.log;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DbAdapter {

	// Database fields
	public static final String KEY_ROWID = "_id";
	public static final String KEY_TYPE = "type";
	public static final String KEY_DATE = "date";
	public static final String KEY_DATA = "data";
	public static final String DB_TABLE = "experiments";

	private Context ctx;
	private SQLiteDatabase db;
	private DatabaseHelper dbHelper;

	public DbAdapter(Context ctx) {
		this.ctx = ctx;
	}

	public DbAdapter open() throws SQLException {
		dbHelper = new DatabaseHelper(ctx);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	/**
	 * Create a new experiment, if the experiment is succesfully created return
	 * the new rowId Else return a -1 to indicate failure.
	 */
	public long createExperiment(String type, String date, String data) {
		ContentValues initialValues = createContentValues(type, date, data);

		return db.insert(DB_TABLE, null, initialValues);
	}

	/**
	 * Update an experiment
	 */
	public boolean updateExperiment(long rowId, String type, String date,
			String data) {
		ContentValues updateValues = createContentValues(type, date, data);

		return db.update(DB_TABLE, updateValues, KEY_ROWID + "=" + rowId, null) > 0;
	}

	/**
	 * Delete experiment
	 */
	public boolean deleteExperiment(long rowId) {
		return db.delete(DB_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	/**
	 * Return a Cursor over the list of all experiment in the database
	 * 
	 * @return Cursor over all experiments
	 */
	public Cursor fetchAllExperiments() {
		return db.query(DB_TABLE, new String[] { KEY_ROWID, KEY_TYPE, KEY_DATE,
				KEY_DATA }, null, null, null, null, null);
	}

	/**
	 * Return a Cursor positioned at the defined experiment
	 */
	public Cursor fetchExperiments(long rowId) throws SQLException {
		Cursor mCursor = db.query(true, DB_TABLE, new String[] { KEY_ROWID,
				KEY_TYPE, KEY_DATE, KEY_DATA }, KEY_ROWID + "=" + rowId, null,
				null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	private ContentValues createContentValues(String type, String date,
			String data) {
		ContentValues values = new ContentValues();
		values.put(KEY_TYPE, type);
		values.put(KEY_DATE, date);
		values.put(KEY_DATA, data);
		return values;
	}
}