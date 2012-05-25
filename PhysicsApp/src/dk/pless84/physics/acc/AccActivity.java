package dk.pless84.physics.acc;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import dk.pless84.physics.R;
import dk.pless84.physics.log.DatabaseManager;

public class AccActivity extends Activity implements SensorEventListener {
	private TextView mAccX;
	private TextView mAccY;
	private TextView mAccZ;

	private Timer mTimer;
	private TimerTask mTimerTask;

	private SensorManager sensorManager;
	private DatabaseManager dbMgr;

	private float xVal;
	private float yVal;
	private float zVal;
	private long rowId;
	private boolean isStop;
	private long rate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acc);

		isStop = false;

		mAccX = (TextView) findViewById(R.id.accX);
		mAccY = (TextView) findViewById(R.id.accY);
		mAccZ = (TextView) findViewById(R.id.accZ);

		xVal = 0;
		yVal = 0;
		zVal = 0;
		
		rate = 50;

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);

		dbMgr = new DatabaseManager(this);
	}

	public void startLog(View v) {
		Button btn = (Button) v;
		if (isStop) {
			stopTimer();
			btn.setText(R.string.start_logging);
			isStop = false;
		} else {
			rowId = dbMgr.addExperiment("Acc", rate);
			startTimer();
			btn.setText(R.string.stop_logging);
			isStop = true;
		}
	}
	
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			xVal = event.values[0];
			yVal = event.values[1];
			zVal = event.values[2];
			mAccX.setText(xVal + "");
			mAccY.setText(yVal + "");
			mAccZ.setText(zVal + "");
		}
	}

	private void startTimer() {
		if (mTimer == null) {
			mTimer = new Timer();
		}
		if (mTimerTask == null) {
			mTimerTask = new TimerTask() {
				@Override
				public void run() {
					dbMgr.addLogRow(rowId, xVal, yVal, zVal);
				}
			};
		}
		if (mTimer != null && mTimerTask != null)
			mTimer.schedule(mTimerTask, 0, rate);
	}

	private void stopTimer() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
	}
}