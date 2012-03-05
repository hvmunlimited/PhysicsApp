package dk.pless84.physics;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class Main extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TabHost tabHost = getTabHost();
        
        // Tab for Measuring
        String tmp = getResources().getString(R.string.measurements);
        TabSpec measureSpec = tabHost.newTabSpec(tmp);
        measureSpec.setIndicator(tmp, getResources().getDrawable(R.drawable.icon_measure_tab));
        Intent measureIntent = new Intent(this, MeasureActivity.class);
        measureSpec.setContent(measureIntent);
 
        // Tab for Tools
        tmp = getResources().getString(R.string.tools);
        TabSpec toolsSpec = tabHost.newTabSpec(tmp);
        toolsSpec.setIndicator(tmp, getResources().getDrawable(R.drawable.icon_tools_tab));
        Intent toolsIntent = new Intent(this, ToolsActivity.class);
        toolsSpec.setContent(toolsIntent);
        
        // Tab for Log
        tmp = getResources().getString(R.string.log);
        TabSpec logSpec = tabHost.newTabSpec(tmp);
        logSpec.setIndicator(tmp, getResources().getDrawable(R.drawable.icon_archive_tab));
        Intent logIntent = new Intent(this, LogActivity.class);
        logSpec.setContent(logIntent);

        // Adding all TabSpec to TabHost
        tabHost.addTab(measureSpec);
        tabHost.addTab(toolsSpec);
        tabHost.addTab(logSpec);
    }
}