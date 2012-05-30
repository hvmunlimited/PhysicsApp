package dk.pless84.physics.clock;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import dk.pless84.physics.R;
import dk.pless84.physics.clock.StopwatchTextView.TimerState;

public class ClockActivity extends Activity {
	private TextView mChron;
	private TextView mCountdown;
	private StopwatchTextView stopWatch;
	private CountdownTextview countdown;
	private Button mSWStartpauseresume;
	private Button mSWStop;
	private Button mCDStart;
	private Button mCDStop;
	private Button mCDReset;
	private Button mCDPlusminute;
	private Button mCDPlussecond;
	private Button mCDMinusminute;
	private Button mCDMinussecond;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clock);

		mChron = (TextView) findViewById(R.id.time);
		stopWatch = new StopwatchTextView(mChron, 100);
		mSWStartpauseresume = (Button) findViewById(R.id.startclockbt);
		mSWStop = (Button) findViewById(R.id.stopclockbt);

		mCountdown = (TextView) findViewById(R.id.countdowntime);
		countdown = new CountdownTextview(mCountdown, 100);
		mCDStart = (Button) findViewById(R.id.startcountdownbt);
		mCDReset = (Button) findViewById(R.id.resetcountdownbt);
		mCDPlusminute = (Button) findViewById(R.id.plusminutecountdownbt);
		mCDPlussecond = (Button) findViewById(R.id.plussecondcountdownbt);
		mCDMinusminute = (Button) findViewById(R.id.minusminutecountdownbt);
		mCDMinussecond = (Button) findViewById(R.id.minussecondcountdownbt);

	}

	public void startChronometer(View view) {
		if (stopWatch.getState().equals(TimerState.STOPPED)) {
			stopWatch.start();
			mSWStartpauseresume.setText("Pause");
		} else if (stopWatch.getState().equals(TimerState.RUNNING)) {
			stopWatch.pause();
			mSWStartpauseresume.setText("Resume");
		} else {
			stopWatch.resume();
			mSWStartpauseresume.setText("Pause");
		}
	}

	public void pauseChronometer(View view) {
		stopWatch.pause();
	}

	public void stopChronometer(View view) {
		stopWatch.stop();
		mSWStartpauseresume.setText("Start");
	}

	public void resumeChronometer(View view) {
		stopWatch.resume();
	}

	public void startCountdown(View view) {
		if (countdown.getState().equals(TimerState.STOPPED)) {
			countdown.start();
			mCDStart.setText("Start");
		} else if (countdown.getState().equals(TimerState.RUNNING)) {
			countdown.pause();
			mCDStart.setText("Resume");

		} else {
			countdown.resume();
			mCountdown.setText("Pause");
		}

	}

	public void reset(View view) {
		countdown.reset();
	}

	public void plusMinute(View view) {
		countdown.plustime(60000);
	}

	public void minusMinute(View view) {
		countdown.minustime(60000);
	}

	public void plusSecond(View view) {
		countdown.plustime(1000);
	}

	public void minusSecond(View view) {
		countdown.minustime(1000);
	}

}