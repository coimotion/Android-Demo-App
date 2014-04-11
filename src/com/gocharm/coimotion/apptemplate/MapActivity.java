package com.gocharm.coimotion.apptemplate;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.coimotion.csdk.common.COIMCallListener;
import com.coimotion.csdk.util.ReqUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
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
	private static String tsIDs;
	//private GoogleMap map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(LOG_TAG, "activity on create");
		super.onCreate(savedInstanceState);
		double lat;
		double lng;
		String placeName;
		
		setContentView(R.layout.activity_map);
		android.support.v7.app.ActionBar ab = getSupportActionBar();
		ab.setTitle("活動地點");
		
		lat = Double.parseDouble(getIntent().getExtras().getString("lat"));
		lng = Double.parseDouble(getIntent().getExtras().getString("lng"));
		placeName = getIntent().getExtras().getString("here");
		ab.setSubtitle(placeName);
		Log.i(LOG_TAG, "(lat, lng) = (" + lat + ", " + lng + ")");
		getSupportActionBar().show();
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					//.add(R.id.map, new myMapFragment(new LatLng(lat,lng), placeName)).commit();
			.add(R.id.map, myMapFragment.newInstance(new LatLng(lat, lng), placeName)).commit();
		}
	}
	
	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.map, menu);
		MenuItem route = menu.add(0,111,0,"Routes");
		
		route.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		Log.i(LOG_TAG, "tsIDs: " + tsIDs);
		if (id == 111) {
			Intent intent = new Intent();
			intent.setClass(this, RouteListActivity.class);
			intent.putExtra("tsIDs", tsIDs);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@SuppressLint("ValidFragment")
	public static class myMapFragment extends SupportMapFragment {
		private static final String LOG_TAG = "mapFragment";
	    private GoogleMap mapView;
	    private Map<String, String> stopList;
	    public static LatLng destination;
	    public static String place;
	    
	    public static myMapFragment newInstance(LatLng pos, String placeName){
	    	myMapFragment frag = new myMapFragment();
	    	destination = pos;
	    	place = placeName;
			return  frag;
		}
	    
	    public myMapFragment() {
	    	Log.i(LOG_TAG, "fragment constructed");
	    	
	    }
	    
	    @Override
	    public void onCreate(Bundle arg0) {
	    	Log.i(LOG_TAG, "fragment on create");
	        super.onCreate(arg0);
	    }

	    @Override
	    public View onCreateView(LayoutInflater mInflater, ViewGroup arg1, Bundle arg2) {
	    	Log.i(LOG_TAG, "fragment on create view");
	        View view = super.onCreateView(mInflater, arg1, arg2);
	        
	        return view;
	    }

	    @Override
	    public void onInflate(Activity arg0, AttributeSet arg1, Bundle arg2) {
	        super.onInflate(arg0, arg1, arg2);
	        Log.i(LOG_TAG, "fragment on inflate");
	    }

	    @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	        Log.i(LOG_TAG, "fragment on activity create");
	        stopList = new HashMap<String, String>();
	        Map<String, Object> mapParam = new HashMap<String, Object>();
	        mapParam.put("lat", "" + destination.latitude);
	        mapParam.put("lng", "" + destination.longitude);
	        
	        ReqUtil.send("twCtBus/busStop/search", mapParam, new COIMCallListener() {
				
				@Override
				public void onSuccess(Map<String, Object> result) {
					// TODO Auto-generated method stub
					JSONObject value = (JSONObject) result.get("value");
					try {
						JSONArray stops = value.getJSONArray("list");
						Log.i(LOG_TAG, "success: " + stops);
						tsIDs = "[";
						for (int i=0; i<stops.length(); i++) {
							Map<String, String> item = new HashMap<String, String>();
							if(i>0) {
								tsIDs += ",";
							}
							tsIDs += stops.getJSONObject(i).getString("tsID");
							//mark stop i
							String title = stops.getJSONObject(i).getString("stName");
							double sLat = stops.getJSONObject(i).getDouble("latitude");
							double sLng = stops.getJSONObject(i).getDouble("longitude");
							Marker stop= mapView.addMarker(new MarkerOptions()
							.position(new LatLng(sLat, sLng))
							.title(title)
							.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
							//save to array
							item.put("title", title);
							stopList.put(stop.getId(), stops.getJSONObject(i).getString("tsID"));
						}
						tsIDs += "]";
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				
				@Override
				public void onProgress(Integer progress) {
				}
				
				@Override
				public void onFail(HttpResponse response, Exception ex) {
					Log.i(LOG_TAG, "ex: " + ex.getLocalizedMessage());
				}
			});
	        mapView = getMap();
	        mapView.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 16));
	        final Marker here= mapView.addMarker(new MarkerOptions().position(destination).title(place));
	        mapView.setOnMarkerClickListener(new OnMarkerClickListener() {
				
				@Override
				public boolean onMarkerClick(final Marker marker) {
					Log.i(LOG_TAG, "marker: " + marker.getTitle());
					Log.i(LOG_TAG, "here: " + here.getTitle());
					if(marker.getTitle().equals(here.getTitle())) {
						Log.i(LOG_TAG, "click here~~");
					}
					else {
						String tsID = stopList.get(marker.getId());
						ReqUtil.send("twCtBus/busStop/routes/" + tsID, null, new COIMCallListener() {
							
							@Override
							public void onSuccess(Map<String, Object> result) {
								try {
									JSONObject value = new JSONObject(result).getJSONObject("value");
									JSONArray list = value.getJSONArray("list");
									String tmp = "";
									for (int i = 0; i< list.length(); i++) {
										if(i > 0) 
											tmp += ", ";
										tmp += list.getJSONObject(i).getString("rtName");
									}
									marker.setSnippet(tmp);
									Log.i(LOG_TAG, "routes: " + tmp);
								} catch (JSONException e) {
									e.printStackTrace();
								}
								marker.showInfoWindow();
							}
							
							@Override
							public void onProgress(Integer progress) {
							}
							
							@Override
							public void onFail(HttpResponse response, Exception ex) {
								Log.i(LOG_TAG, "ex: " + ex.getLocalizedMessage());
							}
						});
					}
					return false;
				}
			});	        
	    }
	}
}
