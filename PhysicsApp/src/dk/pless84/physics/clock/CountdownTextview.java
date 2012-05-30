package dk.pless84.physics.clock;

import android.os.Handler;
import android.widget.TextView;

public class CountdownTextview implements Runnable {
	public enum TimerState {
		STOPPED, PAUSED, RUNNING
	};

	private TextView widgetCountdown;
	private long updateInterval;
	private long time;
	private long startTime;
	private TimerState state;
	private Handler handler;

	public CountdownTextview(TextView widget, long updateInterval) {
		this.widgetCountdown = widget;
		this.updateInterval = updateInterval;
		time = 0;
		state = TimerState.STOPPED;

		handler = new Handler();
	}

	public void run() {
		time = System.currentTimeMillis();
		long millis;
		
		if((startTime - time) >0){
		millis = startTime - time;
		}else{
		millis = 0;
		}
		
		long seconds = (millis / 1000);

		widgetCountdown.setText(String.format("%02d:%02d.%03d", seconds / 60,
				seconds % 60, millis % 1000));

		if (state == TimerState.RUNNING) {
			handler.postDelayed(this, updateInterval);
		}
	}

	/**
	 * Sets the timer into a running state and initialises all time values.
	 */
	public void start() {
		startTime = time = System.currentTimeMillis();
		state = TimerState.RUNNING;

		handler.post(this);
	}

	/**
	 * Puts the timer into a paused state.
	 */
	public void pause() {
		handler.removeCallbacks(this);

		state = TimerState.PAUSED;
	}

	/**
	 * Resumes the timer.
	 */
	public void resume() {
		state = TimerState.RUNNING;

		handler.post(this);
	}
	
	public void reset() {
		pause();
		state = TimerState.STOPPED;
		time = 0;
	}

	public void plustime(long time) {
		startTime = startTime + time;

	}

	public void minustime(long time) {
		if (startTime - time > 0) {
			startTime = startTime - time;
		} else {
			startTime = 0;
		}

	}

	/**
	 * Returns the interval (in ms) at which the timer widget is updated.
	 * 
	 * @return Time in milliseconds
	 */
	public long getUpdateInterval() {
		return updateInterval;
	}

	/**
	 * Sets the update interval for the timer widget.
	 * 
	 * @param updateInterval
	 *            Interval in milliseconds
	 */
	public void setUpdateInterval(long updateInterval) {
		this.updateInterval = updateInterval;
	}

	/**
	 * Returns the current state of the stop-watch.
	 * 
	 * @return State of stop-watch
	 */
	public TimerState getState() {
		return state;
	}
}
