package com.gocharm.coimotion.apptemplate;

import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import com.coimotion.csdk.common.COIMCallListener;
import com.coimotion.csdk.util.ReqUtil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class DetailActivity extends ActionBarActivity {
	private static final String LOG_TAG = "detailAct";
	
	private TextView descTxView;
	private TextView titleView;
	private ImageButton buyBut;
	private ImageButton mapBut;
	private String lat;
	private String lng;
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
					JSONObject showInfo = valueMap.getJSONArray("showInfo").getJSONObject(0);
					lat = showInfo.getString("latitude");
					lng = showInfo.getString("longitude");
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
