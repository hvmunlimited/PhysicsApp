package dk.pless84.physics.log;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import dk.pless84.physics.R;

public class LogActivity extends ListActivity {
	private DatabaseManager dbmgr;
	private BaseAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log);

		dbmgr = new DatabaseManager(this);

		final List<Experiment> experiments = dbmgr.getAllExperiments();

		adapter = new BaseAdapter() {

			public View getView(int position, View convertView, ViewGroup parent) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.log_exp_row, null);
				TextView txtV = (TextView) view.findViewById(R.id.log_id);
				txtV.setText(experiments.get(position).getId() + "");
				txtV = (TextView) view.findViewById(R.id.log_type);
				txtV.setText(experiments.get(position).getType());
				txtV = (TextView) view.findViewById(R.id.log_date);
				txtV.setText(experiments.get(position).getDate());
				txtV = (TextView) view.findViewById(R.id.log_rate);
				txtV.setText(experiments.get(position).getRate() + "ms");
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
		};

		setListAdapter(adapter);

		getListView().setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(getApplicationContext(),
						SingleListItem.class);
				i.putExtra("expId", experiments.get(position).getId());
				startActivity(i);
			}
		});

		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				AlertDialog.Builder adb = new AlertDialog.Builder(
						LogActivity.this);
				adb.setTitle("Slet eller Gem?");
				adb.setMessage(getString(R.string.log_dialog));
				final int pos = position;
				adb.setNeutralButton("Gem", new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Experiment exp = experiments.get(pos);
						Uri uri = dbmgr.genCSVFile(getApplicationContext(), exp);
						
						Intent i = new Intent();
						i.setAction(Intent.ACTION_SEND);
						i.putExtra(Intent.EXTRA_STREAM, uri);
						i.setType("application/csv");
						try {
							startActivity(Intent.createChooser(i, "Title"));
						} catch (ActivityNotFoundException e) {
							e.printStackTrace();
						}
					}
				});
				adb.setNegativeButton(getString(R.string.cancel), null);
				adb.setPositiveButton("Slet", new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						long expId = experiments.get(pos).getId();
						dbmgr.deleteExperiment(expId);
						dbmgr.deleteExpLog(expId);
						experiments.remove(pos);
						adapter.notifyDataSetChanged();
					}
				});
				adb.show();
				
				return true;
			}
		});
	}
}