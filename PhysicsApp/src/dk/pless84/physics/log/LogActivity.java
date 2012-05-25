package dk.pless84.physics.log;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import dk.pless84.physics.R;

public class LogActivity extends ListActivity {
	private DatabaseManager dbmgr;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log);

		dbmgr = new DatabaseManager(this);

		final List<Experiment> experiments = dbmgr.getAllExperiments();
		final List<ExpLog> logs = dbmgr.getAllLogs(1);
		Log.e("LogExp2", logs.toString());

		//ArrayAdapter<Experiment> adapter = new ArrayAdapter<Experiment>(this, R.layout.log_row, R.id.log_type, experiments);
		setListAdapter(new BaseAdapter() {
			
			public View getView(int position, View convertView, ViewGroup parent) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.log_exp_row, null);
				TextView txtV = (TextView) view.findViewById(R.id.log_id);
				txtV.setText(experiments.get(position).getId()+"");
				txtV = (TextView) view.findViewById(R.id.log_type);
				txtV.setText(experiments.get(position).getType());
				txtV = (TextView) view.findViewById(R.id.log_date);
				txtV.setText(experiments.get(position).getDate());
				return view;
			}
			
			public long getItemId(int position) {
				return position;
			}
			
			public Experiment getItem(int position) {
				return experiments.get(position);
			}
			
			public int getCount() {
				return experiments.size();
			}
		});
		
		getListView().setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent(getApplicationContext(), SingleListItem.class);
				i.putExtra("expId", experiments.get(position).getId());
				startActivity(i);
			}
		});
		
		
	}
}