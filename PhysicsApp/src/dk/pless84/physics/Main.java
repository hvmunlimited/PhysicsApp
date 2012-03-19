package dk.pless84.physics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;

public class Main extends Activity implements OnClickListener {

	private TableLayout table;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        table = (TableLayout) findViewById(R.id.table);
        
        for (int i = 0; i < table.getChildCount(); i++) {
			TableRow tableRow = (TableRow) table.getChildAt(i);
			for (int j = 0; j < tableRow.getChildCount(); j++) {
				tableRow.getChildAt(j).setOnClickListener(this);
			}
		}
    }

    public void onClick(View view) {
    	Intent i = new Intent();
    	switch (view.getId()) {
		case R.id.acceleration:
			i = new Intent(this, AccActivity.class);
			break;
		case R.id.light:
			i = new Intent(this, LightActivity.class);
			break;
		case R.id.sound:
			i = new Intent(this, SoundActivity.class);
			break;
		case R.id.fourier:
			i = new Intent(this, FourierActivity.class);
			break;
		case R.id.magnet:
			i = new Intent(this, MagnetActivity.class);
			break;
		case R.id.flash:
			i = new Intent(this, FlashActivity.class);
			break;
		case R.id.compass:
			i = new Intent(this, CompassActivity.class);
			break;
		case R.id.clock:
			i = new Intent(this, ClockActivity.class);
			break;
		case R.id.osci:
			i = new Intent(this, OsciActivity.class);
			break;
		case R.id.convert:
			i = new Intent(this, ConvertActivity.class);
			break;
		case R.id.angle:
			i = new Intent(this, AngleActivity.class);
			break;
		case R.id.log:
			i = new Intent(this, LogActivity.class);
			break;
		default:
			i = new Intent();
			break;
		}
    	startActivity(i);
	}
}