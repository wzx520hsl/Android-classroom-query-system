package com.wzx.buptschedule.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.avoscloud.leanchatlib.utils.Constants;
import com.wzx.buptschedule.R;
import com.wzx.buptschedule.activity.ChatRoomActivity;
import com.wzx.buptschedule.activity.MarkListActivity;
import com.wzx.buptschedule.adapter.FreeRoomsAdapter;
import com.wzx.buptschedule.base.BaseFragment;
import com.wzx.buptschedule.bean.FreeRoom;
import com.wzx.buptschedule.util.C;
import com.wzx.buptschedule.widget.FreeRoomsListView;
import com.wzx.buptschedule.widget.StickyLayout;

public class FreeRoomsFragment extends BaseFragment implements
		ExpandableListView.OnChildClickListener,
		ExpandableListView.OnGroupClickListener, FreeRoomsListView.OnHeaderUpdateListener,
		StickyLayout.OnGiveUpTouchEventListener {


	private FreeRoomsListView expandableListView;
	private TextView mTxStick;
	private StickyLayout stickyLayout;
	private FreeRoomsAdapter adapter;

	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_freerooms, container, false);
		initViews();
		return rootView;
	}


	private void initViews(){
		expandableListView = (FreeRoomsListView) rootView.findViewById(R.id.expandablelist);
		mTxStick = (TextView) rootView.findViewById(R.id.tx_stick);

		stickyLayout = (StickyLayout) rootView.findViewById(R.id.sticky_layout);

		adapter = new FreeRoomsAdapter(mAct, mApp.mFreeRoomsList);
		expandableListView.setAdapter(adapter);

		mTxStick.setText((mApp.mFreeRoomsList.size()!=0&&mApp.mFreeRoomsList.get(0)==null)?"今日无空闲教室":mApp.mFreeRoomsList.get(0).getStatus());

		expandableListView.setOnHeaderUpdateListener(this);
		expandableListView.setOnChildClickListener(this);
		expandableListView.setOnGroupClickListener(this);
		stickyLayout.setOnGiveUpTouchEventListener(this);

	}



	@Override
	public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2,
			long arg3) {
		return false;
	}

	@Override
	public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2,
			int arg3, long arg4) {
		showMenu(mApp.mFreeRoomsList.get(arg2).getRooms().get(arg3));
		return true;
	}


	@Override
	public boolean giveUpTouchEvent(MotionEvent event) {
		if (expandableListView.getFirstVisiblePosition() == 0) {
			View view = expandableListView.getChildAt(0);
			if (view != null && view.getTop() >= 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public View getPinnedHeader() {
		View headerView = mAct.getLayoutInflater().inflate(
				R.layout.exlistview_freerooms_head, null);
		headerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
				AbsListView.LayoutParams.WRAP_CONTENT));
		return headerView;
	}

	@Override
	public void updatePinnedHeader(View headerView, int firstVisibleGroupPos) {

	}

	private void showMenu(final FreeRoom freeRoom) {
		String[] items = { getString(R.string.menu_mark), getString(R.string.menu_chat) };
		ListAdapter menuAdapter = new ArrayAdapter<String>(mAct,
				R.layout.screen_manager_menu, items);
		String title = String.format(getString(R.string.menu_title_choice),freeRoom.getRoom());
		new AlertDialog.Builder(mAct).setTitle(title)
				.setAdapter(menuAdapter, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							//Classroom comment
							Intent itMark = new Intent(mAct,
									MarkListActivity.class);
							itMark.putExtra(
									C.INTENT_TYPE.DATA_MENUTYPE_NAME,
									freeRoom.getRoom());
							startActivity(itMark);
							mAct.overridePendingTransition(R.anim.push_down_in, R.anim.push_up_out);
						} else if (which == 1) {
							//Classroom chat
							Intent intent = new Intent(mAct, ChatRoomActivity.class);
							System.out.println( freeRoom.getRoom());
							intent.putExtra(Constants.CONVERSATION_ID, freeRoom.getRoom());
							intent.putExtra(Constants.MEMBER_ID, getUserInfo().getObjectId());
							startActivity(intent);
						}
					}
				}).show();
	}

}
