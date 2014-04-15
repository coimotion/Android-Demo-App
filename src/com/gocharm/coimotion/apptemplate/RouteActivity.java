package com.gocharm.coimotion.apptemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.coimotion.csdk.common.COIMCallListener;
import com.coimotion.csdk.util.ReqUtil;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.R.array;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.os.Build;

public class RouteActivity extends ActionBarActivity {
	private static final String LOG_TAG = "routeInfo";
	private ListView stopList;
	private SimpleAdapter adapter;
	private ArrayList<HashMap<String,String>> dataArray = new ArrayList<HashMap<String,String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route);
		
		stopList = (ListView) findViewById(R.id.stopList);
		getSupportActionBar().setTitle(getIntent().getExtras().getString("title"));
		ReqUtil.send("twCtBus/busRoute/next/" + getIntent().getExtras().getString("brID"), null, new COIMCallListener() {
			@Override
			public void onSuccess(Map<String, Object> result) {
				// TODO Auto-generated method stub
				JSONObject value = (JSONObject)result.get("value");
				try {
					JSONArray list = value.getJSONArray("list");
					for (int i=0; i<list.length();i++) {
						HashMap<String, String> item = new HashMap<String, String>();
						String stName = (list.getJSONObject(i).getString("stName") == null)?"":list.getJSONObject(i).getString("stName");
						String arvTime = (list.getJSONObject(i).has("arvTime"))?list.getJSONObject(i).getString("arvTime"):"無";
						if (arvTime.equals("")) {
							arvTime = "未提供";
						}
						item.put("stName",  stName);
						item.put("arvTime", arvTime);
						item.put("isInBd", list.getJSONObject(i).getString("isInBd"));
						dataArray.add(item);
					}
					Log.i(LOG_TAG, "success\n" + dataArray);
					
					adapter = new SimpleAdapter(getApplicationContext(), 
							dataArray, 
							R.layout.row_stop_time,//android.R.layout.simple_list_item_2, 
							new String[]{"stName", "arvTime"}, 
							new int[]{R.id.stopText, R.id.timeText}){

			        		@SuppressLint("ResourceAsColor")
							public View getView(int position, View convertView, ViewGroup parent) {
			        			View view = super.getView(position, convertView, parent);
			        			TextView text1 = (TextView) view.findViewById(R.id.stopText);
			        			text1.setTextColor(R.color.DeepBlue);
			        			TextView text2 = (TextView) view.findViewById(R.id.timeText);
			        			text2.setTextColor(R.color.BlackOak);
			        			ImageView gobackImg = (ImageView) view.findViewById(R.id.gobackImage);
			        			if(dataArray.get(position).get("isInBd").equals("0")) {
			        				Log.i(LOG_TAG,"go");
			        				gobackImg.setImageResource(R.drawable.go);
			        				view.setBackgroundColor(getResources().getColor(R.color.goColor));
			        			}
			        			else {
			        				Log.i(LOG_TAG,"back");
			        				gobackImg.setImageResource(R.drawable.back);
			        				view.setBackgroundColor(getResources().getColor(R.color.backColor));
			        			}
			        			return view;

			        		};
					};
					try {
					stopList.setAdapter(adapter);
					}catch(Exception e) {
						Log.i(LOG_TAG, "e: " + e.getLocalizedMessage());
					}
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
				Log.i(LOG_TAG, "fail\n" + ex.getLocalizedMessage());
			}
		});
	}
	

}
