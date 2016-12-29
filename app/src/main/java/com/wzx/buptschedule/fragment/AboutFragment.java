package com.wzx.buptschedule.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.wzx.buptschedule.R;




public class AboutFragment extends Fragment{

//	private static final String TAG = AboutFragment.class.getSimpleName();

	private View rootView;
	
	private ImageView logo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_about, container, false);
		initViews();
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	private void initViews() {
		logo= (ImageView)rootView.findViewById(R.id.btn_back);
		shake();
	}

	private void shake() {
		logo.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake));
	}

}
