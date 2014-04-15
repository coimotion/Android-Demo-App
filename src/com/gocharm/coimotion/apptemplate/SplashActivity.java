package com.gocharm.coimotion.apptemplate;

import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import com.coimotion.csdk.common.COIMCallListener;
import com.coimotion.csdk.common.COIMException;
import com.coimotion.csdk.util.ReqUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SplashActivity extends Activity {
	private final static String LOG_TAG = "splash";
	private final static String checkTokenURL = "go4art/account/profile";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		try {
			ReqUtil.initSDK(getApplication());
		} catch (COIMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ReqUtil.send(checkTokenURL, null, new COIMCallListener() {
			
			@Override
			public void onSuccess(Map<String, Object> results) {
				// TODO Auto-generated method stub
				Log.i(LOG_TAG, "" + results);
				//if (results.get("errCode").equals("0")) {
					JSONObject values = (JSONObject) results.get("value");
					try {
						Log.i(LOG_TAG,"dspname: " + values.getString("dspName"));
						if(!values.getString("dspName").equalsIgnoreCase("Guest")) {
							Intent intent = new Intent();
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intent.setClass(SplashActivity.this, GridActivity.class);
							startActivity(intent);
							finish();
						}
						else {
							Intent intent = new Intent();
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intent.setClass(SplashActivity.this, LoginActivity.class);
							startActivity(intent);
							finish();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				//}
			}
			
			@Override
			public void onProgress(Integer arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFail(HttpResponse arg0, Exception arg1) {
				// TODO Auto-generated method stub
				Log.i(LOG_TAG, "err: " + arg1.getLocalizedMessage());
			}
		});
	}
}
