package com.gocharm.coimotion.apptemplate;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MapActivity extends ActionBarActivity {
	private static final String LOG_TAG = "mapAct";
	private double lat;
	private double lng;
	//private GoogleMap map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		lat = Double.parseDouble(getIntent().getExtras().getString("lat"));
		lng = Double.parseDouble(getIntent().getExtras().getString("lng"));
		Log.i(LOG_TAG, "(lat, lng) = (" + lat + ", " + lng + ")");
		if (savedInstanceState == null) {
			//SupportMapFragment mapFragment = new SupportMapFragment();
			//MapsInitializer.initialize(this);
			Log.i(LOG_TAG, "3");
			//map = mapFragment.getMap();
			Log.i(LOG_TAG, "4");
			//Log.i(LOG_TAG, "map type: " + mapFragment);
			//map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			//Log.i(LOG_TAG, "map type: " + map.getMapType());
			getSupportFragmentManager().beginTransaction()
					.add(R.id.map, new myMapFragment(new LatLng(lat,lng))).commit();
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
	
	@SuppressLint("ValidFragment")
	public class myMapFragment extends SupportMapFragment {

	    GoogleMap mapView;
	    private Context context;
	    private LatLng destination;
	    
	    public myMapFragment(LatLng pos) {
	    	destination = pos;
	    }
	    
	    @Override
	    public void onCreate(Bundle arg0) {
	        super.onCreate(arg0);
	    }

	    @Override
	    public View onCreateView(LayoutInflater mInflater, ViewGroup arg1,
	        Bundle arg2) {
	        View view = super.onCreateView(mInflater, arg1, arg2);

	        return view;
	    }

	    @Override
	    public void onInflate(Activity arg0, AttributeSet arg1, Bundle arg2) {
	        super.onInflate(arg0, arg1, arg2);
	    }

	    @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	        context = getActivity();
	        mapView = getMap();
	        Log.i(LOG_TAG, "lat: " + lat);
	        mapView.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 16));
	        
	        final Marker here= mapView.addMarker(new MarkerOptions().position(destination).title("WOW"));
	        mapView.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
				
				@Override
				public void onInfoWindowClick(Marker marker) {
					// TODO Auto-generated method stub
					Log.i(LOG_TAG, "marker: " + marker.getTitle());
					Log.i(LOG_TAG, "here: " + here.getTitle());
					if(marker.getTitle().equals(here.getTitle())) {
						Log.i(LOG_TAG, "click here~~");
					}
				}
			});
	        mapView.setOnCameraChangeListener(new OnCameraChangeListener() {
				@Override
				public void onCameraChange(CameraPosition position) {
					// TODO Auto-generated method stub
					Log.i(LOG_TAG, "position: " + position);
				}
			});
	    }
	}
}
