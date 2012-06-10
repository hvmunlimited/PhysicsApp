package dk.pless84.physics.compass;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.jwetherell.compass.data.GlobalData;


/**
 * This class extends Activity and processes sensor data and location data.
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class SensorActivity extends Activity implements SensorEventListener {
    public static final Logger logger = Logger.getLogger(SensorActivity.class.getSimpleName());    
    public static final AtomicBoolean computing = new AtomicBoolean(false); 
    
    public static final float grav[] = new float[3]; //Gravity (a.k.a accelerometer data)
    public static final float mag[] = new float[3]; //Magnetic 
    public static final float rotation[] = new float[9]; //Rotation matrix in Android format
    public static final float inclination[] = new float[9]; //Inclination matrix
    public static final float orientation[] = new float[3]; //yaw, pitch, roll
    
    private static int bearingIdx = 0;
    public static final float[] bearingArray = new float[3];

    private static SensorManager sensorMgr = null;
    private static List<Sensor> sensors = null;
    private static Sensor sensorGrav = null;
    private static Sensor sensorMag = null;

    private static int bearing = 0;
    private static float floatBearing = 0;
    private static float floatSmoothedBearing = 0;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        
        try {
            sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);

            sensors = sensorMgr.getSensorList(Sensor.TYPE_ACCELEROMETER);
            if (sensors.size() > 0) sensorGrav = sensors.get(0);

            sensors = sensorMgr.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
            if (sensors.size() > 0) sensorMag = sensors.get(0);

            sensorMgr.registerListener(this, sensorGrav, SensorManager.SENSOR_DELAY_UI);
            sensorMgr.registerListener(this, sensorMag, SensorManager.SENSOR_DELAY_UI);
        } catch (Exception ex1) {
            try {
                if (sensorMgr != null) {
                    sensorMgr.unregisterListener(this, sensorGrav);
                    sensorMgr.unregisterListener(this, sensorMag);
                    sensorMgr = null;
                }
            } catch (Exception ex2) {
                logger.info("Exception: "+ex2);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        try {
            try {
                sensorMgr.unregisterListener(this, sensorGrav);
            } catch (Exception ex) {
                logger.info("Exception: "+ex);
            }
            try {
                sensorMgr.unregisterListener(this, sensorMag);
            } catch (Exception ex) {
                logger.info("Exception: "+ex);
            }
            sensorMgr = null;
        } catch (Exception ex) {
            logger.info("Exception: "+ex);
        }
    }
    
    public void onSensorChanged(SensorEvent evt) {
    	if (!computing.compareAndSet(false, true)) return;
    	
        if (evt.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            grav[0] = evt.values[0];
            grav[1] = evt.values[1];
            grav[2] = evt.values[2];
        } else if (evt.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mag[0] = evt.values[0];
            mag[1] = evt.values[1];
            mag[2] = evt.values[2];
        }

        //Get rotation and inclination matrices given the gravity and geomagnetic matrices
        SensorManager.getRotationMatrix(rotation, inclination, grav, mag);
        SensorManager.getOrientation(rotation, orientation);
        floatBearing = orientation[0];

        int smoothCnt = 0;
        float smooth = 0f;
        for (int i = 0; i < bearingArray.length; i++) {
        	smooth += bearingArray[i];
        	smoothCnt++;
        }
        floatSmoothedBearing = (smoothCnt>0)?(smooth/smoothCnt):0f;

        if (bearingIdx == bearingArray.length) bearingIdx = 0;
        bearingArray[bearingIdx] = floatBearing;
        bearingIdx++;

        bearing = (int)Math.toDegrees(floatSmoothedBearing); //0 to 180 and 0 to -180
        if (bearing<0) bearing+=360; //adjust to 0-360
        
        GlobalData.setBearing(bearing);

        computing.set(false);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if(sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD && accuracy==SensorManager.SENSOR_STATUS_UNRELIABLE) {
            logger.info("Compass data unreliable");
        }
    }
}