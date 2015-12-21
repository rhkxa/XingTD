package com.gps808.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gps808.app.utils.BaseFragment;

public class WeatherFragment extends BaseFragment {

	private String vid;

	public static WeatherFragment newInstance(String id) {
		WeatherFragment fragment = new WeatherFragment();
		fragment.vid = id;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}
