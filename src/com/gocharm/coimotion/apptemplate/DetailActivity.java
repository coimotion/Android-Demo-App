package com.gocharm.coimotion.apptemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.coimotion.csdk.common.COIMCallListener;
import com.coimotion.csdk.util.ReqUtil;
import com.gocharm.coimotion.apptemplate.ShowListActivity.DropDownListenser;

import android.R.array;
import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class DetailActivity extends ActionBarActivity {
	private static final String LOG_TAG = "detailAct";
	
	private TextView descTxView;
	private TextView titleView;
	private ImageButton buyBut;
	private ImageButton mapBut;
	private String lat;
	private String lng;
	private String place;
	private JSONArray showInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		getSupportActionBar().setTitle("活動資訊");
		descTxView = (TextView) findViewById(R.id.descTx);
		descTxView.setMovementMethod(new ScrollingMovementMethod());
		
		titleView = (TextView) findViewById(R.id.title);
		
		mapBut = (ImageButton) findViewById(R.id.mapButton);
		mapBut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), MapActivity.class);
				intent.putExtra("lat", lat);
				intent.putExtra("lng", lng);
				intent.putExtra("here", place);
				startActivity(intent);
				
			}
		});
		
		String spID = getIntent().getExtras().getString("spID");
		Log.i(LOG_TAG, "spID: " + spID);
		ReqUtil.send("twShow/show/info/" + spID, null, new COIMCallListener() {
			
			@Override
			public void onSuccess(Map<String, Object> result) {
				// TODO Auto-generated method stub
				//Log.i(LOG_TAG, "" + result);
				JSONObject valueMap = (JSONObject) result.get("value");
				Log.i(LOG_TAG, "value: " + valueMap);
				try {
					showInfo = valueMap.getJSONArray("showInfo");
					Log.i(LOG_TAG, "1st: " + showInfo.getJSONObject(0));
					if(showInfo.length() > 1) {
						ArrayList<String> dropDown = new ArrayList<String>();
						
						for (int i = 0; i < showInfo.length(); i++) {
							dropDown.add(showInfo.getJSONObject(i).getString("time"));
						}
						SpinnerAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.period, android.R.layout.simple_spinner_dropdown_item);
				        final ActionBar actionBar = getSupportActionBar();
				        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
				        actionBar.setListNavigationCallbacks(adapter,new OnNavigationListener() {
							
							@Override
							public boolean onNavigationItemSelected(int arg0, long arg1) {
								// TODO Auto-generated method stub
								
								return false;
							}
						});
					}
					//lat = showInfo.getString("latitude");
					//lng = showInfo.getString("longitude");
					//place = showInfo.getString("placeName");
					descTxView.setText(valueMap.getString("descTx"));
					titleView.setText(valueMap.getString("title"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			@Override
			public void onProgress(Integer progress) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFail(HttpResponse response, Exception ex) {
				// TODO Auto-generated method stub
				Log.i(LOG_TAG, "ex: " + ex.getLocalizedMessage());
			}
		});
		
	}

}
