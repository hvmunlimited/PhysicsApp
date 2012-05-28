package dk.pless84.physics.sound;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import dk.pless84.physics.R;

public class SoundActivity extends Activity {

	private MediaRecorder recorder;
	private Button start;
	private Button stop;
	private File path;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sound);
		
		start = (Button) findViewById(R.id.startRecBtn);
		start.setOnClickListener(startListener);
		stop = (Button) findViewById(R.id.stopRecBtn);
		stop.setOnClickListener(stopListener);
		
		recorder = new MediaRecorder();
		path = new File(Environment.getExternalStorageDirectory(), "myRecording,3gp");
		
		resetRecorder();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		recorder.release();
	}
	
	private void resetRecorder() {
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		recorder.setOutputFile(path.getAbsolutePath());
		try {
			recorder.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private View.OnClickListener startListener = new View.OnClickListener() {
		
		public void onClick(View v) {
			recorder.start();
			
			start.setEnabled(false);
			stop.setEnabled(true);
		}
	};

	private View.OnClickListener stopListener = new View.OnClickListener() {
		
		public void onClick(View v) {
			recorder.stop();
			resetRecorder();
			
			start.setEnabled(true);
			stop.setEnabled(false);
		}
	};
}