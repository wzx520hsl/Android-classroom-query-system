package com.wzx.buptschedule.base;

import android.os.Bundle;




public class BaseSwitchFragment extends BaseFragment{

	public interface FragmentCallBack {
		void fragmentCallBack(int targetID, Bundle data);
	}
	
	protected FragmentCallBack mFragmentCallBack;
	
	public BaseSwitchFragment newInstance(FragmentCallBack mFragmentCallBack){
		this.mFragmentCallBack =mFragmentCallBack;
		return this;
	}
	
	public void doFragmentBackPressed(){
		
	}
}
