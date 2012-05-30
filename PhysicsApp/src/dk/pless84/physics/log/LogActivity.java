package dk.pless84.physics.log;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import dk.pless84.physics.R;

public class LogActivity extends ListActivity {
	private DatabaseManager dbmgr;
	private BaseAdapter adapter;
	private int requestCode;
	private int lastPosition;
	private List<Experiment> experiments;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log);

		dbmgr = new DatabaseManager(this);

		experiments = dbmgr.getAllExperiments();

		adapter = new BaseAdapter() {

			public View getView(int position, View convertView, ViewGroup parent) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.log_exp_row, null);

				TextView txtV = (TextView) view.findViewById(R.id.log_date);
				txtV.setText(experiments.get(position).getDate());
				txtV = (TextView) view.findViewById(R.id.log_rate);
				txtV.setText(experiments.get(position).getRate() + "ms");

				ImageView imgV = (ImageView) view.findViewById(R.id.log_type);
				switch ((int) experiments.get(position).getType()) {
				case Experiment.TYPE_ACC:
					imgV.setImageResource(R.drawable.acc_s);
					break;
				case Experiment.TYPE_MAGNET:
					imgV.setImageResource(R.drawable.magnet_s);
					break;
				}
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
				startActivityForResult(i, requestCode);
				lastPosition = position;
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 1:
			experiments.remove(lastPosition);
			adapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}
}