package dk.pless84.physics.fourier;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import ca.uol.aig.fftpack.RealDoubleFFT;
import dk.pless84.physics.R;

public class FourierActivity extends Activity {
	private int frequency = 8000;
	private int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_STEREO;
	private int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

	private RealDoubleFFT transformer;
	private int blockSize = 256;

	private RecordAudio recordTask;

	private ImageView imageView;
	private Bitmap bitmap;
	private Canvas canvas;
	private Paint paint;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fourier);

		transformer = new RealDoubleFFT(blockSize);
		
		imageView = (ImageView) findViewById(R.id.fourierView);
		bitmap = Bitmap.createBitmap(512, 200,
				Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap);
		paint = new Paint();
		paint.setColor(Color.GREEN);
		imageView.setImageBitmap(bitmap);
		recordTask = new RecordAudio();
		recordTask.execute();
	}

	private class RecordAudio extends AsyncTask<Void, double[], Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				int bufferSize = AudioRecord.getMinBufferSize(frequency,
						channelConfiguration, audioEncoding);

				AudioRecord audioRecord = new AudioRecord(
						MediaRecorder.AudioSource.MIC, frequency,
						channelConfiguration, audioEncoding, bufferSize);

				short[] buffer = new short[blockSize];
				double[] toTransform = new double[blockSize];

				audioRecord.startRecording();

				while (recordTask != null) {
					int bufferReadResult = audioRecord.read(buffer, 0,
							blockSize);

					for (int i = 0; i < blockSize && i < bufferReadResult; i++) {
						toTransform[i] = buffer[i] / 32768.0; // signed
																		// 16bit
					}

					transformer.ft(toTransform);
					publishProgress(toTransform);
				}

				audioRecord.stop();
			} catch (Throwable t) {
				Log.e("AudioRecord", "Recording Failed");
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(double[]... toTransform) {
			canvas.drawColor(Color.WHITE);

			for (int i = 0; i < toTransform[0].length; i++) {
				int x = i;
				int downy = (int) (100 - (toTransform[0][i] * 10));
				int upy = 100;

				canvas.drawLine(x, downy, x, upy, paint);
			}
			imageView.invalidate();
		}
	}

	@Override
	protected void onDestroy() {
		recordTask.cancel(true);
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		recordTask.cancel(true);
		super.onPause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		// recordTask.execute();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// recordTask.execute();
	}
}