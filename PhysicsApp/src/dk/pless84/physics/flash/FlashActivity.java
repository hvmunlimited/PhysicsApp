package dk.pless84.physics.flash;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
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
				((TextView) findViewById(R.id.bg)).setBackgroundColor(color);
			}
		}, Color.WHITE);

		// Turn off auto screen brightness if on
		try {
			if (Settings.System.getInt(getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
				Settings.System.putInt(getContentResolver(),
						Settings.System.SCREEN_BRIGHTNESS_MODE,
						Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
			}
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}

		// Set screen brightness to 100%
		WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
		layoutParams.screenBrightness = 1.0F; // set 100% brightness
		getWindow().setAttributes(layoutParams);
	}

	public void changeColor(View view) {
		colorPicker.show();
		((TextView) findViewById(R.id.bg)).setText("");
	}
}