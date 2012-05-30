package dk.pless84.physics.compass;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.ImageView;
import dk.pless84.physics.R;

public class Rose extends ImageView {
	private float direction;
	
	public Rose(Context context) {
		super(context);
		direction = 0;
		
		setImageResource(R.drawable.compassrose);
	}
	
	// Called when component is to be drawn
	@Override
	public void onDraw(Canvas canvas) {
		int height = getHeight();
		int width = getWidth();
		
		setBackgroundResource(R.color.background);
		canvas.rotate(direction, width / 2, height / 2);
		super.onDraw(canvas);
	}
	
	// Called by Compass to update the orientation
	public void setDirection(float direction) {
		this.direction = direction;
		invalidate();
	}
}
