package dk.pless84.physics.flash;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import dk.pless84.physics.R;
import dk.pless84.physics.flash.ColorPickerDialog.OnColorChangedListener;

public class FlashActivity extends Activity {

	private ColorPickerDialog colorPicker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flash);

		colorPicker = new ColorPickerDialog(this, new OnColorChangedListener() {

			public void colorChanged(int color) {
				TextView flashBg = (TextView) findViewById(R.id.flashBg);
				flashBg.setBackgroundColor(color);
			}
		}, Color.WHITE);

		// Set screen brightness to 100%
		WindowManager.LayoutParams layoutParams = getWindow().getAttributes();

		layoutParams.screenBrightness = 1; // set 100% brightness
		getWindow().setAttributes(layoutParams);
	}

	public void changeColor(View view) {
		colorPicker.show();
	}
}