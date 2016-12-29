package com.wzx.buptschedule.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;


/**
 * 拖拽的自定义View
 * 
 * @author sjy
 * 
 */
public class DragRlLayout extends RelativeLayout {
	private DragLayout dragLy;

	public DragRlLayout(Context context) {
		super(context);
	}

	public DragRlLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DragRlLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setDragLayout(DragLayout dl) {
		this.dragLy = dl;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (dragLy.getStatus() != DragLayout.Status.Close) {
			return true;
		}
		return super.onInterceptTouchEvent(event);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (dragLy.getStatus() != DragLayout.Status.Close) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				dragLy.close();
			}
			return true;
		}
		return super.onTouchEvent(event);
	}

	@Override
	public boolean performClick() {
		// TODO Auto-generated method stub
		return super.performClick();
	}

}
