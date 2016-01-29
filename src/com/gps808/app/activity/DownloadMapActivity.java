package com.gps808.app.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.gps808.app.R;

import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.fragment.OffLineMapFragment;
import com.gps808.app.fragment.OnLineMapFragment;

import com.gps808.app.utils.BaseActivity;


public class DownloadMapActivity extends BaseActivity {
	private RadioGroup map_rg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offline_map);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		HeaderFragment headerFragment = (HeaderFragment) this
				.getSupportFragmentManager().findFragmentById(R.id.title);
		headerFragment.setTitleText("地图下载");
		map_rg = (RadioGroup) findViewById(R.id.map_rg);
		map_rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				Fragment fragment = null;
				switch (arg0.getCheckedRadioButtonId()) {
				case R.id.online_map:
					fragment = new OnLineMapFragment();
					break;

				case R.id.offline_map:
					fragment=new OffLineMapFragment();
					break;
				}
				getSupportFragmentManager().beginTransaction().replace(
						R.id.show_map, fragment).commit();
			}
		});
		getSupportFragmentManager().beginTransaction().replace(
				R.id.show_map, new OnLineMapFragment()).commit();
	}
}