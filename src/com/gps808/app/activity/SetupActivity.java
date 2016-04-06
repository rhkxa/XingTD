package com.gps808.app.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.dialog.AlterPassWordDialog;
import com.gps808.app.dialog.AlterPassWordDialog.OnAlterClickListener;
import com.gps808.app.dialog.WheelDialog;
import com.gps808.app.dialog.WheelDialog.OnWheelClickListener;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.models.XbOption;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.CyptoUtils;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.PreferenceUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;
import com.gps808.app.view.FancyButton;
import com.gps808.app.view.wheelview.WheelCurvedPicker;

public class SetupActivity extends BaseActivity {

	private TextView setup_monitor_time;
	private TextView setup_track_time;
	private LinearLayout setup_monitor, setup_track;
	private WheelDialog wheelDialog;
	private List<String> data;
	private List<Integer> timeList;
	private int mTime, tTime;
	private LinearLayout reset_pass;
	private FancyButton exit_login;

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
		reset_pass = (LinearLayout) findViewById(R.id.reset_pass);
		reset_pass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlterPassWordDialog alter = new AlterPassWordDialog(
						SetupActivity.this);
				alter.setOnAlterClickListener(new OnAlterClickListener() {

					@Override
					public void onAlterOk(String oldPw, String newPw) {
						// TODO Auto-generated method stub
						if (newPw.length() < 6) {
							Utils.ToastMessage(SetupActivity.this, "密码不能小于6位");
							return;
						}

						setResetPw(oldPw, newPw);
					}
				});
				alter.show();
			}
		});
		exit_login = (FancyButton) findViewById(R.id.exit_login);
		exit_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Utils.exitLogin(SetupActivity.this);
				finish();
			}
		});
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
						mTime = timeList.get(index);
						break;
					case R.id.setup_track:
						setup_track_time.setText(key);
						tTime = timeList.get(index);
						break;
					}
					setIntervalTime();
				}
			});
			wheelDialog.setContentView(curvedPicker);
			wheelDialog.show();

		}
	};

	private int getPosition(int time) {
		int position = 0;
		for (int i = 0; i < timeList.size(); i++) {
			if (timeList.get(i) == time) {
				position = i;
				break;
			}
		}
		return position;
	}

	private void getData() {
		String url = UrlConfig.getUserOptions();
		HttpUtil.get(SetupActivity.this, url, new jsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				LogUtils.DebugLog("result json", response.toString());
				XbOption option = JSON.parseObject(response.toString(),
						XbOption.class);
				setup_monitor_time.setText(data.get(getPosition(option
						.getMonitorInterval())));
				setup_track_time.setText(data.get(getPosition(option
						.getTrackInterval())));
				mTime = option.getMonitorInterval();
				tTime = option.getTrackInterval();

				super.onSuccess(statusCode, headers, response);
			}
		});
	}

	private void setResetPw(String oldPw, final String newPw) {
		String url = UrlConfig.getUserResetPassword();
		JSONObject params = new JSONObject();
		StringEntity entity = null;
		try {
			params.put("oldPassword", CyptoUtils.MD5(oldPw));
			params.put("newPassword", CyptoUtils.MD5(newPw));
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
						if (Utils.requestOk(response)) {
							Utils.ToastMessage(SetupActivity.this,
									"密码修改成功,将为您重新登陆");
							PreferenceUtils.getInstance(SetupActivity.this)
									.setUserPW(newPw);
							Intent intent = new Intent(SetupActivity.this,
									LoginActivity.class);
							startActivity(intent);
							finish();
						} else {
							Utils.ToastMessage(SetupActivity.this,
									Utils.getKey(response, "errorMsg"));
						}
						super.onSuccess(statusCode, headers, response);
					}
				});

	}

	private void setIntervalTime() {
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
		LogUtils.DebugLog("post json", params.toString());
		HttpUtil.post(SetupActivity.this, url, entity, "application/json",
				new jsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						LogUtils.DebugLog("result json" + response);
						if (Utils.requestOk(response)) {
							Utils.ToastMessage(SetupActivity.this, "操作成功");
							PreferenceUtils.getInstance(SetupActivity.this)
									.setMonitorTime(mTime);
							PreferenceUtils.getInstance(SetupActivity.this)
									.setTrackTime(tTime);

						}
						super.onSuccess(statusCode, headers, response);
					}
				});

	}

}
