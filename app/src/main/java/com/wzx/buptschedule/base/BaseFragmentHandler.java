package com.wzx.buptschedule.base;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.wzx.buptschedule.util.SingleToast;



public class BaseFragmentHandler extends Handler {
	
	protected static final String TAG = BaseFragmentHandler.class.getSimpleName();
	

	public static final int TOAST_INT = 0x3691;

	public static final int TOAST_STR = TOAST_INT+1;

	public static final int REQ_SUCC = TOAST_STR+1;

	public static final int REQ_ERROR = REQ_SUCC+1;

	public static final int NET_ERROR = REQ_ERROR+1;
	
	protected BaseFragment ui;
	
	public BaseFragmentHandler (BaseFragment ui) {
		this.ui = ui;
	}
	
	public BaseFragmentHandler (Looper looper) {
		super(looper);
	}
	

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case TOAST_INT:
			toastHint((Integer)msg.obj);
			break;
		case TOAST_STR:
			toastHint((String)msg.obj);
			break;
		default:
			break;
		}
	}
	
	public void toastHint (String msg) {
		SingleToast.showToast(ui.mAct, msg, 1500);
	}
	
	public void toastHint (int msgId) {
		SingleToast.showToast(ui.mAct, ui.getResources().getString(msgId), 1500);
	}
	
}