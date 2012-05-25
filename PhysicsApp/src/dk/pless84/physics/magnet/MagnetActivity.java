package dk.pless84.physics.magnet;

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

public class MagnetActivity extends Activity implements SensorEventListener {
	private TextView mMagX;
	private TextView mMagY;
	private TextView mMagZ;

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
		setContentView(R.layout.magnet);

		isStop = false;

		mMagX = (TextView) findViewById(R.id.magX);
		mMagY = (TextView) findViewById(R.id.magY);
		mMagZ = (TextView) findViewById(R.id.magZ);

		xVal = 0;
		yVal = 0;
		zVal = 0;
		
		rate = 50;

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
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
			rowId = dbMgr.addExperiment("Mag", rate);
			startTimer();
			btn.setText(R.string.stop_logging);
			isStop = true;
		}
	}
	
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			xVal = event.values[0];
			yVal = event.values[1];
			zVal = event.values[2];
			mMagX.setText(xVal + "");
			mMagY.setText(yVal + "");
			mMagZ.setText(zVal + "");
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