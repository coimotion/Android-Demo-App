package com.gocharm.coimotion.apptemplate;

import java.util.ArrayList;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.coimotion.csdk.common.COIMCallListener;
import com.coimotion.csdk.common.COIMException;
import com.coimotion.csdk.util.ReqUtil;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class DetailActivity extends ActionBarActivity {
	private static final String LOG_TAG = "detailAct";
	
	private TextView descTxView, 
					 titleView,				 
					 showAddrView,
					 showPriceView,
					 showTimeView,
					 showPeriodView,
					 showOrgView,
					 infoSrcView;
	
	private ImageButton	buyBut,
						mapBut;
	
	private String lat,
				   lng,
				   place,
				   saleURL;
	
	private JSONArray showInfo;
	
	private ImageView descTxScrollImage,
					  showPriceScrollImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			ReqUtil.initSDK(getApplication());
		} catch (COIMException e) {
		} catch (Exception e) {
		}
		
		setContentView(R.layout.activity_detail);
		getSupportActionBar().setTitle("活動資訊");
		
		showOrgView = (TextView) findViewById(R.id.showOrg);
		
		infoSrcView = (TextView) findViewById(R.id.infoSrc);
		
		descTxScrollImage = (ImageView) findViewById(R.id.descScrollImage);
		descTxScrollImage.setVisibility(View.INVISIBLE);
		
		showPriceScrollImage = (ImageView) findViewById(R.id.showPriceScrollImage);
		showPriceScrollImage.setVisibility(View.INVISIBLE);
		
		descTxView = (TextView) findViewById(R.id.descTx);
		descTxView.setMovementMethod(new ScrollingMovementMethod());
		
		showPriceView = (TextView) findViewById(R.id.showPrice);
		showPriceView.setMovementMethod(new ScrollingMovementMethod());
		
		showPeriodView = (TextView) findViewById(R.id.showPeriod);
		
		showAddrView = (TextView) findViewById(R.id.showAddr);
		
		showTimeView = (TextView) findViewById(R.id.showTime);
		
		titleView = (TextView) findViewById(R.id.title);
		
		mapBut = (ImageButton) findViewById(R.id.mapButton);
		mapBut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), MapActivity.class);
				intent.putExtra("lat", lat);
				intent.putExtra("lng", lng);
				intent.putExtra("here", place);
				startActivity(intent);
			}
		});
		
		buyBut = (ImageButton) findViewById(R.id.buySiteBut);
		buyBut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(saleURL));
				startActivity(browserIntent);
			}
		});
		
		String spID = getIntent().getExtras().getString("spID");
		
		ReqUtil.send("twShow/show/info/" + spID, null, new COIMCallListener() {
			
			@Override
			public void onSuccess(Map<String, Object> result) {
				JSONObject valueMap = (JSONObject) result.get("value");
				try {
					showInfo = valueMap.getJSONArray("showInfo");
					if(showInfo.length() > 1) {
						final ArrayList<String> dropDown = new ArrayList<String>();
						
						for (int i = 0; i < showInfo.length(); i++) {
							String timeString = showInfo.getJSONObject(i).getString("time");
							timeString = timeString.replace("-", "/");
							timeString = timeString.substring(0, timeString.length()-3);
							dropDown.add( timeString + "|" + showInfo.getJSONObject(i).getString("placeName"));
						}
						
						SpinnerAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), 
								android.R.layout.simple_spinner_dropdown_item, dropDown)
								{

									@Override
									public View getView(int position, View convertView, ViewGroup parent) 
					        		{
										View view = convertView;
									    if (view == null) {
									      view = getLayoutInflater().inflate(R.layout.row_detail_spin, null);
									    }
					        			TextView text1 = (TextView) view.findViewById(R.id.detailTitle);
					        			TextView text2 = (TextView) view.findViewById(R.id.detailSubtitle);
					        			String tmpString = dropDown.get(position);
					        			int index = tmpString.indexOf("|");
					        			String title = tmpString.substring(0, index);
					        			String subtitle = tmpString.substring(index+1, tmpString.length());
					        			text1.setText(title);
					        			text2.setText(subtitle);
					        			return view;
					        		};
					        		
					        		@Override
									public View getDropDownView(int position, View convertView, ViewGroup parent) {
										View view = convertView;
									    if (view == null) {
									      view = getLayoutInflater().inflate(R.layout.row_detail_spin, null);
									    }
					        			TextView text1 = (TextView) view.findViewById(R.id.detailTitle);
					        			TextView text2 = (TextView) view.findViewById(R.id.detailSubtitle);
					        			String tmpString = dropDown.get(position);
					        			int index = tmpString.indexOf("|");
					        			String title = tmpString.substring(0, index);
					        			String subtitle = tmpString.substring(index+1, tmpString.length());
					        			text1.setText(title);
					        			text2.setText(subtitle);
					        			return view;
					        			
					        		}
					        	};
				        final ActionBar actionBar = getSupportActionBar();
				        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
				        actionBar.setListNavigationCallbacks(adapter,new OnNavigationListener() {
							
							@Override
							public boolean onNavigationItemSelected(int position, long arg1) {
								try {
									lat = showInfo.getJSONObject(position).getString("latitude");
									lng = showInfo.getJSONObject(position).getString("longitude");
									place = showInfo.getJSONObject(position).getString("placeName");
									
									String tmp = showInfo.getJSONObject(position).getString("time");
									showTimeView.setText("場次：" + tmp.replace("-", "/").substring(0, tmp.length()-3));
									
									showAddrView.setText("地點：" + showInfo.getJSONObject(position).getString("placeName") + "\n地址：" + showInfo.getJSONObject(position).getString("addr"));
									
									showPriceView.setText("票價：\n" + (showInfo.getJSONObject(position).getString("priceInfo").equals("")?"N/A":showInfo.getJSONObject(position).getString("priceInfo")));
									
									int showPriceHeight = showPriceView.getLineCount() * showPriceView.getLineHeight();
									if(showPriceHeight > showPriceView.getLayoutParams().height) {
										Log.i(LOG_TAG, "show");
										showPriceScrollImage.setVisibility(View.VISIBLE);
									}
									else{
										Log.i(LOG_TAG, "hide");
										showPriceScrollImage.setVisibility(View.INVISIBLE);
									}
									
									if(showInfo.getJSONObject(position).getInt("isFree") == 1) {
										showPriceView.setVisibility(View.INVISIBLE);
										buyBut.setVisibility(View.INVISIBLE);
									}
									else {
										showPriceView.setVisibility(View.VISIBLE);
										
										if(saleURL.equals("")){
											buyBut.setVisibility(View.INVISIBLE);
										}
										else {
											buyBut.setVisibility(View.VISIBLE);
										}
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
								
								return false;
							}
						});
					}
					
					lat = showInfo.getJSONObject(0).getString("latitude");
					lng = showInfo.getJSONObject(0).getString("longitude");
					place = showInfo.getJSONObject(0).getString("placeName");
					
					titleView.setText(valueMap.getString("title"));
					descTxView.setText(valueMap.getString("descTx").equals("")?"未提供":valueMap.getString("descTx"));
					
					LayoutParams params = descTxView.getLayoutParams();
					int contentHeight = descTxView.getLineCount() * descTxView.getLineHeight();
					DisplayMetrics metrics = getResources().getDisplayMetrics();
					
					if(contentHeight > metrics.density * 120) {//descTxView.getMaxHeight()) {
						descTxScrollImage.setVisibility(View.VISIBLE);
						
					}
					String startDate = valueMap.getString("startDate");
					String endDate = valueMap.getString("endDate");
					
					showPeriodView.setText("期間：" + startDate.replace("-", "/") + " 至 " + endDate.replace("-", "/"));
					showOrgView.setText("主辦：" + valueMap.getString("organizer"));
					infoSrcView.setText("售票：" + valueMap.getString("infoSrc"));
					String tmp = showInfo.getJSONObject(0).getString("time");
					showTimeView.setText("場次：" + tmp.replace("-", "/").substring(0, tmp.length()-3));
					
					showAddrView.setText("地點：" + showInfo.getJSONObject(0).getString("placeName") + "\n地址：" + showInfo.getJSONObject(0).getString("addr"));
					
					showPriceView.setText("票價：\n" + (showInfo.getJSONObject(0).getString("priceInfo").equals("")?"N/A":showInfo.getJSONObject(0).getString("priceInfo")));
					
					if(showInfo.getJSONObject(0).getInt("isFree") == 1) {
						showPriceView.setVisibility(View.INVISIBLE);
						buyBut.setImageResource(R.drawable.free);
						buyBut.setEnabled(false);
					}
					else {
						buyBut.setImageResource(R.drawable.shopping);
						buyBut.setEnabled(true);
						
						showPriceView.setVisibility(View.VISIBLE);
						if (!showInfo.getJSONObject(0).getString("priceInfo").equals("")){
							
							
							int showPriceHeight = showPriceView.getLineCount() * showPriceView.getLineHeight();
							if(showPriceHeight > showPriceView.getLayoutParams().height) {
								Log.i(LOG_TAG, "show");
								showPriceScrollImage.setVisibility(View.VISIBLE);
							}
							else{
								Log.i(LOG_TAG, "hide");
								showPriceScrollImage.setVisibility(View.INVISIBLE);
							}
						}
						else 
							showPriceView.setVisibility(View.INVISIBLE);
							
						saleURL = valueMap.getString("saleURL");
						/*if(saleURL.equals("")){
							buyBut.setVisibility(View.INVISIBLE);
						}
						else {
							buyBut.setVisibility(View.VISIBLE);
						}*/
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
				Log.i(LOG_TAG, "ex: " + ex.getLocalizedMessage());
			}
		});
		
	}
}
