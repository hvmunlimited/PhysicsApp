package dk.pless84.physics.clock;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import dk.pless84.physics.R;

public class ClockActivity extends ListActivity {
	private Handler mHandler = new Handler();
	private long startTime;
	private long elapsedTime;
	private int state; // 1 - Initial, 2 - Timer running, 3 - Reset after pause
	private List<String> laps;
	private ClockArrayAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clock);

		state = 1;
		laps = new ArrayList<String>();
		adapter = new ClockArrayAdapter(this, laps);
		setListAdapter(adapter);
	}

	public void onClick(View view) {
		switch (state) {
		case 1:
			if (view.getId() == R.id.startButton) {
				// Start time, switch to pause/lap
				startTimer(System.nanoTime());
			} else {
				// Reset
				((TextView) findViewById(R.id.timer))
						.setText(getString(R.string.timer));
				adapter.clear();
				laps.clear();
			}
			break;
		case 2:
			if (view.getId() == R.id.startButton) {
				// Pause time, switch to resume/reset
				((Button) findViewById(R.id.startButton))
						.setText(R.string.resumeText);
				((Button) findViewById(R.id.resetButton))
						.setText(R.string.resetText);
				mHandler.removeCallbacks(startTimer);
				state = 3;
			} else {
				// Add lap
				laps.add(updateTimer(elapsedTime));
				adapter.notifyDataSetChanged();
			}
			break;
		case 3:
			if (view.getId() == R.id.startButton) {
				// Resume time
				startTimer(System.nanoTime() - elapsedTime);
			} else {
				// Reset
				((Button) findViewById(R.id.startButton))
						.setText(R.string.startText);
				((TextView) findViewById(R.id.timer))
						.setText(getString(R.string.timer));
				adapter.clear();
				laps.clear();
				state = 1;
			}
			break;
		}
	}

	private void startTimer(long time) {
		((Button) findViewById(R.id.startButton)).setText(R.string.pauseText);
		((Button) findViewById(R.id.resetButton)).setText(R.string.lapText);
		startTime = time;
		mHandler.removeCallbacks(startTimer);
		mHandler.post(startTimer);
		state = 2;
	}

	private Runnable startTimer = new Runnable() {
		
		public void run() {
			elapsedTime = System.nanoTime() - startTime;

			((TextView) findViewById(R.id.timer))
					.setText(updateTimer(elapsedTime));
			mHandler.postDelayed(this, 10);
		}
	};

	private String updateTimer(long time) {
		/*
		 * Convert the seconds to String and format to ensure it has a leading
		 * zero when required
		 */
		long secs = (time / 1000000000) % 60;
		String seconds = String.valueOf(secs);
		if (secs == 0) {
			seconds = "00";
		}
		if (secs < 10 && secs > 0) {
			seconds = "0" + seconds;
		}

		/* Convert the minutes to String and format the String */
		long mins = ((time / 1000000000) / 60) % 60;
		String minutes = String.valueOf(mins);
		if (mins == 0) {
			minutes = "00";
		}
		if (mins < 10 && mins > 0) {
			minutes = "0" + minutes;
		}

		/* Convert the hours to String and format the String */
		long hrs = (((time / 1000000000) / 60) / 60);
		String hours = String.valueOf(hrs);
		if (hrs == 0) {
			hours = "00";
		}
		if (hrs < 10 && hrs > 0) {
			hours = "0" + hours;
		}

		long msecs = time / 1000000;
		String milliseconds = String.valueOf(msecs);
		if (milliseconds.length() == 2) {
			milliseconds = "0" + milliseconds;
		}
		if (milliseconds.length() <= 1) {
			milliseconds = "00";
		}
		milliseconds = milliseconds.substring(milliseconds.length() - 3,
				milliseconds.length());

		return hours + ":" + minutes + ":" + seconds + "." + milliseconds;
	}
	
	private class ClockArrayAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final List<String> values;

		protected ClockArrayAdapter(Context context, List<String> values) {
			super(context, R.layout.clock_row, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.clock_row, parent, false);
			TextView lapTime = (TextView) rowView.findViewById(R.id.lap_time);
			TextView lapNo = (TextView) rowView.findViewById(R.id.lap_no);
			
			lapTime.setText(values.get(position));
			lapNo.setText(position + "");
			return rowView;
		}
	}
}