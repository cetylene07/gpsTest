package com.example.gpstest;

import android.app.*;
import android.content.*;
import android.os.*;
import android.widget.*;

public class Regps extends Activity {
	Intent intent;
	TextView gps1;
	TextView gps2;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.send_gps);
	    intent = getIntent();
	    gps1 = (TextView)findViewById(R.id.gpsre1);
	    gps2 = (TextView)findViewById(R.id.gpsre2);
	    
	    gps1.setText(intent.getStringExtra("gps1").toString());
	    gps2.setText(intent.getStringExtra("gps2").toString());
	    
	}
}
