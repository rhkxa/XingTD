package com.gps808.app.activity;

import org.apache.http.Header;
import org.json.JSONObject;

import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.bean.XbOption;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.Common;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.PreferenceUtils;
import com.gps808.app.utils.UrlConfig;

public class SetupActivity extends BaseActivity {

	private XbOption option = new XbOption();
	private TextView setup_monitor_time;
	private TextView setup_track_time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup);
		init();
		getData();
	}

	private void init() {
		// TODO Auto-generated method stub
		HeaderFragment headerFragment = (HeaderFragment) this
				.getSupportFragmentManager().findFragmentById(R.id.title);
		headerFragment.setTitleText("设置");
		setup_monitor_time = (TextView) findViewById(R.id.setup_monitor_time);
		setup_track_time = (TextView) findViewById(R.id.setup_track_time);
	}

	private void getData() {
		String url = UrlConfig.getUserOptions();
		HttpUtil.get(SetupActivity.this, url, new jsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				option = JSON.parseObject(response.toString(), XbOption.class);
				setup_monitor_time.setText(option.getMonitorInterval() + "s");
				setup_track_time.setText(option.getTrackInterval() + "s");
				PreferenceUtils.getInstance(SetupActivity.this).setMonitorTime(
						option.getMonitorInterval());
				PreferenceUtils.getInstance(SetupActivity.this).setTrackTime(
						option.getTrackInterval());
				super.onSuccess(statusCode, headers, response);
			}
		});
	}

	private void setData() {

		String url = UrlConfig.getUserSetOptions();
	}

}
