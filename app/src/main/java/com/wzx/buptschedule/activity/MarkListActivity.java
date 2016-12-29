package com.wzx.buptschedule.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.wzx.buptschedule.R;
import com.wzx.buptschedule.adapter.MarkListAdapter;
import com.wzx.buptschedule.base.BaseActivity;
import com.wzx.buptschedule.bean.MarkInfo;
import com.wzx.buptschedule.service.AVService;
import com.wzx.buptschedule.util.C;
import com.wzx.buptschedule.util.LogTrace;
import com.wzx.buptschedule.util.SingleToast;
import com.wzx.buptschedule.widget.ShowHideOnScroll;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MarkListActivity extends BaseActivity implements OnClickListener {

	private static final String TAG = MarkListActivity.class.getSimpleName();


	private String mMenu = "";
	private ProgressDialog mProgDialog;
	private Dialog progressDialog;
	private MarkListAdapter mListAdapter;
	private Dialog dialog;

	private View mFastBtn;
	private TextView mTvTitle;
	private ListView mListView;
	private TextView mTxEmpty;
	private RadioGroup mRgRank;


	private ImageView mImgBack;

	private List<MarkInfo> mList;
	private List<MarkInfo> mListByTime;

	@Override
	protected void onCreate(Bundle state) {
		super.onCreate(state);
		setContentView(R.layout.activity_marklist);

		initViews();
		initData();
	}

	private void initViews() {
		mFastBtn = findViewById(R.id.fab);
		mTvTitle = (TextView) findViewById(R.id.tx_title);
		mTxEmpty = (TextView) findViewById(R.id.tx_empty);
		mListView = (ListView) findViewById(R.id.marklist);
		mImgBack = (ImageView) findViewById(R.id.img_back);
		mRgRank = (RadioGroup) findViewById(R.id.gr_rank);
		mRgRank.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// 获取变更后的选中项的ID
				switch (arg1) {
				case R.id.rbbyTime:
					sortByTime();
					break;
				case R.id.rbbyRate:
					sortByRate();
					break;
				default:
					break;
				}
			}
		});
	}

	private void initData() {

		Intent it = getIntent();
		mMenu = it.getStringExtra(C.INTENT_TYPE.DATA_MENUTYPE_NAME);
		
		mTvTitle.setText(mMenu);
		mListView.setOnTouchListener(new ShowHideOnScroll(mFastBtn));
		mFastBtn.setOnClickListener(this);
		mImgBack.setOnClickListener(this);
		
		if(MainActivity.mUserTypeMode == 2){
			//商户没有评论按钮
			mFastBtn.setVisibility(View.GONE);
		}
		
		//从服务端获取评论
		new RemoteDataTask().execute();
	}

	
	/**
	 * 根据评分排序
	 * @return
	 */
	private void sortByRate(){
		 Collections.reverse(mList);  //按照评分降序排列 
			// 刷新ListView
			mListAdapter = new MarkListAdapter(activity, mList);
			mListAdapter.notifyDataSetChanged();
			mListView.setAdapter(mListAdapter);
	}
	
	/**
	 * 根据时间排序
	 * @return
	 */
	private void sortByTime(){
		mList.clear();
		mList.addAll(mListByTime);//按照更新时间排列
		// 刷新ListView
		mListAdapter = new MarkListAdapter(activity, mList);
		mListAdapter.notifyDataSetChanged();
		mListView.setAdapter(mListAdapter);
	}
	
	/**
	 * 显示点评对话框
	 */
	private void showMarkDialog() {
		dialog = new Dialog(this, R.style.DialogStyle);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.view_mark_dialog);
		final EditText dialogEtContent = (EditText) dialog
				.findViewById(R.id.et_content);
		final TextView txTitle = (TextView) dialog.findViewById(R.id.tx_title);
		final RatingBar ratingBar = (RatingBar) dialog
				.findViewById(R.id.view_ratingbar);

		String mTarg = String.format(getString(R.string.mark_info), mMenu);
		txTitle.setText(mTarg);

		Button sure = (Button) dialog.findViewById(R.id.btn);
		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String tar = dialogEtContent.getEditableText().toString();
				if (TextUtils.isEmpty(tar)) {
					SingleToast.showToast(activity, "Content cannot be empty!", 2000);
				} else {
					mProgDialog = ProgressDialog.show(activity, "",
							"Uploading data....");
					
					AVService.createMarkInfo(getUserName(), mMenu, tar,
							String.valueOf(ratingBar.getRating()),
							new SaveCallback() {

								@Override
								public void done(AVException e) {
									if (e == null) {
										SingleToast.showToast(activity,
												"Upload success", 2000);
									} else {
										SingleToast.showToast(activity,
												"Upload fail", 2000);
									}
									mProgDialog.dismiss();
									dialog.dismiss();
									new RemoteDataTask().execute();

								}

							});

				}

			}
		});

		dialog.show();

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.fab:
			showMarkDialog();
			break;
		case R.id.img_back:
			MarkListActivity.this.finish();
			activity.overridePendingTransition(R.anim.push_up_in, R.anim.push_down_out);
			break;
		default:
			break;
		}

	}

	// 远程获取数据
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			mList = AVService.findMarkInfos(mMenu);
			mListByTime = new ArrayList<MarkInfo>();
			mListByTime.clear();
			mListByTime.addAll(mList);
			return null;
		}

		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(MarkListActivity.this, "", "Loading the data...",
					true);
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values) {

			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Void result) {
			// 展现ListView
			mListAdapter = new MarkListAdapter(activity, mList);
			mListView.setAdapter(mListAdapter);
			progressDialog.dismiss();
			if (mList != null && !mList.isEmpty()) {
				mTxEmpty.setVisibility(View.INVISIBLE);
				mListView.setVisibility(View.VISIBLE);
				if (MainActivity.mUserTypeMode == 1) {
					// 是管理员的话可以长按出现管理菜单
					registerForContextMenu(mListView);
				}
				LogTrace.d(TAG, "onPostExecute", "mTxEmpty  hide");
			} else {
				mListView.setVisibility(View.GONE);
				
					//显示抢沙发提示
					mTxEmpty.setVisibility(View.VISIBLE);
					if(MainActivity.mUserTypeMode == 2){
						mTxEmpty.setText(R.string.mark_empty_business);
					}else{
					mTxEmpty.setText(R.string.mark_empty);
					mTxEmpty.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// 发表评论
							showMarkDialog();
						}
					});
					}

				LogTrace.d(TAG, "onPostExecute", "mTxEmpty  show");
			}
		}
	}

	private static final int DELETE_ID = Menu.FIRST;

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		final MarkInfo markInfo = mList.get(info.position);
		menu.setHeaderTitle(markInfo.getMarkContent());
		menu.add(0, DELETE_ID, 0, R.string.data_del);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		// 从服务器删除该数据
		final MarkInfo markInfo = mList.get(info.position);

		switch (item.getItemId()) {
		case DELETE_ID:
			LogTrace.d(TAG, "onContextItemSelected", "DELETE_ID");
			new RemoteDataTask() {
				@Override
				protected Void doInBackground(Void... params) {
					try {
						markInfo.delete();
					} catch (AVException e) {
					}
					super.doInBackground();
					return null;
				}
			}.execute();
			return true;
		}
		return super.onContextItemSelected(item);
	}

}
