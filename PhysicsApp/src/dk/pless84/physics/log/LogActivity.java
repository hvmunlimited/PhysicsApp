package dk.pless84.physics.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import dk.pless84.physics.R;
import dk.pless84.physics.contentprovider.LogContentProvider;
import dk.pless84.physics.database.ExperimentRowTable;
import dk.pless84.physics.database.LogTable;

public class LogActivity extends ListActivity {
	private Long expId;
	private File file;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log);

		Uri allExperiments = LogContentProvider.EXPERIMENT_URI;
		SimpleCursorAdapter adapter;
		expId = getIntent().getExtras().getLong("expId");
		String[] columns = new String[] { ExperimentRowTable.COLUMN_TIME,
				ExperimentRowTable.COLUMN_XVAL, ExperimentRowTable.COLUMN_YVAL,
				ExperimentRowTable.COLUMN_ZVAL };
		int[] views = new int[] { R.id.log_time, R.id.log_xval, R.id.log_yval,
				R.id.log_zval };
		if (android.os.Build.VERSION.SDK_INT < 11) {
			// ---before Honeycomb---
			adapter = new SimpleCursorAdapter(this, R.layout.log_exp_row,
					managedQuery(allExperiments, null, "expId=" + expId, null,
							ExperimentRowTable.COLUMN_ID), columns, views, 0);
		} else {
			// ---Honeycomb and later---
			CursorLoader cursorLoader = new CursorLoader(this, allExperiments,
					null, "expId=" + expId, null, ExperimentRowTable.COLUMN_ID);
			adapter = new SimpleCursorAdapter(this, R.layout.log_exp_row,
					cursorLoader.loadInBackground(), columns, views,
					CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		}

		setListAdapter(adapter);

		file = new File(Environment.getExternalStorageDirectory(), "log "
				+ expId
				+ " "
				+ DateFormat.format("dd-MM-yyyy kk-mm-ss",
						Calendar.getInstance().getTime()).toString() + ".csv");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.logmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.log_menu_export:
			writeFileToExternalStorage();
			Intent sendIntent = new Intent(Intent.ACTION_SEND);
			sendIntent.setType("text/csv");
			sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
			startActivity(Intent.createChooser(sendIntent, "Send til"));
			return true;
		case R.id.log_menu_delete:
			getContentResolver().delete(LogContentProvider.EXPERIMENT_URI,
					ExperimentRowTable.COLUMN_EXPID + "=" + expId, null);
			getContentResolver().delete(LogContentProvider.LOGROW_URI,
					LogTable.COLUMN_ID + "=" + expId, null);
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onStop() {
		setResult(0);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		setResult(0);
		super.onDestroy();
	}

	private void writeFileToExternalStorage() {
		String seperator = "; ";
		StringBuilder sb = new StringBuilder("time" + seperator + "x"
				+ seperator + "y" + seperator + "z" + seperator + "\n");
		Cursor c = managedQuery(LogContentProvider.EXPERIMENT_URI, null,
				"expId=" + expId, null, ExperimentRowTable.COLUMN_ID + " asc");
		if (c.moveToFirst()) {
			do {
				sb.append(c.getString(c
						.getColumnIndex(ExperimentRowTable.COLUMN_TIME))
						+ seperator
						+ c.getString(c
								.getColumnIndex(ExperimentRowTable.COLUMN_XVAL))
						+ seperator
						+ c.getString(c
								.getColumnIndex(ExperimentRowTable.COLUMN_YVAL))
						+ seperator
						+ c.getString(c
								.getColumnIndex(ExperimentRowTable.COLUMN_ZVAL))
						+ seperator + "\n");
			} while (c.moveToNext());
		}
		byte[] data = sb.toString().getBytes();
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(data);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}