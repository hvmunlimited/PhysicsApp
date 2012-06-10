package dk.pless84.physics.compass;

import java.util.logging.Logger;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jwetherell.compass.data.GlobalData;

import dk.pless84.physics.R;


/**
 * This class extends the SensorsActivity and is designed tie the CompassView and Sensors together.
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class CompassActivity extends SensorActivity {
	private static final Logger logger = Logger.getLogger(CompassActivity.class.getSimpleName());

    private static TextView text = null;
    private static View compassView = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logger.info("onCreate()");

        setContentView(R.layout.compass2);

        text = (TextView) findViewById(R.id.text);
        compassView = findViewById(R.id.compass);
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	logger.info("onDestroy()");
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    	logger.info("onStart()");
    }
    
    @Override
    public void onStop() {
    	super.onStop();
    	logger.info("onStop()");
    }
    
    @Override
    public void onSensorChanged(SensorEvent evt) {
        super.onSensorChanged(evt);

        if (    evt.sensor.getType() == Sensor.TYPE_ACCELEROMETER || 
                evt.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD
        ) {
        	//Tell the compass to update it's graphics
            if (compassView!=null) compassView.postInvalidate();
        }

        //Update the direction text
        updateText(GlobalData.getBearing());
    }
    
    private static void updateText(float bearing) {
        int range = (int) (bearing / (360f / 16f)); 
        String  dirTxt = "";
        if (range == 15 || range == 0) dirTxt = "N"; 
        else if (range == 1 || range == 2) dirTxt = "NE"; 
        else if (range == 3 || range == 4) dirTxt = "E"; 
        else if (range == 5 || range == 6) dirTxt = "SE";
        else if (range == 7 || range == 8) dirTxt= "S"; 
        else if (range == 9 || range == 10) dirTxt = "SW"; 
        else if (range == 11 || range == 12) dirTxt = "W"; 
        else if (range == 13 || range == 14) dirTxt = "NW";
        text.setText(""+((int) bearing)+((char)176)+" "+dirTxt);
    }
}