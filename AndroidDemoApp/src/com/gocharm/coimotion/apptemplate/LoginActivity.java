package com.gocharm.coimotion.apptemplate;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;

import com.coimotion.csdk.common.COIMCallListener;
import com.coimotion.csdk.common.COIMException;
import com.coimotion.csdk.util.ReqUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class LoginActivity extends Activity {
	private static final String LOG_TAG = "loginActivity";
	
	private EditText accNameText,
					 passwdText,
				     passwd2Text;
	private Button submitBut;
	private RadioButton loginRadio,
						regRadio;
	private RadioGroup radioGroup;
	private ProgressDialog pDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			ReqUtil.initSDK(getApplication());
		} catch (COIMException e) {
		} catch (Exception e) {
		}
		
		setContentView(R.layout.activity_login);
		
		accNameText = (EditText) findViewById(R.id.accName);
		passwdText = (EditText) findViewById(R.id.passwd);
		passwd2Text = (EditText) findViewById(R.id.passwd2);
		submitBut = (Button) findViewById(R.id.submitBut);
		submitBut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(loginRadio.isChecked()){
					Log.i(LOG_TAG, "login mode");
					pDialog = ProgressDialog.show(
			                  LoginActivity.this,
			                  "",
			                  "登入中…",
			                  true
			                );
					Map<String, Object> mapParam = new HashMap<String, Object>();
					mapParam.put("accName", accNameText.getText().toString());
					mapParam.put("passwd", passwdText.getText().toString());
					ReqUtil.login("core/user/login", mapParam, new COIMCallListener() {
						
						@Override
						public void onSuccess(Map<String, Object> result) {
							Log.i(LOG_TAG, "success\n" + result);
							pDialog.dismiss();
							Intent intent = new Intent();
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intent.setClass(LoginActivity.this, GridActivity.class);
							startActivity(intent);
							finish();
						}
						
						@Override
						public void onFail(HttpResponse response, Exception ex) {
							Log.i(LOG_TAG, "fail\n" + ex.getLocalizedMessage());
							pDialog.dismiss();
						}
					});
				}
				if(regRadio.isChecked()){
					Log.i(LOG_TAG, "reg mode");
					pDialog = ProgressDialog.show(
			                  LoginActivity.this,
			                  "",
			                  "註冊中…",
			                  true
			                );
					Map<String, Object> mapParam = new HashMap<String, Object>();
					mapParam.put("accName", accNameText.getText().toString());
					mapParam.put("passwd", passwdText.getText().toString());
					mapParam.put("passwd2", passwd2Text.getText().toString());
					ReqUtil.registerUser(mapParam, new COIMCallListener() {
						
						@Override
						public void onSuccess(Map<String, Object> result) {
							Log.i(LOG_TAG, "success\n" + result);
							pDialog.dismiss();
							Intent intent = new Intent();
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intent.setClass(LoginActivity.this, GridActivity.class);
							startActivity(intent);
							finish();
						}
						
						@Override
						public void onFail(HttpResponse response, Exception ex) {
							// TODO Auto-generated method stub
							Log.i(LOG_TAG, "fail\n" + ex.getLocalizedMessage());
							pDialog.dismiss();
						}
					});
				}
			}
		});
		
		loginRadio = (RadioButton) findViewById(R.id.loginRadio);
		regRadio = (RadioButton) findViewById(R.id.regRadio);
		
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(loginRadio.isChecked()){
					passwd2Text.setVisibility(View.INVISIBLE);
					submitBut.setText("登入");
				}
				if(regRadio.isChecked()){
					passwd2Text.setVisibility(View.VISIBLE);
					submitBut.setText("註冊");
				}
			}
		});
	}

}
