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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import dk.pless84.physics.R;

public class SingleListItem extends ListActivity {
	private DatabaseManager dbmgr;
	private long expId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log_single_experiment);

		expId = getIntent().getLongExtra("expId", 0);
		dbmgr = new DatabaseManager(this);

		final List<ExpLog> logs = dbmgr.getAllLogs(expId);

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.addSubMenu(Menu.NONE, 0, Menu.NONE, "Exporter Log");
		menu.addSubMenu(Menu.NONE, 1, Menu.NONE, "Slet Log");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			Uri uri = dbmgr.genCSVFile(getApplicationContext(), expId);
			
			Intent i = new Intent(Intent.ACTION_SEND);
			i.putExtra(Intent.EXTRA_STREAM, uri);
			i.setType("application/csv");
			try {
				startActivity(Intent.createChooser(i, "Exporter fil"));
			} catch (ActivityNotFoundException e) {
				e.printStackTrace();
			}
			break;
		case 1:
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Slet Log?");
			alertDialog.setMessage(getText(R.string.log_dialog));
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dbmgr.deleteExperiment(expId);
					dbmgr.deleteExpLog(expId);
					setResult(1);
					finish();
				}
			});
			alertDialog.setButton2("Annuller", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});
			alertDialog.show();
			break;
		}
		return true;
	}
}
