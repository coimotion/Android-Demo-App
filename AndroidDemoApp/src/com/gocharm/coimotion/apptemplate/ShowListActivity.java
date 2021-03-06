package com.gocharm.coimotion.apptemplate;

import com.coimotion.csdk.common.COIMException;
import com.coimotion.csdk.util.ReqUtil;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SpinnerAdapter;

public class ShowListActivity extends ActionBarActivity {
	private final static String LOG_TAG = "ShowActivity";
	
	public DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private LinearLayout mLlvDrawerContent;
	private ListView mLsvDrawerMenu;
	// 記錄被選擇的選單指標用
	private int  pIndex = -1,cIndex = -1;
	private String title = null;
	private boolean flag = false;
	private boolean open = true;
	// 選單項目
	public static final String[] MENU_ITEMS = {
		"音樂表演",  "戲劇表演",  "舞蹈表演", "親子活動", "展覽活動", "講座", "電影", "演唱會", "其它資訊"
	};
	public static final String[] catIDs = {
	    "1", "2", "3", "4", "6", "7", "8", "17", "15"
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			ReqUtil.initSDK(getApplication());
		} catch (COIMException e) {
		} catch (Exception e) {
		}
		
		setContentView(R.layout.activity_main);
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drw_layout);
	    // 生成SpinnerAdapter
        SpinnerAdapter adapter = ArrayAdapter.createFromResource(this, R.array.period, android.R.layout.simple_spinner_dropdown_item);
        // 得到ActionBar
        
        final ActionBar actionBar = getSupportActionBar();
        // 将ActionBar的操作模型设置为NAVIGATION_MODE_LIST
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        // 为ActionBar设置下拉菜单和监听器
        actionBar.setListNavigationCallbacks(adapter, new OnNavigationListener() {
			
			@Override
			public boolean onNavigationItemSelected(int itemPosition, long arg1) {
	        	if(!open){
		            if(cIndex > -1) {
			        	Log.i(LOG_TAG, "selected: " + itemPosition);
			        	ShowListFrag listFrag = ShowListFrag.newInstance(catIDs[cIndex], itemPosition);
						FragmentTransaction ft  = getSupportFragmentManager().beginTransaction();
						ft.replace(R.id.container, listFrag);
						ft.commit();
		            }
	        	}
	        	open = false;
	            //return true;
				return false;
			}
		});
		
		//actionBar.setDisplayHomeAsUpEnabled(true);
		mDrawerToggle = new ActionBarDrawerToggle(
	            this, 
	            mDrawerLayout,    // 讓 Drawer Toggle 知道母體介面是誰
	            R.drawable.ic_drawer, // Drawer 的 Icon
	            R.string.open_left_drawer, // Drawer 被打開時的描述
	            R.string.close_left_drawer // Drawer 被關閉時的描述
	            ) {
	                //被打開後要做的事情
	                @Override
	                public void onDrawerOpened(View drawerView) {
	                    // 將 Title 設定為自定義的文字
	                    getSupportActionBar().setTitle("活動類別");
	                }
	 
	                //被關上後要做的事情
	                @Override
	                public void onDrawerClosed(View drawerView) {
	                	if(flag) {
	                		flag = false;
		                    if (cIndex > -1) {
		                    	//flag = false;
		                    	title = MENU_ITEMS[cIndex];
		                        getSupportActionBar().setTitle(title);
		                        ShowListFrag listFrag = ShowListFrag.newInstance(catIDs[cIndex], 0);
		        				FragmentTransaction ft  = getSupportFragmentManager().beginTransaction();
		        				ft.replace(R.id.container, listFrag);
		        				ft.commit();
		        				open = true;
		        				actionBar.setSelectedNavigationItem(0);
		                    }
	                    } 
	                	else {
	                        getSupportActionBar().setTitle(title);
	                    }
	                	
	                }
	    };
	    mDrawerLayout.setDrawerListener(mDrawerToggle);
	    setDrawerMenu();
	    
	    String catID = "";
		if (savedInstanceState == null) {
			catID = getIntent().getExtras().getString("catID");
		}
		else {
			catID = savedInstanceState.getString("catID");
		}
		for (int i = 0; i < 9; i++) {
			if (catIDs[i].equals(catID)) {
				Log.i(LOG_TAG, "equals " + MENU_ITEMS[i]);
				title = MENU_ITEMS[i];
				getSupportActionBar().setTitle(MENU_ITEMS[i]);
				cIndex = i;
				
			}
		}
		ShowListFrag slFrag = ShowListFrag.newInstance(catID, 0);
		getSupportFragmentManager().beginTransaction().replace(R.id.container, slFrag).commit();
		
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
	}
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
	
	private void setDrawerMenu() {
	    // 定義新宣告的兩個物件：選項清單的 ListView 以及 Drawer內容的 LinearLayou
	    mLsvDrawerMenu = (ListView) findViewById(R.id.lsv_drawer_menu);
	    mLlvDrawerContent = (LinearLayout) findViewById(R.id.llv_left_drawer);
	    mLsvDrawerMenu.setSelection(cIndex);
	    // 當清單選項的子物件被點擊時要做的動作
	    mLsvDrawerMenu.setOnItemClickListener(new OnItemClickListener() {
	 
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            selectMenuItem(position);
	        }
	    });
	    // 設定清單的 Adapter，這裡直接使用 ArrayAdapter<String>
	    mLsvDrawerMenu.setAdapter(new ArrayAdapter<String>(
	            this,
	            R.layout.drawer_menu_item,  // 選單物件的介面 
	            MENU_ITEMS                  // 選單內容
	    ));
	}
	
	private void selectMenuItem(int position) {
		if(position != cIndex) {
			flag = true;
		}
	    cIndex = position;
	    // 將選單的子物件設定為被選擇的狀態
	    mLsvDrawerMenu.setItemChecked(position, true);	 
	    // 關掉 Drawer
	    mDrawerLayout.closeDrawer(mLlvDrawerContent);
	}
	
}
