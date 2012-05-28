package dk.pless84.physics.compass;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

public class CompassActivity extends Activity implements SensorEventListener {

	private SensorManager mSensorManager;
	private Rose rose;
	public static final float ALPHA = 0.2f;

	private Sensor mAccelerometer;
	private Sensor mField;

	private float[] mGravity;
	private float[] mMagnetic;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Create new instance of custom Rose and set it on the screen
		rose = new Rose(this);
		setContentView(rose);
		
		// Get sensor manager and sensor
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mAccelerometer,
				SensorManager.SENSOR_DELAY_UI);
		mSensorManager.registerListener(this, mField,
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
		SensorManager.getRotationMatrix(R, null, mGravity, mMagnetic);
		// Remap coordinates
		//SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, R);
		// Return the orientation values
		float[] values = new float[3];
		SensorManager.getOrientation(R, values);
		// Convert to degrees
		for (int i = 0; i < values.length; i++) {
			Double degrees = (values[i] * 180) / Math.PI;
			values[i] = degrees.floatValue();
		}
		rose.setDirection(values[0]);
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
	
	// Ignore accuracy changes
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
	
	// Listen to sensor and provide output
	public void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			mGravity = lowPass(event.values.clone(), mGravity);
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			mMagnetic = lowPass(event.values.clone(), mMagnetic);
			break;
		default:
			return;
		}

		if (mGravity != null && mMagnetic != null) {
			updateDirection();
		}
	}
}