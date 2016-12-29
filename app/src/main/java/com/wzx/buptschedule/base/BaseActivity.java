package com.wzx.buptschedule.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.avos.avoscloud.AVUser;
import com.wzx.buptschedule.R;


public class BaseActivity extends Activity {

  public BaseActivity activity;
  private String userId;
  private String userName;
  private AVUser currentUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = this;
    userId = null;
    currentUser = AVUser.getCurrentUser();
    if (currentUser != null) {
      userId = currentUser.getObjectId();
      userName = currentUser.getUsername();
    }
  }

  public String getUserId() {
    return userId;
  }

  public String getUserName(){
	  return userName; 
  }

  protected void showError(String errorMessage) {
    showError(errorMessage, activity);
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
