package com.gps808.app.adapter;

import java.util.List;

import com.gps808.app.bean.BnSlider;
import com.gps808.app.fragment.MainImageFragment;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainFragmentAdapter extends FragmentPagerAdapter {

	private List<BnSlider> data;

	public MainFragmentAdapter(FragmentManager fm, List<BnSlider> data) {

		super(fm);
		this.data = data;
	}

	@Override
	public Fragment getItem(int position) {
		return MainImageFragment.newInstance(data.get(position));
	}

	@Override
	public int getCount() {
		//return dataList == null ? 0 : dataList.size();
		return data.size();
	}
	

}
