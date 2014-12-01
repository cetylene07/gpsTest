package com.example.gpstest;

import java.text.*;
import java.util.*;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {
	private Button btnShowLocation;
	private Button btnShutdown;

	private ListView listview;
	
	private gpsDBHelper db;

	private Double gps1;
	private Double gps2;
	private String date;

	private GpsInfo gps;
	
	private List<Contact> list;
	private Contact sitem;
	gpsadpater adapter;
	
	Intent intent;
	TimerTask testt;
	Timer timert;
	
	private Handler gHandler;
	private Runnable gRunnable;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnShowLocation = (Button) findViewById(R.id.button1);
		btnShutdown = (Button) findViewById(R.id.button2);


		db = new gpsDBHelper(this, null, 1);
		

        list = new ArrayList<Contact>();
        list = db.getAllContacts();
		
		adapter = new gpsadpater(this, list);
		listview = (ListView) findViewById(R.id.listView1);
		listview.setAdapter(adapter);
		

        listview.setOnItemClickListener(new OnItemClickListener() {	 
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int setpos = position + 1;
				sitem = db.getContact(setpos);
				
				intent=new Intent("com.example.gpstest.recgps");
				intent.putExtra("gps1",	sitem.gpsinfo1);
				intent.putExtra("gps2", sitem.gpsinfo2);
				startActivity(intent);
			}
		});
		// GPS 정보를 보여주기 위한 이벤트 클래스 등록
		btnShowLocation.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				testtimer();
			}
		});
		
		btnShutdown.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				stoptimer();
			}
		});
	
	}
	
	public void testtimer(){
		timert = new Timer();
		gHandler = new Handler();
		gRunnable = new Runnable(){
			public void run() {
				gpsdbreturn();
			}
		};
		
		testt = new TimerTask(){
			public void run() {
				gHandler.post(gRunnable);
			}
		};
		timert = new Timer();
		timert.scheduleAtFixedRate(testt, 500, 10000);
	}
	public void stoptimer(){
		gHandler.removeCallbacks(gRunnable);
		timert.cancel();
	}
	
	
	public void gpsdbreturn(){
		gps = new GpsInfo(MainActivity.this);
		Date today = Calendar.getInstance().getTime();
		SimpleDateFormat fdate = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm", Locale.KOREA);
		// GPS 사용유무 가져오기
		if (gps.isGetLocation()) {

			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();
			gps1 = latitude;
			gps2 = longitude;

			date = fdate.format(today);
			db.addContact(new Contact(date, gps1.toString(), gps2
					.toString()));

			Toast.makeText(getApplicationContext(),
					"당신의 위치 - \n위도: " + gps1 + "\n경도: " + gps2 + "\n시간 : " + date,
					Toast.LENGTH_LONG).show();
		} else {
			// GPS 를 사용할수 없으므로
			gps.showSettingsAlert();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			db.deleteAll();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
