package dk.pless84.physics.magnet;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import dk.pless84.physics.R;

public class MagnetActivity extends Activity implements SensorEventListener {
	private TextView mMagX;
	private TextView mMagY;
	private TextView mMagZ;
	private SensorManager sensorManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.magnet);
		
		mMagX = (TextView) findViewById(R.id.magX);
		mMagY = (TextView) findViewById(R.id.magY);
		mMagZ = (TextView) findViewById(R.id.magZ);

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			mMagX.setText(event.values[0] + "");
			mMagY.setText(event.values[1] + "");
			mMagZ.setText(event.values[2] + "");
		}
	}

}