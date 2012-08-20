package dk.pless84.physics.compass;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;
import dk.pless84.physics.R;

public class CompassView extends ImageView {

	private float azimuth;

	public CompassView(Context context) {
		super(context);
		setImageResource(R.drawable.compass_pointer);
		azimuth = 0;
	}

	public CompassView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setImageResource(R.drawable.compass_pointer);
		azimuth = 0;
	}
	
	public CompassView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setImageResource(R.drawable.compass_pointer);
		azimuth = 0;
	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.rotate(azimuth, getWidth() / 2, getHeight() / 2);
		super.onDraw(canvas);
	}

	public void setAzimuth(float azimuth) {
		this.azimuth = azimuth;
		invalidate();
	}
}
