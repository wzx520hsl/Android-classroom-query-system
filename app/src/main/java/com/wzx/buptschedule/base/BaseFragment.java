package com.wzx.buptschedule.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;

import com.wzx.buptschedule.R;
import com.wzx.buptschedule.TasApp;
import com.wzx.buptschedule.bean.UserInfo;
import com.wzx.buptschedule.util.LogTrace;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class BaseFragment extends Fragment {

	protected static TasApp mApp;
	protected String TAG = BaseFragment.class.getSimpleName();
	protected BaseFragmentHandler handler;
	protected ExecutorService taskPool;
	protected Activity mAct;
	protected boolean showDebugMsg = true;
	protected int mScreenWidth;
	protected int mScreenHeight;

	/** Called when the mAct is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// debug memory
		// debugMemory("onCreate");
		this.handler = new BaseFragmentHandler(this);
		mAct = getActivity();
		mApp = (TasApp) mAct.getApplication();
		/**
		 * 获取屏幕宽度和高度
		 */
		DisplayMetrics metric = new DisplayMetrics();
		mAct.getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
		mScreenHeight = metric.heightPixels;
		this.taskPool = Executors.newCachedThreadPool();
	}

	public String getUserName(){
		return mApp.getUserName();
	}
	
	public UserInfo getUserInfo(){
		return mApp.getUserInfo();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		// debug memory
		// debugMemory("onStart");
	}

	@Override
	public void onStop() {
		super.onStop();
		// debug memory
		// debugMemory("onStop");
	}

	public void overlay(Class<?> classObj) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		intent.setClass(mAct, classObj);
		startActivity(intent);
	}

	public void overlay(Class<?> classObj, int enterAnim, int exitAnim) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		intent.setClass(mAct, classObj);
		startActivity(intent);
		mAct.overridePendingTransition(enterAnim, exitAnim);
	}

	public void overlay(Class<?> classObj, Bundle params) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		intent.setClass(mAct, classObj);
		intent.putExtras(params);
		startActivity(intent);
	}

	public void forward(Class<?> classObj, int enterAnim, int quitAnim) {
		Intent intent = new Intent();
		intent.setClass(mAct, classObj);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
		mAct.finish();
		mAct.overridePendingTransition(enterAnim, quitAnim);
	}

	public void forward(Class<?> classObj) {
		Intent intent = new Intent();
		intent.setClass(mAct, classObj);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
		mAct.finish();
	}

	public void forward(Class<?> classObj, Bundle params) {
		Intent intent = new Intent();
		intent.setClass(mAct, classObj);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtras(params);
		this.startActivity(intent);
		mAct.finish();
	}

	public Context getContext() {
		return mAct;
	}

	public BaseFragmentHandler getHandler() {
		return this.handler;
	}

	public void setHandler(BaseFragmentHandler handler) {
		this.handler = handler;
	}

	public LayoutInflater getLayout() {
		return (LayoutInflater) mAct
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getLayout(int layoutId) {
		return getLayout().inflate(layoutId, null);
	}

	public View getLayout(int layoutId, int itemId) {
		return getLayout(layoutId).findViewById(itemId);
	}

	public ExecutorService getTaskPool() {
		return this.taskPool;
	}

	public void doFinish() {
		mAct.finish();
	}

	public void toastHint(String msg) {
		sendMessage(BaseFragmentHandler.TOAST_STR, msg);
	}

	public void toastHint(int msgId) {
		sendMessage(BaseFragmentHandler.TOAST_INT, msgId);
	}

	public void sendMessage(int what) {
		Message m = new Message();
		m.what = what;
		handler.sendMessage(m);
	}

	public void sendMessage(int what, Object data) {
		Message m = new Message();
		m.what = what;
		m.obj = data;
		handler.sendMessage(m);
	}

	public void debugMemory(String tag) {
		if (this.showDebugMsg) {
			LogTrace.w(TAG, "debugMemory UsedMemory:", LogTrace.getUsedMemory()
					+ "");
		}
	}

	protected void showError(String errorMessage) {
		showError(errorMessage, mAct);
	}

	public void showError(String errorMessage, Activity activity) {
		new AlertDialog.Builder(activity)
				.setTitle(
						activity.getResources().getString(
								R.string.dialog_message_title))
				.setMessage(errorMessage)
				.setNegativeButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).show();
	}

}