package com.wzx.buptschedule.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.wzx.buptschedule.R;


public class WelcomeActivity extends Activity {

	private final long SPLASH_LENGTH = 2200;
	Handler handler = new Handler();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		handler.postDelayed(new Runnable() {

			public void run() {
				Intent intent = new Intent(WelcomeActivity.this,
						LoginActivity.class);
				startActivity(intent);
				finish();
			}
		}, SPLASH_LENGTH);// After 3 seconds, the activity will jump to the LoginActivity

	}

}
