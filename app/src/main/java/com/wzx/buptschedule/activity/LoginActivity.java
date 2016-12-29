package com.wzx.buptschedule.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.wzx.buptschedule.R;
import com.wzx.buptschedule.base.BaseSwitchFragment;
import com.wzx.buptschedule.fragment.LoginFgtPwdFragment;
import com.wzx.buptschedule.fragment.LoginFragment;
import com.wzx.buptschedule.fragment.LoginRegFragment;



public class LoginActivity extends FragmentActivity implements BaseSwitchFragment.FragmentCallBack {


	private static final String TAG = LoginActivity.class.getSimpleName();

	// Jump to the LoginFragment
	public static final int JUMP_2_LOGIN = 0X988;
	// Jump to the LoginRegFragment
	public static final int JUMP_2_REG = JUMP_2_LOGIN + 1;
	// Jump to the LoginFgtPwdFragment
	public static final int JUMP_2_FRG = JUMP_2_REG + 1;
	
	//Current fragment
	private BaseSwitchFragment mSwitchFragment;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		if (savedInstanceState == null) {
			
			mSwitchFragment = new LoginFragment().newInstance(this);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, mSwitchFragment)
					.commit();
		}
	}

	/**
	 * Login
	 * 
	 * @param view
	 */
	public void doRegister(View view) {
		changeFragment(new LoginFragment().newInstance(this),
				R.anim.push_left_in, R.anim.fade_out);
	}

	/**
	 * Find password
	 * 
	 * @param view
	 */
	public void doFindPwd(View view) {
		changeFragment(
				new LoginFgtPwdFragment().newInstance(this),
				R.anim.push_left_in, R.anim.fade_out);
	}

	private void changeFragment(BaseSwitchFragment targetFragment,
			int showAnim, int fadeAnim) {
		getSupportFragmentManager()
				.beginTransaction()
				.setCustomAnimations(showAnim, fadeAnim, R.anim.push_left_in,
						R.anim.push_left_out)
				.replace(R.id.container, targetFragment, "fragment")
				.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commit();
		mSwitchFragment = targetFragment;
	}

	@Override
	public void fragmentCallBack(int targetID, Bundle data) {
		switch (targetID) {
		case JUMP_2_LOGIN:
			changeFragment(new LoginFragment().newInstance(this), R.anim.fade_in,
					R.anim.push_left_out);
			break;
		case JUMP_2_REG:
			changeFragment(
					new LoginRegFragment().newInstance(this), R.anim.push_left_in,
					R.anim.fade_out);
			break;
		case JUMP_2_FRG:
			changeFragment(
					new LoginFgtPwdFragment().newInstance(this), R.anim.push_left_in,
					R.anim.fade_out);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		mSwitchFragment.doFragmentBackPressed();
	}

}
