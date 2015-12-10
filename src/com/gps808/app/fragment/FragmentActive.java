package com.gps808.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.gps808.app.R;
import com.gps808.app.adapter.ActiveGridViewAdapter;
import com.gps808.app.utils.BaseFragment;

public class FragmentActive extends BaseFragment {
	private View view;

	private ActiveGridViewAdapter mAdapter;
	private ImageButton main_head_search;

	public static FragmentActive newInstance() {
		FragmentActive fragment = new FragmentActive();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_active, null);
		return view;
	}

}
