package dk.pless84.physics.acc;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.drakenclimber.graph.GraphData;
import com.drakenclimber.graph.LineGraphView;

import dk.pless84.physics.R;
import dk.pless84.physics.log.DatabaseManager;
import dk.pless84.physics.log.Experiment;

public class AccActivity extends Activity implements SensorEventListener {
	private TextView mAccX;
	private TextView mAccY;
	private TextView mAccZ;
	private TextView mRateBarValue;
	private SeekBar mRateBar;

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
	
	public static final int ACCEL_DATA_COUNT = 128;

	/* widgets */
	private LineGraphView mAccelGraphView;

	/* data for the accelerometer graph */
	private GraphData mXAccelData;
	private GraphData mYAccelData;
	private GraphData mZAccelData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acc);
		
		isStop = false;
		rate = 50;

		xVal = 0;
		yVal = 0;
		zVal = 0;
		
		/* initialize the accelerometer graph and its data */
		mXAccelData = new GraphData(null, Color.RED, ACCEL_DATA_COUNT);
		mYAccelData = new GraphData(null, Color.MAGENTA, ACCEL_DATA_COUNT);
		mZAccelData = new GraphData(null, Color.GREEN, ACCEL_DATA_COUNT);

		mAccelGraphView = (LineGraphView) findViewById(R.id.accelGraph);
		mAccelGraphView.addDataSet(mXAccelData);
		mAccelGraphView.addDataSet(mYAccelData);
		mAccelGraphView.addDataSet(mZAccelData);

		mAccX = (TextView) findViewById(R.id.accX);
		mAccY = (TextView) findViewById(R.id.accY);
		mAccZ = (TextView) findViewById(R.id.accZ);
		
		mRateBarValue = (TextView) findViewById(R.id.accRateBarValue);
		mRateBar = (SeekBar) findViewById(R.id.accRateBar);

		mRateBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				progress = ((int) Math.round(progress / 50)) * 50;
				seekBar.setProgress(progress);
				mRateBarValue.setText(String.valueOf(progress) + "ms");
				rate = progress;
			}
		});

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
			rowId = dbMgr.addExperiment(Experiment.TYPE_ACC, rate);
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
			
			mXAccelData.appendValue(xVal);
			mYAccelData.appendValue(yVal);
			mZAccelData.appendValue(zVal);
			
			mAccelGraphView.invalidate();
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
	
	/** method to handle the destruction of this activity */
	@Override
	public void onDestroy() {
		stopTimer();
		sensorManager.unregisterListener(this);
		super.onDestroy();
	}
}