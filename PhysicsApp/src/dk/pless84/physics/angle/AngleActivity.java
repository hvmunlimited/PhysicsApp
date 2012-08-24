package dk.pless84.physics.angle;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import dk.pless84.physics.R;

public class AngleActivity extends Activity implements SensorEventListener {

	public static final float ALPHA = 0.2f;

	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private TextView valueView;
	private TextView directionView;

	private float[] mGravity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.angle);

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		valueView = (TextView) findViewById(R.id.angleValues);
		directionView = (TextView) findViewById(R.id.angle);
		
		mGravity = new float[3];
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mAccelerometer,
				SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	private void updateDirection() {
		float[] R = new float[9];
		// Load rotation matrix into R
		SensorManager.getRotationMatrix(R, null, mGravity, null);
		// Remap coordinates
		SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, R);
		// Return the orientation values
		float[] values = new float[3];
		SensorManager.getOrientation(R, values);
		
		// Convert to degrees
		for (int i = 0; i < values.length; i++) {
			Double degrees = (values[i] * 180) / Math.PI;
			values[i] = degrees.floatValue();
		}
		
		directionView.setText(values[1] + "");
		// Display the raw values
		valueView.setText(String.format(
				"Azimuth: %1$1.2f, Pitch: %2$1.2f, Roll: %3$1.2f", values[0],
				values[1], values[2]));
	}

	private float[] lowPass(float[] input, float[] output) {
		if (output == null) {
			return input;
		}

		for (int i = 0; i < input.length; i++) {
			output[i] = output[i] + ALPHA * (input[i] - output[i]);
		}
		return output;
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			mGravity = lowPass(event.values.clone(), mGravity);
		}

		if (mGravity != null) {
			updateDirection();
		}
	}
}