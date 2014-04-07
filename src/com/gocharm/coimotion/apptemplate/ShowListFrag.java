package com.gocharm.coimotion.apptemplate;

import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.coimotion.csdk.common.COIMCallListener;
import com.coimotion.csdk.util.ReqUtil;

import android.R.anim;
import android.R.integer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AnalogClock;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class ShowListFrag extends Fragment {
	private final static String LOG_TAG = "show list";
	private static String catID;
	private ListView mShowList;
	public static ShowListFrag newInstance(String ID){
		ShowListFrag frag = new ShowListFrag();
		catID = ID;
		return  frag;
	}
	
	private SimpleAdapter adapter;
	ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_show_list, container,
				false);
		
		return rootView;
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Log.i(LOG_TAG,"catID: " + catID);
		
		mShowList = (ListView) view.findViewById(R.id.showList);
		mShowList.setOnItemClickListener(new OnItemClickListener() {
			 
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view,
	                int position, long id) {
	            Log.i(LOG_TAG, "click on " + position);
	        }
	    });
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("cat", catID);
		mapParam.put("fromTm", "2014-04-07");
		mapParam.put("toTm", "2014-04-014");
		ReqUtil.send("twShow/show/byCity/15", mapParam, new COIMCallListener() {
			
			@Override
			public void onSuccess(Map<String, Object> result) {
				// TODO Auto-generated method stub
				Log.i(LOG_TAG, "data: " + result);
				JSONArray arr= new JSONArray();
				try {
					arr = ((new JSONObject(result)).getJSONObject("value")).getJSONArray("list");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				for(int i = 0; i < arr.length(); i++) {
					HashMap<String, String> item = new HashMap<String, String>();
					JSONObject obj;
					try {
						obj = (JSONObject) arr.get(i);
						item.put("title", obj.getString("title"));
						item.put("placeName", obj.getString("placeName"));
						item.put("spID", obj.getString("spID"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					list.add(item);
					
				}
				//adapter = new SimpleAdapter(this, list, android.R.layout.simple_list_item_2, new String[]{"title", "placeName"}, new int[]{android.R.id.text1, android.R.id.text2});
				adapter = new SimpleAdapter(
						getActivity().getApplicationContext(), 
						list, R.layout.show_list_row, 
						new String[]{"title", "placeName"}, 
						new int[]{R.id.rowTitle, R.id.rowSubTitle});
				mShowList.setAdapter(adapter);
			}
			
			@Override
			public void onProgress(Integer progress) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFail(HttpResponse response, Exception ex) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
