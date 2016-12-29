package com.wzx.buptschedule.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.wzx.buptschedule.R;



public class MarkButton extends TextView{


	public MarkButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}

	public MarkButton(Context context) {
		super(context);
		initViews(context);
	}

	private void initViews(Context context){
//		DisplayMetrics dm = getResources().getDisplayMetrics();
//		float div = (dm.widthPixels*10)/1080;
		this.setBackgroundResource(R.drawable.selector_btn_mark);
		
	}
}
