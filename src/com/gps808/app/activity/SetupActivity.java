package com.gps808.app.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.bean.XbOption;
import com.gps808.app.dialog.WheelDialog;
import com.gps808.app.dialog.WheelDialog.OnWheelClickListener;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.PreferenceUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.view.wheelview.WheelCurvedPicker;

public class SetupActivity extends BaseActivity {

	private XbOption option = new XbOption();
	private TextView setup_monitor_time;
	private TextView setup_track_time;
	private LinearLayout setup_monitor, setup_track;
	private WheelDialog wheelDialog;
	private List<String> data;
	private List<Integer> timeList;
	private int mTime, tTime;

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
		wheelDialog = new WheelDialog(this);
		data = Arrays.asList(getResources()
				.getStringArray(R.array.refresh_time));
		timeList = new ArrayList<Integer>();
		timeList.add(10);
		timeList.add(30);
		timeList.add(60);
		timeList.add(300);
		timeList.add(0);
		setup_monitor_time = (TextView) findViewById(R.id.setup_monitor_time);
		setup_track_time = (TextView) findViewById(R.id.setup_track_time);
		setup_monitor = (LinearLayout) findViewById(R.id.setup_monitor);
		setup_track = (LinearLayout) findViewById(R.id.setup_track);
		setup_monitor.setOnClickListener(click);
		setup_track.setOnClickListener(click);
	}

	private OnClickListener click = new OnClickListener() {

		@Override
		public void onClick(final View arg0) {
			// TODO Auto-generated method stub
			WheelCurvedPicker curvedPicker = new WheelCurvedPicker(
					SetupActivity.this);
			curvedPicker.setData(data);
			curvedPicker.setCurrentTextColor(getResources().getColor(
					R.color.app_blue));
			wheelDialog.setOnWheelClickListener(new OnWheelClickListener() {

				@Override
				public void onWheelOk(int index, String key) {
					// TODO Auto-generated method stub
					switch (arg0.getId()) {
					case R.id.setup_monitor:
						setup_monitor_time.setText(key);
						break;
					case R.id.setup_track:
						setup_track_time.setText(key);
						break;
					}
				}
			});
			wheelDialog.setContentView(curvedPicker);
			wheelDialog.show();

		}
	};

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
		// {"monitorInterval":10,"trackInterval":10}
		String url = UrlConfig.getUserSetOptions();
		JSONObject params = new JSONObject();
		StringEntity entity = null;
		try {
			params.put("monitorInterval", mTime);
			params.put("trackInterval", tTime);
			entity = new StringEntity(params.toString(), "UTF-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpUtil.post(SetupActivity.this, url, entity, "application/json",
				new jsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						super.onSuccess(statusCode, headers, response);
					}
				});

	}

}
