package com.wzx.buptschedule.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avoscloud.leanchatlib.activity.AVBaseActivity;
import com.avoscloud.leanchatlib.utils.Constants;
import com.avoscloud.leanchatlib.utils.PhotoUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wzx.buptschedule.R;
import com.wzx.buptschedule.TasApp;
import com.wzx.buptschedule.bean.UserInfo;
import com.wzx.buptschedule.util.UserCacheUtils;


public class ContactPersonInfoActivity extends AVBaseActivity implements OnClickListener {
  public static final String USER_ID = "userId";
  TextView usernameView;
  ImageView avatarView, avatarArrowView;
  LinearLayout allLayout;

  RelativeLayout avatarLayout;

  String userId = "";
  UserInfo user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.contact_person_info_activity);
    initData();

    findView();
    initView();
  }

  private void initData() {
    userId = getIntent().getStringExtra(Constants.LEANCHAT_USER_ID);
    user = UserCacheUtils.getCachedUser(userId);
  }

  private void findView() {
    allLayout = (LinearLayout) findViewById(R.id.all_layout);
    avatarView = (ImageView) findViewById(R.id.avatar_view);
    avatarArrowView = (ImageView) findViewById(R.id.avatar_arrow);
    usernameView = (TextView) findViewById(R.id.username_view);
    avatarLayout = (RelativeLayout) findViewById(R.id.head_layout);
  }

  private void initView() {
    UserInfo curUser = ((TasApp)getApplication()).getUserInfo();
    if (curUser.equals(user)) {
      setTitle(R.string.contact_personalInfo);
      avatarLayout.setOnClickListener(this);
      avatarArrowView.setVisibility(View.VISIBLE);
    } else {
      setTitle(R.string.contact_detailInfo);
      avatarArrowView.setVisibility(View.INVISIBLE);
    }
    updateView(user);
  }

  private void updateView(UserInfo user) {
    ImageLoader.getInstance().displayImage(user.getAvatarUrl(), avatarView, PhotoUtils.avatarImageOptions);
    usernameView.setText(user.getUserName());
  }

  @Override
  public void onClick(View v) {

  }
}
