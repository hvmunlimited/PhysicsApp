package dk.pless84.physics.clock;

import android.os.Handler;
import android.widget.TextView;

public class StopwatchTextView implements Runnable {
	public enum TimerState {
		STOPPED, PAUSED, RUNNING
	};

	private TextView widget;
	private long updateInterval;
	private long time;
	private long startTime;
	private TimerState state;
	private Handler handler;

	public StopwatchTextView(TextView widget, long updateInterval) {
		this.widget = widget;
		this.updateInterval = updateInterval;
		time = 0;
		startTime = 0;
		state = TimerState.STOPPED;

		handler = new Handler();
	}

	public void run() {
		time = System.currentTimeMillis();
		long millis = time - startTime;
		long seconds = (long) (millis / 1000);

		widget.setText(String.format("%02d:%02d.%03d", seconds / 60,
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
	 * Resets the timer.
	 */
	public void reset() {
		start();
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

		startTime = System.currentTimeMillis() - (time - startTime);

		handler.post(this);
	}

	/**
	 * Stops the timer and resets all time values.
	 */
	public void stop() {
		handler.removeCallbacks(this);

		time = 0;
		startTime = 0;
		state = TimerState.STOPPED;

		widget.setText("00:00.000");
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