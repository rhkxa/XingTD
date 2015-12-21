package com.gps808.app.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.SupportMapFragment;
import com.gps808.app.R;
import com.gps808.app.fragment.CarFragment;
import com.gps808.app.fragment.TrackFragment;
import com.gps808.app.fragment.TrailFragment;
import com.gps808.app.fragment.WeatherFragment;
import com.gps808.app.utils.BaseActivity;

/**
 * 车辆的详情
 * 
 * @author JIA
 * 
 */
public class CarDetailsActivity extends BaseActivity {
	private FragmentManager mFragmentMan;
	private Fragment mContent;
	private RadioGroup group;

	private String vid;
	private int flag;
	private RadioButton car_trail, car_track, car_details, car_weather;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_details);
		init();

	}

	private void init() {
		// TODO Auto-generated method stub
		vid = getIntent().getStringExtra("vid");
		flag = getIntent().getIntExtra("flag", 0);
		final Fragment car = CarFragment.newInstance(vid);
		final WeatherFragment weather = WeatherFragment.newInstance(vid);
		final TrailFragment trail = TrailFragment.newInstance(vid);
		final TrackFragment track = TrackFragment.newInstance(vid);
		car_trail = (RadioButton) findViewById(R.id.car_trail);
		car_track = (RadioButton) findViewById(R.id.car_track);
		car_details = (RadioButton) findViewById(R.id.car_details);
		car_weather = (RadioButton) findViewById(R.id.car_weather);
		switch (flag) {
		case 0:
			car_track.setChecked(true);
			mContent = track;
			break;
		case 1:
			car_trail.setChecked(true);
			mContent = trail;
			break;
		case 2:
			car_details.setChecked(true);
			mContent = car;
			break;
		case 3:
			car_weather.setChecked(true);
			mContent = weather;
			break;

		}
		mFragmentMan = getSupportFragmentManager();

		mFragmentMan.beginTransaction().add(R.id.content, mContent).commit();
		group = (RadioGroup) findViewById(R.id.main_linearlayout_footer);
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub

				switch (arg1) {
				case R.id.car_trail:
					switchContent(mContent, trail);
					break;
				case R.id.car_track:
					switchContent(mContent, track);
					break;
				case R.id.car_details:
					switchContent(mContent, car);
					break;
				case R.id.car_weather:
					switchContent(mContent, weather);
					break;
				}
			}
		});
	}

	public void switchContent(Fragment from, Fragment to) {
		if (mContent != to) {
			mContent = to;
			FragmentTransaction transaction = mFragmentMan.beginTransaction();
			// .setCustomAnimations(android.R.anim.fade_in,
			// android.R.anim.fade_out);
			if (!to.isAdded()) { // 先判断是否被add过
				transaction.hide(from).add(R.id.content, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else {
				transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
			}
		}
	}

}
