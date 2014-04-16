package com.gocharm.coimotion.apptemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.coimotion.csdk.common.COIMCallListener;
import com.coimotion.csdk.util.ReqUtil;
import com.gocharm.coimotion.apptemplate.R.drawable;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class RouteListActivity extends ActionBarActivity {
	private static final String LOG_TAG = "RouteView";
	private JSONArray tsIDs;
	private int nStop;
	private ListView routeList;
	private SimpleAdapter adapter;
	private ArrayList<HashMap<String,String>> dataArray = new ArrayList<HashMap<String,String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_route_view);
		getSupportActionBar().setTitle("路線列表");
		routeList = (ListView) findViewById(R.id.routeList);
		routeList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				String brID = dataArray.get(pos).get("brID");
				Intent intent = new Intent();
				intent.setClass(RouteListActivity.this, RouteActivity.class);
				intent.putExtra("brID", brID);
				intent.putExtra("title", dataArray.get(pos).get("rtName"));
				startActivity(intent);
			}
		});
		String tsIDStr = getIntent().getExtras().getString("tsIDs");
		try {
			tsIDs = new JSONArray(tsIDStr);
			nStop = tsIDs.length();
			queryRoutes(0);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	private void queryRoutes(final int index){
		try {
			ReqUtil.send("twCtBus/busStop/routes/" + tsIDs.getString(index) , null, new COIMCallListener() {
				
				@Override
				public void onSuccess(Map<String, Object> result) {
					// TODO Auto-generated method stub
					Log.i(LOG_TAG, "result: " + result);
					JSONObject value = (JSONObject) result.get("value");
					JSONArray list;
					try {
						list = value.getJSONArray("list");
						for (int i = 0; i < list.length(); i++) {
							HashMap<String, String> item = new HashMap<String,String>();
							item.put("brID", list.getJSONObject(i).getString("brID"));
							item.put("rtName", list.getJSONObject(i).getString("rtName") + "(" + list.getJSONObject(i).getString("descTx") + ")");
							//item.put("descTx", list.getJSONObject(i).getString("descTx"));
							if(!dataArray.contains(item)) {
								dataArray.add(item);
							}
						}
						if((index+1) < nStop) {
							queryRoutes(index+1);
							Log.i(LOG_TAG, "# data: " + dataArray.size());
						}
						else {
							adapter = new SimpleAdapter(
										getApplicationContext(), 
										dataArray, android.R.layout.simple_list_item_1, 
										new String[]{"rtName"}, 
										new int[]{android.R.id.text1}){

						        		@SuppressLint("ResourceAsColor")
										public View getView(int position, View convertView, ViewGroup parent) {
						        			View view = super.getView(position, convertView, parent);
						        			TextView text1 = (TextView) view.findViewById(android.R.id.text1);
						        			text1.setTextColor(R.color.DeepBlue);
						        			//view.setBackgroundColor(getResources().getColor(R.color.ClearColor));
						        			view.setBackgroundResource(R.drawable.bg_pink);
						        			//LayoutParams lp = view.getLayoutParams();
						        			//DisplayMetrics dm = getResources().getDisplayMetrics();
						        			
						        			//view.setLayoutParams(new LayoutParams(lp.width, (int)(50 * dm.density)));
						        			return view;
						        			
						        		};
						    };
							routeList.setAdapter(adapter);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}
				
				@Override
				public void onProgress(Integer progress) {
					
				}
				
				@Override
				public void onFail(HttpResponse response, Exception ex) {
					
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
