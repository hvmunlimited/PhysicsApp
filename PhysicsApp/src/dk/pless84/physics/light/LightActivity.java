package dk.pless84.physics.light;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import dk.pless84.physics.R;

public class LightActivity extends Activity implements SensorEventListener {
	private TextView mLightX;
	private TextView mLightMax;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.light);

		mLightX = (TextView) findViewById(R.id.lightX);
		mLightMax = (TextView) findViewById(R.id.lightMax);

		SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

		mLightMax.setText("Max: "
				+ String.valueOf(lightSensor.getMaximumRange()));

		sensorManager.registerListener(this, lightSensor,
				SensorManager.SENSOR_DELAY_FASTEST);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
			float currentReading = event.values[0];
			mLightX.setText(String.valueOf(currentReading));
		}
	}
}