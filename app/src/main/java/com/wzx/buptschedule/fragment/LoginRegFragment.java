package com.wzx.buptschedule.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SignUpCallback;
import com.wzx.buptschedule.R;
import com.wzx.buptschedule.activity.LoginActivity;
import com.wzx.buptschedule.base.BaseSwitchFragment;
import com.wzx.buptschedule.bean.TeacherCode;
import com.wzx.buptschedule.bean.UserInfo;
import com.wzx.buptschedule.service.AVService;

/**
 * Register fragment
 * 
 * @author wzx
 * 
 */
public class LoginRegFragment extends BaseSwitchFragment implements
		OnClickListener {

	private Button mBtnReg;
	private EditText mEtUserName;
	private EditText mEtUserNickName;
	private EditText mEtUserEmail;
	private EditText mEtUserPhone;
	private EditText mEtUserPassword;
	private EditText mEtUserPasswordAgain;
	private TextView mTxBack;
	private EditText mEtUserTeacherCode;
	private ProgressDialog progressDialog;
	private String userType;

	private String userName;
	private String userNickName;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_register, container,
				false);
		mBtnReg = (Button) rootView.findViewById(R.id.button_i_need_register);
		mEtUserName = (EditText) rootView
				.findViewById(R.id.editText_register_username);
		mEtUserNickName = (EditText) rootView
				.findViewById(R.id.editText_register_nickname);
		mEtUserEmail = (EditText) rootView
				.findViewById(R.id.editText_register_email);
		mEtUserPhone = (EditText) rootView
				.findViewById(R.id.editText_register_phone);
		mEtUserPassword = (EditText) rootView
				.findViewById(R.id.editText_register_userPassword);
		mEtUserPasswordAgain = (EditText) rootView
				.findViewById(R.id.editText_register_userPassword_again);
		mEtUserTeacherCode = (EditText) rootView
				.findViewById(R.id.editText_register_manager);
		mTxBack = (TextView) rootView.findViewById(R.id.rl_top_btn_back);

		userType = UserInfo.S_STUDENT;

		mBtnReg.setOnClickListener(this);
		mTxBack.setOnClickListener(this);



		return rootView;
	}

	// start ot reister
	public void register() {
		SignUpCallback signUpCallback = new SignUpCallback() {
			public void done(AVException e) {
				progressDialogDismiss();
				if (e == null) {
					AVService.createOrUpdateUserInfo("", userName,
							userNickName, mEtUserPhone.getText().toString(),
							userType, null);
					showRegisterSuccess();
				} else {
					switch (e.getCode()) {
					case 202:
						showError(mAct
								.getString(R.string.error_register_user_name_repeat));
						break;
					case 203:
						showError(mAct
								.getString(R.string.error_register_email_repeat));
						break;
					default:
						showError(mAct.getString(R.string.network_error));
						break;
					}
				}
			}
		};
		String password = mEtUserPassword.getText().toString();
		String email = mEtUserEmail.getText().toString();
		userName = mEtUserName.getText().toString();
		userNickName = mEtUserNickName.getText().toString();
		AVService.signUp(userName, userNickName, password, email,
				signUpCallback);
	}

	private void progressDialogDismiss() {
		if (progressDialog != null)
			progressDialog.dismiss();
	}

	private void progressDialogShow() {
		progressDialog = ProgressDialog
				.show(mAct,
						mAct.getResources().getText(
								R.string.dialog_message_title),
						mAct.getResources().getText(
								R.string.dialog_text_wait), true, false);
	}

	private void showRegisterSuccess() {
		new AlertDialog.Builder(mAct)
				.setTitle(
						mAct.getResources().getString(
								R.string.dialog_message_title))
				.setMessage(
						mAct.getResources().getString(
								R.string.success_register_success))
				.setNegativeButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								mFragmentCallBack.fragmentCallBack(
										LoginActivity.JUMP_2_LOGIN, null);
							}
						}).show();
	}

	// Check the register information
	private void doRegVerfy() {
		if (mEtUserPassword.getText().toString()
				.equals(mEtUserPasswordAgain.getText().toString())) {
			if (!mEtUserPassword.getText().toString().isEmpty()) {
				if (!mEtUserName.getText().toString().isEmpty()) {
					if (!mEtUserEmail.getText().toString().isEmpty()) {

						if (userType.equals(UserInfo.S_TEACHER)) {

							regManager();
						} else {
							progressDialogShow();
							register();
						}

					} else {
						showError(mAct
								.getString(R.string.error_register_email_address_null));
					}
				} else {
					showError(mAct
							.getString(R.string.error_register_user_name_null));
				}
			} else {
				showError(mAct
						.getString(R.string.error_register_password_null));
			}
		} else {
			showError(mAct
					.getString(R.string.error_register_password_not_equals));
		}
	}


	private void regManager() {
		new VerfiyCodeTask().execute();

	}
	private class VerfiyCodeTask extends AsyncTask<Void, Void, TeacherCode> {
		@Override
		protected TeacherCode doInBackground(Void... params) {
			TeacherCode verifyCode = AVService.findManagerCode();
			return verifyCode;
		}

		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(mAct, "", "正在验证教师邀请码...",
					true);
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values) {

			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(TeacherCode verifyCode) {
			progressDialog.dismiss();
			if (verifyCode != null
					&& verifyCode.getCode().equals(
							mEtUserTeacherCode.getEditableText().toString())) {
				//Check success
				progressDialogShow();
				register();
			} else {
				// Check fail
				showError(mAct
						.getString(R.string.error_register_manager_codeerror));
			}

		}
	}

	public void doFragmentBackPressed() {
		mFragmentCallBack.fragmentCallBack(LoginActivity.JUMP_2_LOGIN, null);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.button_i_need_register:
			doRegVerfy();
			break;
		case R.id.rl_top_btn_back:
			AVService.logout();
			mFragmentCallBack
					.fragmentCallBack(LoginActivity.JUMP_2_LOGIN, null);
			break;
		default:
			break;
		}

	};

}
