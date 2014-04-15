package com.gocharm.coimotion.apptemplate;

import java.util.Map;

import org.apache.http.HttpResponse;

import com.coimotion.csdk.common.COIMCallListener;
import com.coimotion.csdk.util.ReqUtil;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AboutActivity extends ActionBarActivity {
	private static final String LOG_TAG = "aboutActivity";
	private Button logoutButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		logoutButton = (Button) findViewById(R.id.logoutBut);
		logoutButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ReqUtil.logout(new COIMCallListener() {
					
					@SuppressLint("InlinedApi")
					@Override
					public void onSuccess(Map<String, Object> result) {
						Log.i(LOG_TAG, "success");
						Intent intent = new Intent();
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
						intent.setClass(AboutActivity.this, LoginActivity.class);
						startActivity(intent);
					}
					
					@Override
					public void onProgress(Integer progress) {
					}
					
					@SuppressLint("InlinedApi")
					@Override
					public void onFail(HttpResponse response, Exception ex) {
						Log.i(LOG_TAG, "fail");
						Intent intent = new Intent();
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
						intent.setClass(AboutActivity.this, LoginActivity.class);
						startActivity(intent);
					}
				});
			}
		});
		
	}

}
