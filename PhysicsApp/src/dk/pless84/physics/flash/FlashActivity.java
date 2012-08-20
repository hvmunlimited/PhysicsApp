package dk.pless84.physics.flash;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import dk.pless84.physics.R;

public class FlashActivity extends Activity implements
		ColorPickerDialog.OnColorChangedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flash);

		getWindow().getAttributes().screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL; // set 100% brightness
		getWindow().setAttributes(getWindow().getAttributes());
	}

	public void changeColor(View view) {
         new ColorPickerDialog(this, this,
                 Color.WHITE).show();
	}
	
    public void colorChanged(int color) {
        TextView flashBg = (TextView) findViewById(R.id.flashBg);
		flashBg.setBackgroundColor(color);
    }
}