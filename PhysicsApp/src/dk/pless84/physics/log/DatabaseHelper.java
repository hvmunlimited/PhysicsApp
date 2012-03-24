package dk.pless84.physics.log;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "experiments.db";
	public static final int DB_VER = 1;

	// Database creation sql statement
	public static final String DB_CREATE = "create table experiments (_id integer primary key autoincrement, type text not null, date text not null, data text not null);";

	public DatabaseHelper(Context ctx) {
		super(ctx, DB_NAME, null, DB_VER);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DB_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
		Log.w(DatabaseHelper.class.getName(),
				"Upgrading database from version " + oldVer + " to " + newVer
						+ ", which will destroy all old data");
		db.execSQL("drop table if exists experiments");
		onCreate(db);
	}

}
