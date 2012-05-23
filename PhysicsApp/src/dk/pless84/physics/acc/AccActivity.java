package dk.pless84.physics.acc;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import dk.pless84.physics.R;
import dk.pless84.physics.log.DatabaseManager;

public class AccActivity extends Activity implements SensorEventListener {
	private TextView mAccX;
	private TextView mAccY;
	private TextView mAccZ;

	private SensorManager sensorManager;
	private DatabaseManager dbMgr;
	
	private float xVal;
	private float yVal;
	private float zVal;
	private long rowId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acc);

		mAccX = (TextView) findViewById(R.id.accX);
		mAccY = (TextView) findViewById(R.id.accY);
		mAccZ = (TextView) findViewById(R.id.accZ);
		
		xVal = 0;
		yVal = 0;
		zVal = 0;

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		
		dbMgr = new DatabaseManager(this);
		rowId = dbMgr.addExperiment("Acc");
		
		final Handler handler = new Handler(); 
        Timer t = new Timer(); 
        t.schedule(new TimerTask() { 
                public void run() { 
                        handler.post(new Runnable() { 
                                public void run() { 
                                        killListener(); 
                                        showDialog(DIALOG_DELAY); 
                                } 
                        }); 
                } 
        }, 30000);
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
	
	private Runnable logTask = new Runnable() {
		public void run() {
			dbMgr.addLogRow(rowId, xVal, yVal, zVal);
		}
	};
}