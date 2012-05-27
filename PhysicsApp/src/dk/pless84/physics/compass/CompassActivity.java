package dk.pless84.physics.compass;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import dk.pless84.physics.R;

public class CompassActivity extends Activity implements SensorEventListener {

	public static final float ALPHA = 0.2f;

	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private Sensor mField;
	private TextView valueView;
	private TextView directionView;

	private float[] mGravity;
	private float[] mMagnetic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compass);

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		valueView = (TextView) findViewById(R.id.values);
		directionView = (TextView) findViewById(R.id.direction);
	}

	@Override
	protected void onResume() {
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
		// Return the orientation values
		float[] values = new float[3];
		SensorManager.getOrientation(R, values);
		// Convert to degrees
		for (int i = 0; i < values.length; i++) {
			Double degrees = (values[i] * 180) / Math.PI;
			values[i] = degrees.floatValue();
		}
		// Display the compass direction
		directionView.setText(getDirectionFromDegrees(values[0]));
		// Display the raw values
		valueView.setText(String.format(
				"Azimuth: %1$1.2f, Pitch: %2$1.2f, Roll: %3$1.2f", values[0],
				values[1], values[2]));
	}

	private String getDirectionFromDegrees(float degrees) {
		if (degrees >= -22.5 && degrees < 22.5) {
			return "N";
		}
		if (degrees >= 22.5 && degrees < 67.5) {
			return "NE";
		}
		if (degrees >= 67.5 && degrees < 112.5) {
			return "E";
		}
		if (degrees >= 112.5 && degrees < 157.5) {
			return "SE";
		}
		if (degrees >= 157.5 && degrees < -157.5) {
			return "S";
		}
		if (degrees >= -157.5 && degrees < -112.5) {
			return "SW";
		}
		if (degrees >= -112.5 && degrees < -67.5) {
			return "W";
		}
		if (degrees >= -67.5 && degrees < -22.5) {
			return "NW";
		}

		return null;
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
		// TODO Auto-generated method stub
	}

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