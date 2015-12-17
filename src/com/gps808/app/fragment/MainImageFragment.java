package com.gps808.app.fragment;

import com.gps808.app.R;

import com.gps808.app.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public final class MainImageFragment extends Fragment {

	private View view;
	private String pager;

	public static MainImageFragment newInstance(String pager) {
		MainImageFragment fragment = new MainImageFragment();
		fragment.pager = pager;
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.widget_imageview, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
		
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			
			}
		});
		return view;
	}

}
