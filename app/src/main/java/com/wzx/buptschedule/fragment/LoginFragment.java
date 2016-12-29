package com.wzx.buptschedule.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.wzx.buptschedule.R;
import com.wzx.buptschedule.TasApp;
import com.wzx.buptschedule.activity.LoginActivity;
import com.wzx.buptschedule.activity.MainActivity;
import com.wzx.buptschedule.base.BaseSwitchFragment;
import com.wzx.buptschedule.bean.UserInfo;
import com.wzx.buptschedule.service.AVService;
import com.wzx.buptschedule.util.C;
import com.wzx.buptschedule.util.SPUtils;
import com.wzx.buptschedule.util.SingleToast;


/**
 * Login Fragment
 *
 * @author wzx
 */
public class LoginFragment extends BaseSwitchFragment implements OnClickListener {

    private Button mBtnLogin;
    private TextView mBtnReg;
    private TextView mBtnFrgpwd;
    private EditText mEtUserName;
    private EditText mEtPwd;
    private ProgressDialog mPrgDialog;
    private CheckBox mCkRemberPwd;
    private String clientId;
    private TasApp mApp;

    private int back = 0;// To judge how many backs have been pressed

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container,
                false);
        mApp = (TasApp) mAct.getApplication();

        mBtnLogin = (Button) rootView.findViewById(R.id.button_login);
        mBtnReg = (TextView) rootView.findViewById(R.id.button_register);
        mBtnFrgpwd = (TextView) rootView
                .findViewById(R.id.button_forget_password);
        mEtUserName = (EditText) rootView
                .findViewById(R.id.editText_userName);
        mEtPwd = (EditText) rootView
                .findViewById(R.id.editText_userPassword);
        mCkRemberPwd = (CheckBox) rootView.findViewById(R.id.cb_rememberpwd);

        mCkRemberPwd.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                SPUtils.setParam(mAct, C.SP.remember_login, arg1);
            }
        });
        //Check whether password and username has been remembered
        Boolean isRemember = (Boolean) SPUtils.getParam(mAct,
                C.SP.remember_login, true);
        mCkRemberPwd.setChecked(isRemember);
        if (isRemember) {
            String userAccount = (String) SPUtils.getParam(mAct,
                    C.SP.account, "");
            String userPassword = (String) SPUtils.getParam(mAct, C.SP.pwd,
                    "");

            if (!TextUtils.isEmpty(userAccount)
                    && !TextUtils.isEmpty(userPassword)) {
                mEtUserName.setText(userAccount);
                mEtPwd.setText(userPassword);



            }

        }

        mBtnLogin.setOnClickListener(this);
        mBtnReg.setOnClickListener(this);
        mBtnFrgpwd.setOnClickListener(this);

        mAct.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        return rootView;

    }


    // hide the process dialog
    private void progressDialogDismiss() {
        if (mPrgDialog != null)
            mPrgDialog.dismiss();
    }

    // show the process dialog
    private void progressDialogShow() {
        mPrgDialog = ProgressDialog
                .show(mAct,
                        mAct.getResources().getText(
                                R.string.dialog_message_title),
                        mAct.getResources().getText(
                                R.string.dialog_text_wait), true, false);
    }

    // show the login error
    private void showLoginError() {
        new AlertDialog.Builder(mAct)
                .setTitle(
                        mAct.getResources().getString(
                                R.string.dialog_error_title))
                .setMessage(
                        mAct.getResources().getString(
                                R.string.error_login_error))
                .setNegativeButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).show();
    }

    // show the password error dialog
    private void showUserPasswordEmptyError() {
        new AlertDialog.Builder(mAct)
                .setTitle(
                        mAct.getResources().getString(
                                R.string.dialog_error_title))
                .setMessage(
                        mAct.getResources().getString(
                                R.string.error_register_password_null))
                .setNegativeButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).show();
    }

    // show username blank dialog
    private void showUserNameEmptyError() {
        new AlertDialog.Builder(mAct)
                .setTitle(
                        mAct.getResources().getString(
                                R.string.dialog_error_title))
                .setMessage(
                        mAct.getResources().getString(
                                R.string.error_register_user_name_null))
                .setNegativeButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).show();
    }

    public void doFragmentBackPressed() {
        back++;
        switch (back) {
            case 1:
                SingleToast.showToast(mAct, R.string.exit_again, 3000);
                // 3秒内都可以退出应用
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        back = 0;
                    }
                }, 3000);
                break;
            case 2:
                back = 0;
                if (TextUtils.isEmpty(mEtUserName.getText())
                        || TextUtils.isEmpty(mEtPwd.getText())) {
                    SPUtils.setParam(mApp, C.SP.remember_login, false);
                }
                mAct.finish();
                android.os.Process.killProcess(android.os.Process.myPid());// 关闭进程
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                mApp.getRooms();//get the empty classroom information again if fail
                final String username = mEtUserName.getText().toString();
                if (username.isEmpty()) {
                    showUserNameEmptyError();
                    return;
                }
                String pwd = mEtPwd.getText().toString();
                if (pwd.isEmpty()) {
                    showUserPasswordEmptyError();
                    return;
                }
                progressDialogShow();
                AVUser.logInInBackground(username, pwd, new LogInCallback<AVUser>() {
                    public void done(AVUser user, AVException e) {
                        if (user != null) {

                            new getUserInfoTask().execute(mEtUserName.getText().toString());

                        } else {
                            progressDialogDismiss();
                            showLoginError();
                        }
                    }
                });
                break;
            case R.id.button_register:
                mFragmentCallBack.fragmentCallBack(LoginActivity.JUMP_2_REG, null);
                break;
            case R.id.button_forget_password:
                mFragmentCallBack.fragmentCallBack(LoginActivity.JUMP_2_FRG, null);
                break;
            default:
                break;
        }

    }




    // get the user information from server
    private class getUserInfoTask extends AsyncTask<String, Void, UserInfo> {
        @Override
        protected UserInfo doInBackground(String... params) {
            UserInfo info = AVService.getUserInfo(params[0]);
            return info;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(UserInfo info) {
            // check succeed
            if (info != null) {
                mApp.refCurrUser();


                mApp.setUserInfo(info, AVUser.getCurrentUser());


                ChatManager.getInstance().openClient(null);
                //enter the mainActivity
                if (mCkRemberPwd.isChecked()) {
                    SPUtils.setParam(mAct, C.SP.account,
                            mEtUserName.getText().toString());
                    SPUtils.setParam(mAct, C.SP.pwd,
                            mEtPwd.getText().toString());
                } else {
                    SPUtils.setParam(mAct, C.SP.account, "");
                    SPUtils.setParam(mAct, C.SP.pwd, "");
                }

                progressDialogDismiss();

                Intent mainIntent = new Intent(mAct, MainActivity.class);
                startActivity(mainIntent);
                mAct.finish();

            } else {
                // login fail
            }
        }
    }
}
