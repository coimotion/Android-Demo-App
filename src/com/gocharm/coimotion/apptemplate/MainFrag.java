package com.gocharm.coimotion.apptemplate;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class MainFrag extends Fragment {

	ImageButton b1, b2, b3, b4, b5, b6, b7, b8, b9;
	public MainFrag newInstance(){
		MainFrag frag = new MainFrag();
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		
		return rootView;
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		b1 = (ImageButton) view.findViewById(R.id.imageButton1);
		b1.setOnClickListener(listener());
		b2 = (ImageButton) view.findViewById(R.id.imageButton2);
		b2.setOnClickListener(listener());
		b3 = (ImageButton) view.findViewById(R.id.imageButton3);
		b3.setOnClickListener(listener());
		b4 = (ImageButton) view.findViewById(R.id.ImageButton4);
		b4.setOnClickListener(listener());
		b5 = (ImageButton) view.findViewById(R.id.ImageButton5);
		b5.setOnClickListener(listener());
		b6 = (ImageButton) view.findViewById(R.id.imageButton6);
		b6.setOnClickListener(listener());
		b7 = (ImageButton) view.findViewById(R.id.ImageButton7);
		b7.setOnClickListener(listener());
		b8 = (ImageButton) view.findViewById(R.id.ImageButton8);
		b8.setOnClickListener(listener());
		b9 = (ImageButton) view.findViewById(R.id.ImageButton9);
		b9.setOnClickListener(listener());
	}
	
	public OnClickListener listener(){
		return new OnClickListener() {		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("main frag", "tag: " + v.getTag());
				Log.i("main frag", "" + getFragmentManager());
				//getFragmentManager().beginTransaction().add(R.id.container, new ShowListFrag()).commit();
				ShowListFrag showListFrag = ShowListFrag.newInstance((String) v.getTag());
				FragmentTransaction ft  = getFragmentManager().beginTransaction();
				ft.replace(R.id.container, showListFrag);
				ft.addToBackStack(null);
				ft.commit();
				sendMessage();
			}
		};
	}
	
	private void sendMessage() {
		  Log.d("sender", "Broadcasting message");
		  Intent intent = new Intent("unlock-drawer");
		  // You can also include some extra data.
		  intent.putExtra("message", "This is my message!");
		  LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).sendBroadcast(intent);
		}
}
