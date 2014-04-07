package com.gocharm.coimotion.apptemplate;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SigninFrag extends Fragment
{
	
	private EditText username;
	private EditText password;
	private Button submit;
	
	public SigninFrag() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_signin, container,
				false);
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		username = (EditText) view.findViewById(R.id.email);
		password = (EditText) view.findViewById(R.id.password);
		submit = (Button) view.findViewById(R.id.sign_in_button);
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

}
