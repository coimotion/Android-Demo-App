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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class SplashActivity extends Activity {
	private final static String LOG_TAG = "splash";
	private final static String checkTokenURL = "core/user/profile";

	private SharedPreferences pref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		//初始化COIMOTION SDK
		try {
			ReqUtil.initSDK(getApplication());
		} catch (COIMException e) {
		} catch (Exception e) {
		}
		pref = getApplication().getSharedPreferences("artMania", 0);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(pref.getBoolean("closeApp", false)) {
			pref.edit().remove("closeApp").commit();
			finish();
		}
		else {
			if(pref.getBoolean("logout", false)) {
				pref.edit().remove("logout").commit();
				Intent intent = new Intent();
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setClass(SplashActivity.this, LoginActivity.class);
				startActivity(intent);
			}
			else {
				// 透過API檢查token是否合法
				ReqUtil.send(checkTokenURL, null, new COIMCallListener() {		
					@Override
					public void onSuccess(Map<String, Object> results) {
						//API成功回傳
						JSONObject values = (JSONObject) results.get("value");
						try {
							//如果有附合法的token，dspName會回傳該token擁有者的dspName，否則dspName會是Guest
							if(!values.getString("dspName").equalsIgnoreCase("Guest")) {
								Intent intent = new Intent();
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								intent.setClass(SplashActivity.this, GridActivity.class);
								startActivity(intent);
							}
							else {
								Intent intent = new Intent();
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								intent.setClass(SplashActivity.this, LoginActivity.class);
								startActivity(intent);
							}
						} catch (JSONException e) {
						}
					}
									
					@Override
					public void onFail(HttpResponse arg0, Exception arg1) {
						Log.i(LOG_TAG, "err: " + arg1.getLocalizedMessage());
						Intent intent = new Intent();
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.setClass(SplashActivity.this, LoginActivity.class);
						startActivity(intent);
					}
				});
			}
		}
	}
	
}
