package dk.pless84.physics.log;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import dk.pless84.physics.R;

public class SingleListItem extends ListActivity {
	private DatabaseManager dbmgr;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log_single_experiment);

		long expId = getIntent().getLongExtra("expId", 0);
		dbmgr = new DatabaseManager(this);

		final List<ExpLog> logs = dbmgr.getAllLogs(expId);
		Log.e("LogExp", logs.toString());

		// ArrayAdapter<ExpLog> adapter = new ArrayAdapter<ExpLog>(this,
		// R.layout.log_row, R.id.log_type, logs);
		setListAdapter(new BaseAdapter() {

			public View getView(int position, View convertView, ViewGroup parent) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.log_explog_row, null);
				TextView txtV = (TextView) view.findViewById(R.id.log_time);
				txtV.setText(logs.get(position).getTime());
				txtV = (TextView) view.findViewById(R.id.log_xVal);
				txtV.setText(logs.get(position).getxVal() + "");
				txtV = (TextView) view.findViewById(R.id.log_yVal);
				txtV.setText(logs.get(position).getyVal() + "");
				txtV = (TextView) view.findViewById(R.id.log_zVal);
				txtV.setText(logs.get(position).getzVal() + "");
				return view;
			}

			public long getItemId(int position) {
				return position;
			}

			public ExpLog getItem(int position) {
				return logs.get(position);
			}

			public int getCount() {
				return logs.size();
			}
		});

	}
}
