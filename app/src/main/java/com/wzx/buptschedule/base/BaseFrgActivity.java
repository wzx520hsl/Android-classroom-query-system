package com.wzx.buptschedule.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.avos.avoscloud.AVUser;
import com.wzx.buptschedule.R;
import com.wzx.buptschedule.TasApp;
import com.wzx.buptschedule.bean.UserInfo;


public class BaseFrgActivity extends FragmentActivity {

  public BaseFrgActivity activity;
  public TasApp mApp;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = this;
    mApp = (TasApp) activity.getApplication();
  }
  
  /**
   * Get user objectId
   * @return
   */
  public AVUser getAVUser() {
    return mApp.getAVUser();
  }

  /**
   * Get the account objectId
   * @return
   */
  public String getUserId() {
    return mApp.getUserId();
  }
  
  /**
   * Get the user information
   * @return
   */
  public UserInfo getUserInfo(){
	  return mApp.getUserInfo(); 
  }
  
  /**
   * Get user name
   * @return
   */
  public String getUserName(){
	  return mApp.getUserName(); 
  }


  public void showError(String errorMessage) {
    showError(errorMessage, activity);
  }

  
  /**
   * error dialog
   * @param errorMessage
   * @param activity
   */
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

  protected void onPause() {
    super.onPause();
  }

  protected void onResume() {
    super.onResume();
  }
}
