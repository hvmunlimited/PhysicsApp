package Countdown;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import dk.pless84.physics.R;
import dk.pless84.physics.clock.StopwatchTextView.TimerState;

public class CountdownActivity extends Activity {
	private TextView mChron;
	private CountdownTextView countdownwatch;
	private Button mStartpauseresume;
	private Button mStop;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.countdown);

		mChron = (TextView) findViewById(R.id.countdowntime);
		countdownwatch = new CountdownTextView(mChron, 100);
		mStartpauseresume = (Button) findViewById(R.id.startcountdownbt);
		mStop = (Button) findViewById(R.id.stopcountdownbt);
	}

	public void startChronometer(View view) {
		if (countdownwatch.getState().equals(TimerState.STOPPED)) {
			countdownwatch.start();
			mStartpauseresume.setText("Pause");
		} else if (countdownwatch.getState().equals(TimerState.RUNNING)) {
			countdownwatch.pause();
			mStartpauseresume.setText("Resume");
		} else {
			countdownwatch.resume();
			mStartpauseresume.setText("Pause");
		}
	}

	public void pauseChronometer(View view) {
		countdownwatch.pause();
	}

	public void stopChronometer(View view) {
		countdownwatch.stop();
		mStartpauseresume.setText("Start");
	}

	public void resetChronometer(View view) {
		countdownwatch.reset();
	}

	public void resumeChronometer(View view) {
		countdownwatch.resume();
	}

}