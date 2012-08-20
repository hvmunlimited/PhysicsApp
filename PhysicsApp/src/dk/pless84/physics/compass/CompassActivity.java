package dk.pless84.physics.compass;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import dk.pless84.physics.R;

public class CompassActivity extends Activity implements SensorEventListener {
	private SensorManager mSensorManager;
	private Sensor mAccSensor;
	private Sensor mMagSensor;
	private CompassView mCompassView;
	private TextView directionText;
	private float[] mGravity;
	private float[] mMagnetic;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.compass);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mMagSensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		mCompassView = (CompassView) findViewById(R.id.compassView);
		directionText = (TextView) findViewById(R.id.compassDirection);
	}

	@Override
	protected void onPause() {
		// Unregister the listener event to preserve batterylife;
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mAccSensor,
				SensorManager.SENSOR_DELAY_UI);
		mSensorManager.registerListener(this, mMagSensor,
				SensorManager.SENSOR_DELAY_UI);
	}

	private void updateDirection() {
		float[] temp = new float[9];
		float[] R = new float[9];

		// Load rotation matrix into R
		SensorManager.getRotationMatrix(temp, null, mGravity, mMagnetic);

		// Map coordinate system
		SensorManager.remapCoordinateSystem(temp, SensorManager.AXIS_Y,
				SensorManager.AXIS_MINUS_X, R);

		// Return the orientation values
		float[] values = new float[3];
		SensorManager.getOrientation(R, values);

		// Convert to degrees
		for (int i = 0; i < values.length; i++) {
			Double degrees = (values[i] * 180) / Math.PI;
			values[i] = degrees.floatValue();
		}

		// Convert from -180-0-180 to 0-360
		if (values[0] < 0) {
			float t = values[0] + 360;
			values[0] = t;
		}

		float direction = Math.round(values[0]);

		// Display the compass direction
		mCompassView.setAzimuth(direction);
		directionText.setText(direction + "\u00B0");
	}

	public void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			mGravity = event.values.clone();
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			mMagnetic = event.values.clone();
			break;
		default:
			return;
		}
		if (mGravity != null && mMagnetic != null) {
			updateDirection();
		}
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
}