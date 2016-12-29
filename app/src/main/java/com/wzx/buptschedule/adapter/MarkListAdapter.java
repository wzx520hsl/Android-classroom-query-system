package com.wzx.buptschedule.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.wzx.buptschedule.R;
import com.wzx.buptschedule.bean.MarkInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MarkListAdapter extends BaseAdapter {

//	private static final String TAG =SamplesAdapter.class.getSimpleName();
	
	public static interface IOnItemSelectListener {
		public void onItemClick(int pos);
	};

	private List<MarkInfo> list = new ArrayList<MarkInfo>();

	private LayoutInflater mInflater;
	
	private Context mContext;


	public MarkListAdapter(Context context,List<MarkInfo> list) {
		init(context);
		this.list = list;
	}

	private void init(Context context) {
		mContext = context;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public MarkInfo getItem(int pos) {
		return list.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int pos,  View convertView, ViewGroup arg2) {
		final ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_markitem, null);
			viewHolder = new ViewHolder();
			viewHolder.mtvName = (TextView) convertView
					.findViewById(R.id.marker_name);
			viewHolder.mRatBar = (RatingBar) convertView
					.findViewById(R.id.view_ratingbar);
			viewHolder.mtvInfo = (TextView) convertView
					.findViewById(R.id.mark_content);
			viewHolder.mtvTime = (TextView) convertView
					.findViewById(R.id.mark_time);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		switch (pos%4) {
		case 0:
			convertView.setBackgroundResource(R.drawable.selector_listitem_blue);
			break;
		case 1:
			convertView.setBackgroundResource(R.drawable.selector_listitem_green);
			break;
		case 2:
			convertView.setBackgroundResource(R.drawable.selector_listitem_red);
			break;
		case 3:
			convertView.setBackgroundResource(R.drawable.selector_listitem_yellow);
			break;
		default:
			break;
		}

		MarkInfo info = list.get(pos);
		
		viewHolder.mtvName.setText(info.getMarker());
		viewHolder.mtvInfo.setText(info.getMarkContent());
		
		//格式化时间
		SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA); 
		
		viewHolder.mtvTime.setText(time.format(info.getUpdatedAt()));
		viewHolder.mRatBar.setRating(Float.valueOf(info.getMarkRate()));
		return convertView;
	}


	public static class ViewHolder {
		public TextView mtvName;
		public RatingBar mRatBar;
		public TextView mtvInfo;
		public TextView mtvTime;
	}

}
