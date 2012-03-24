package dk.pless84.physics.log;

import android.app.ListActivity;
import android.os.Bundle;
import dk.pless84.physics.R;

public class LogActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log);
	}
}