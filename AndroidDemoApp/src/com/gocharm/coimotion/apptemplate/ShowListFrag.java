package com.gocharm.coimotion.apptemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.coimotion.csdk.common.COIMCallListener;
import com.coimotion.csdk.util.ReqUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class ShowListFrag extends Fragment {
	private final static String LOG_TAG = "show list";
	private static String catID;
	private ListView mShowList;
	private ImageView mNothingImage;
	private static int weeks;
	
	public static ShowListFrag newInstance(String ID, int period){
		ShowListFrag frag = new ShowListFrag();
		catID = ID;
		weeks = period + 1;
		return  frag;
	}
	
	private SimpleAdapter adapter;
	private ArrayList<HashMap<String,String>> dataArray = new ArrayList<HashMap<String,String>>();
	private AsyncTask<String, Integer, String> task = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_show_list, container,
				false);
		mShowList = (ListView) rootView.findViewById(R.id.showList);
		mShowList.setOnItemClickListener(new OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            String spID = (String)dataArray.get(position).get("spID");
	            Intent intent = new Intent();
				intent.putExtra("spID", spID);
				intent.setClass(getActivity(), DetailActivity.class);
				startActivity(intent);
	        }
	    });
		mNothingImage = (ImageView) rootView.findViewById(R.id.nothingImg);
		
		return rootView;
	}
	@SuppressLint("SimpleDateFormat")
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		Date now = new Date(System.currentTimeMillis());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Log.i(LOG_TAG, " now " + formatter.format(now));
		Calendar cal = Calendar.getInstance();
	    cal.setTime(now);
	    cal.add(Calendar.DAY_OF_MONTH, 7*weeks);
		Log.i(LOG_TAG, " now " + formatter.format((Date) cal.getTime()));
		
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("cat", catID);
		mapParam.put("fromTm", "" + formatter.format(now));
		mapParam.put("toTm", "" + formatter.format((Date) cal.getTime()));
		task = ReqUtil.send("twShow/show/byCity/15", mapParam, new COIMCallListener() {
			
			@Override
			public void onSuccess(Map<String, Object> result) {
				Log.i(LOG_TAG, "success");
				JSONArray arr= new JSONArray();
				try {
					arr = ((new JSONObject(result)).getJSONObject("value")).getJSONArray("list");
				} catch (JSONException e) {
				}
				
				if (arr.length() == 0) {
					mNothingImage.setVisibility(View.VISIBLE);
					mShowList.setVisibility(View.INVISIBLE);
				}
				else {
					String title = "";
					for(int i = 0; i < arr.length(); i++) {
						HashMap<String, String> item = new HashMap<String, String>();
						JSONObject obj;
						try {
							obj = (JSONObject) arr.get(i);
							Log.i(LOG_TAG, "obj: " + obj);
							if (!title.equals(obj.getString("title"))) {
								title = obj.getString("title");
								item.put("title", obj.getString("title"));
								item.put("placeName", obj.getString("placeName"));
								item.put("spID", obj.getString("spID"));
								item.put("imgURL", obj.getString("imgURL"));
								dataArray.add(item);
							}
						} catch (JSONException e) {
						}					
					}
					adapter = new SimpleAdapter(
							getActivity().getApplicationContext(), 
							dataArray, R.layout.row_show_list, 
							new String[]{"title", "placeName"}, 
							new int[]{R.id.rowTitle, R.id.rowSubTitle});
					
					mShowList.setAdapter(adapter);
					mNothingImage.setVisibility(View.INVISIBLE);
					mShowList.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void onFail(HttpResponse response, Exception ex) {
				Log.i(LOG_TAG, "fail");
			}
			
			@Override
			public void onInvalidToken() {
				super.onInvalidToken();
				getActivity().getApplication().getSharedPreferences("artMania", 0).edit().putBoolean("logout", true).commit();
				Intent intent = new Intent(getActivity().getApplicationContext(), SplashActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onDestroy() {
		Log.i(LOG_TAG, "on destroy");
		if(task != null) {
			task.cancel(true);
		}
		super.onDestroy();
	}
}
