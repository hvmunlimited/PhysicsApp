package dk.pless84.physics.log;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import dk.pless84.physics.R;

public class ExperimentDetails extends Activity {
	private EditText mTitleText;
	private EditText mBodyText;
	private Long mRowId;
	private DbAdapter mDbHelper;
	private Spinner mType;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		mDbHelper = new DbAdapter(this);
		mDbHelper.open();
		setContentView(R.layout.log_edit);
		mType = (Spinner) findViewById(R.id.type);
		mTitleText = (EditText) findViewById(R.id.log_edit_date);
		mBodyText = (EditText) findViewById(R.id.log_edit_data);

		Button confirmButton = (Button) findViewById(R.id.log_edit_button);
		mRowId = null;
		Bundle extras = getIntent().getExtras();
		mRowId = (bundle == null) ? null : (Long) bundle
				.getSerializable(DbAdapter.KEY_ROWID);
		if (extras != null) {
			mRowId = extras.getLong(DbAdapter.KEY_ROWID);
		}
		populateFields();
		confirmButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				setResult(RESULT_OK);
				finish();
			}
		});
	}

	private void populateFields() {
		if (mRowId != null) {
			Cursor exp = mDbHelper.fetchExperiments(mRowId);
			startManagingCursor(exp);
			String type = exp.getString(exp
					.getColumnIndexOrThrow(DbAdapter.KEY_TYPE));

			for (int i = 0; i < mType.getCount(); i++) {
				String str = (String) mType.getItemAtPosition(i);
				Log.e(null, str + " " + type);
				if (str.equalsIgnoreCase(type)) {
					mType.setSelection(i);
				}
			}
			
			mTitleText.setText(exp.getString(exp.getColumnIndexOrThrow(DbAdapter.KEY_DATE)));
			mBodyText.setText(exp.getString(exp.getColumnIndexOrThrow(DbAdapter.KEY_DATA)));
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveState();
	}

	@Override
	protected void onResume() {
		super.onResume();
		populateFields();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putSerializable(DbAdapter.KEY_ROWID, mRowId);
	}
	
	private void saveState() {
		String type = (String) mType.getSelectedItem();
		String date = mTitleText.getText().toString();
		String data = mBodyText.getText().toString();
		
		if (mRowId == null) {
			long id = mDbHelper.createExperiment(type, date, data);
			if (id > 0) {
				mRowId = id;
			}
		} else {
			mDbHelper.updateExperiment(mRowId, type, date, data);
		}
	}
}