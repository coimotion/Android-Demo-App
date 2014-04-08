package com.gocharm.coimotion.apptemplate;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity {
	private final static String LOG_TAG = "mainActivity";
	
	public DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private LinearLayout mLlvDrawerContent;
	private ListView mLsvDrawerMenu;
	// 記錄被選擇的選單指標用
	private int mCurrentMenuItemPosition = -1;
	private String title = null;
	// 選單項目
	public static final String[] MENU_ITEMS = {
		"音樂表演",  "戲劇表演",  "舞蹈表演", "親子活動", "展覽活動", "講座", "電影", "演唱會", "其它資訊"
	};
	public static final String[] catIDs = {
	    "1", "2", "3", "4", "6", "7", "8", "17", "15"
	};
	/*
	// Adapter
	SpinnerAdapter adapter =
	        ArrayAdapter.createFromResource(this, R.array.actions,
	        android.R.layout.simple_spinner_dropdown_item);

	// Callback
	OnNavigationListener callback = new OnNavigationListener() {

	    String[] items = getResources().getStringArray(R.array.actions); // List items from res

	    @Override
	    public boolean onNavigationItemSelected(int position, long id) {

	        // Do stuff when navigation item is selected

	        Log.d("NavigationItemSelected", items[position]); // Debug

	        return true;

	    }

	};
	*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drw_layout);
	    // 設定 Drawer 的影子
	    //mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		
		//actionBar.setDisplayHomeAsUpEnabled(true);
		mDrawerToggle = new ActionBarDrawerToggle(
	            this, 
	            mDrawerLayout,    // 讓 Drawer Toggle 知道母體介面是誰
	            R.drawable.ic_launcher, // Drawer 的 Icon
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
	                    if (mCurrentMenuItemPosition > -1) {
	                    	title = MENU_ITEMS[mCurrentMenuItemPosition];
	                        getSupportActionBar().setTitle(title);
	                        ShowListFrag listFrag = ShowListFrag.newInstance(catIDs[mCurrentMenuItemPosition]);
	        				FragmentTransaction ft  = getSupportFragmentManager().beginTransaction();
	        				ft.replace(R.id.container, listFrag);
	        				//ft.addToBackStack(null);
	        				ft.commit();
	                    } else {
	                        
	                        getSupportActionBar().setTitle(title);
	                    }
	                }
	    };
	 
	    mDrawerLayout.setDrawerListener(mDrawerToggle);
	    
		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			String catID = extras.getString("catID");
			for (int i = 0; i < 9; i++) {
				
				if (catIDs[i].equals(catID)) {
					Log.i(LOG_TAG, "equals " + MENU_ITEMS[i]);
					title = MENU_ITEMS[i];
					getSupportActionBar().setTitle(MENU_ITEMS[i]);
				}
			}
			ShowListFrag slFrag = ShowListFrag.newInstance(catID);
			getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, slFrag).commit();
		}
		setDrawerMenu();
	}
	
	private void setDrawerMenu() {
	    // 定義新宣告的兩個物件：選項清單的 ListView 以及 Drawer內容的 LinearLayou
	    mLsvDrawerMenu = (ListView) findViewById(R.id.lsv_drawer_menu);
	    mLlvDrawerContent = (LinearLayout) findViewById(R.id.llv_left_drawer);
	 
	    // 當清單選項的子物件被點擊時要做的動作
	    mLsvDrawerMenu.setOnItemClickListener(new OnItemClickListener() {
	 
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view,
	                int position, long id) {
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
	    mCurrentMenuItemPosition = position;
	 
	    // 將選單的子物件設定為被選擇的狀態
	    mLsvDrawerMenu.setItemChecked(position, true);
	 
	    // 關掉 Drawer
	    mDrawerLayout.closeDrawer(mLlvDrawerContent);
	}
	
}
