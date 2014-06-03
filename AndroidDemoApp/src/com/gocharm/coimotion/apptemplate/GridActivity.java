package com.gocharm.coimotion.apptemplate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class GridActivity extends Activity {
	private ImageButton b1, 
						b2, 
						b3, 
						b4, 
						b5, 
						b6, 
						b7, 
						b8, 
						b9, 
						infoButton;
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		getApplication().getSharedPreferences("artMania", 0).edit().putBoolean("closeApp", true).commit();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grid);
		
		b1 = (ImageButton) findViewById(R.id.buySiteBut);
		b1.setOnClickListener(listener);
		
		b2 = (ImageButton) findViewById(R.id.mapButton);
		b2.setOnClickListener(listener);
		
		b3 = (ImageButton) findViewById(R.id.imageButton3);
		b3.setOnClickListener(listener);
		
		b4 = (ImageButton) findViewById(R.id.ImageButton4);
		b4.setOnClickListener(listener);
		
		b5 = (ImageButton) findViewById(R.id.ImageButton5);
		b5.setOnClickListener(listener);
		
		b6 = (ImageButton) findViewById(R.id.imageButton6);
		b6.setOnClickListener(listener);
		
		b7 = (ImageButton) findViewById(R.id.ImageButton7);
		b7.setOnClickListener(listener);
		
		b8 = (ImageButton) findViewById(R.id.ImageButton8);
		b8.setOnClickListener(listener);
		
		b9 = (ImageButton) findViewById(R.id.ImageButton9);
		b9.setOnClickListener(listener);
		
		infoButton = (ImageButton) findViewById(R.id.infoBut);
		infoButton.setOnClickListener(listener);
	}

	private OnClickListener listener = new OnClickListener(){	
		@Override
		public void onClick(View v) {
			String catID = v.getTag().toString();
			Intent intent = new Intent();
			if(catID.equalsIgnoreCase("info")) {
				intent.setClass(GridActivity.this, AboutActivity.class);
			}
			else {
				intent.putExtra("catID", catID);
				intent.setClass(GridActivity.this, ShowListActivity.class);
			}
			startActivity(intent);
		}
	};
}
