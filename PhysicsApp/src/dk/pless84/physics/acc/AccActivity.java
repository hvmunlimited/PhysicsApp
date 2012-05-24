package dk.pless84.physics.acc;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import dk.pless84.physics.R;

public class AccActivity extends Activity implements SensorEventListener {
	private TextView mAccX;
	private TextView mAccY;
	private TextView mAccZ;

	private SensorManager sensorManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acc);

		mAccX = (TextView) findViewById(R.id.accX);
		mAccY = (TextView) findViewById(R.id.accY);
		mAccZ = (TextView) findViewById(R.id.accZ);

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			mAccX.setText(event.values[0] + "");
			mAccY.setText(event.values[1] + "");
			mAccZ.setText(event.values[2] + "");
		}
	}
}