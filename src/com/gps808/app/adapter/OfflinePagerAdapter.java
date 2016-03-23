package com.gps808.app.adapter;

import com.gps808.app.fragment.OffLineMapFragment;
import com.gps808.app.fragment.OnLineMapFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * ViewPager数据
 */
public class OfflinePagerAdapter extends FragmentPagerAdapter {

	public OfflinePagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		Fragment fragment = null;
		switch (arg0) {
		case 0:
			fragment = new OnLineMapFragment();
			break;
		case 1:
			fragment = new OffLineMapFragment();
			break;
		}
		return fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}

}
