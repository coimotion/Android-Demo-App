package com.gocharm.coimotion.apptemplate;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MapActivity extends ActionBarActivity {
	private static final String LOG_TAG = "mapAct";
	private double lat;
	private double lng;
	private GoogleMap map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		lat = Double.parseDouble(getIntent().getExtras().getString("lat"));
		lng = Double.parseDouble(getIntent().getExtras().getString("lng"));
		Log.i(LOG_TAG, "(lat, lng) = (" + lat + ", " + lng + ")");
		if (savedInstanceState == null) {
			SupportMapFragment mapFragment = new SupportMapFragment();
			MapsInitializer.initialize(this);
			Log.i(LOG_TAG, "3");
			map = mapFragment.getMap();
			Log.i(LOG_TAG, "4");
			Log.i(LOG_TAG, "map type: " + mapFragment);
			//map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			//Log.i(LOG_TAG, "map type: " + map.getMapType());
			getSupportFragmentManager().beginTransaction()
					.add(R.id.map, mapFragment).commit();
			//MapsInitializer.initialize(getApplicationContext());
		}
		//CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
		//map.animateCamera(camUpd3);
		/*map.setOnCameraChangeListener(new OnCameraChangeListener() {
			
			@Override
			public void onCameraChange(CameraPosition position) {
				// TODO Auto-generated method stub
				Log.i(LOG_TAG, "camera changed: " + position.toString());
			}
		});*/
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
