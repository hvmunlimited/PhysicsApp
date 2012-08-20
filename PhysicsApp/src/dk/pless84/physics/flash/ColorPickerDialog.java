package dk.pless84.physics.flash;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import dk.pless84.physics.R;

public class ColorPickerDialog extends Dialog {
	private OnColorChangedListener mListener;
	private int mInitialColor;

	public ColorPickerDialog(Context context, OnColorChangedListener listener,
			int initialColor) {
		super(context);

		mListener = listener;
		mInitialColor = initialColor;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		OnColorChangedListener l = new OnColorChangedListener() {
			public void colorChanged(int color) {
				mListener.colorChanged(color);
				dismiss();
			}
		};

		LinearLayout layout = new LinearLayout(getContext());
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setGravity(Gravity.CENTER);
		layout.setPadding(10, 10, 10, 10);
		layout.addView(new ColorPickerView(getContext(), l, mInitialColor),
				new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT));

		setContentView(layout);
		setTitle(R.string.pickacolor);
	}

	private static class ColorPickerView extends View {
		private Paint mPaint;
		private Paint mCenterPaint;
		private int[] mColors;
		private OnColorChangedListener mListener;
		private boolean mTrackingCenter;
		private boolean mHighlightCenter;
		private int centerX;
		private int centerY;
		private int centerR;
		private float r;
		private RectF mRectF;

		ColorPickerView(Context c, OnColorChangedListener l, int color) {
			super(c);
			mListener = l;
			mColors = new int[] { 0xFFFF0000, 0xFFFF00FF, 0xFF0000FF,
					0xFF00FFFF, 0xFF00FF00, 0xFFFFFF00, 0xFFFF0000 };
			Shader s = new SweepGradient(0, 0, mColors, null);

			mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mPaint.setShader(s);
			mPaint.setStyle(Paint.Style.STROKE);
			mPaint.setStrokeWidth(32);

			mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mCenterPaint.setColor(color);
			mCenterPaint.setStrokeWidth(5);

			centerX = 100;
			centerY = 100;
			centerR = 32;
			r = centerX - mPaint.getStrokeWidth() * 0.5f;
			mRectF = new RectF(-r, -r, r, r);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			canvas.translate(centerX, centerX);

			canvas.drawOval(mRectF, mPaint);
			canvas.drawCircle(0, 0, centerR, mCenterPaint);

			if (mTrackingCenter) {
				int c = mCenterPaint.getColor();
				mCenterPaint.setStyle(Paint.Style.STROKE);

				if (mHighlightCenter) {
					mCenterPaint.setAlpha(0xFF);
				} else {
					mCenterPaint.setAlpha(0x80);
				}
				canvas.drawCircle(0, 0,
						centerR + mCenterPaint.getStrokeWidth(), mCenterPaint);

				mCenterPaint.setStyle(Paint.Style.FILL);
				mCenterPaint.setColor(c);
			}
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			setMeasuredDimension(centerX * 2, centerY * 2);
		}

		private int ave(int s, int d, float p) {
			return s + Math.round(p * (d - s));
		}

		private int interpColor(int colors[], double unit) {
			if (unit <= 0)
				return colors[0];
			if (unit >= 1)
				return colors[colors.length - 1];

			float p = (float) (unit * (colors.length - 1));
			int i = (int) p;
			p -= i;

			// now p is just the fractional part [0...1) and i is the index
			int c0 = colors[i];
			int c1 = colors[i + 1];
			int a = ave(Color.alpha(c0), Color.alpha(c1), p);
			int r = ave(Color.red(c0), Color.red(c1), p);
			int g = ave(Color.green(c0), Color.green(c1), p);
			int b = ave(Color.blue(c0), Color.blue(c1), p);

			return Color.argb(a, r, g, b);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			double x = event.getX() - centerX;
			double y = event.getY() - centerY;
			boolean inCenter = Math.sqrt(x * x + y * y) <= centerR;

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mTrackingCenter = inCenter;
				if (inCenter) {
					mHighlightCenter = true;
					invalidate();
					break;
				}
			case MotionEvent.ACTION_MOVE:
				if (mTrackingCenter) {
					if (mHighlightCenter != inCenter) {
						mHighlightCenter = inCenter;
						invalidate();
					}
				} else {
					double unit = Math.atan2(y, x) / (2 * Math.PI);
					if (unit < 0) {
						unit += 1;
					}
					mCenterPaint.setColor(interpColor(mColors, unit));
					invalidate();
				}
				break;
			case MotionEvent.ACTION_UP:
				if (mTrackingCenter) {
					if (inCenter) {
						mListener.colorChanged(mCenterPaint.getColor());
					}
					mTrackingCenter = false; // so we draw w/o halo
					invalidate();
				}
				break;
			}
			return true;
		}
	}

	public interface OnColorChangedListener {
		void colorChanged(int color);
	}
}