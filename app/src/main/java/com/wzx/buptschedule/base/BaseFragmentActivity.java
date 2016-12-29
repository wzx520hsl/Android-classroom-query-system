package com.wzx.buptschedule.base;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.wzx.buptschedule.TasApp;
import com.wzx.buptschedule.bean.UserInfo;

public class BaseFragmentActivity extends FragmentActivity{

	protected OnFrgActClick mOnFrgActClick;
		
	private TasApp mApp;
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		mApp = (TasApp) getApplication();
	}
	/**
	 * deal with the click event
	 * @param view
	 */
	public void onOnFrgActClick(View view) {
		if(mOnFrgActClick!=null)
		mOnFrgActClick.onOnFrgActClick(view);
	}	
	
	
	/**
	 *  Get user information
	 * 
	 * @return
	 */
	public String getUserName() {
		return mApp.getUserName();
	}

	public UserInfo getUserInfo(){
		return mApp.getUserInfo();
	}
}
