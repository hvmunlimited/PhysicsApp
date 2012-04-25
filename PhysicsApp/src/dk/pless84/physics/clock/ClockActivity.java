package dk.pless84.physics.clock;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import dk.pless84.physics.R;
import dk.pless84.physics.clock.StopwatchTextView.TimerState;

public class ClockActivity extends Activity {
	private TextView mChron;
	private StopwatchTextView stopWatch;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clock);

		mChron = (TextView) findViewById(R.id.time);
		stopWatch = new StopwatchTextView(mChron, 100);
	}

	public void startChronometer(View view) {
		if (stopWatch.getState().equals(TimerState.STOPPED)) {
			stopWatch.start();
		} else if (stopWatch.getState().equals(TimerState.RUNNING)) {
			stopWatch.pause();
		} else {
			stopWatch.resume();
		}
	}

	public void pauseChronometer(View view) {
		stopWatch.pause();
	}

	public void stopChronometer(View view) {
		stopWatch.stop();
	}

	public void resetChronometer(View view) {
		stopWatch.reset();
	}

	public void resumeChronometer(View view) {
		stopWatch.resume();
	}

}