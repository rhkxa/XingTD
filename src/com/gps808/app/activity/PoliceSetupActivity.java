package com.gps808.app.activity;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import com.alibaba.fastjson.JSON;
import com.gps808.app.R;

import com.gps808.app.bean.XbAlarmOption;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.PreferenceUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;
import com.gps808.app.view.FancyButton;
import com.gps808.app.view.Switch.SwitchButton;

public class PoliceSetupActivity extends BaseActivity {

	private HeaderFragment headerFragment;
	private LinearLayout setup_message;
	private SwitchButton push_switch, shock_switch, voice_switch;
	private PreferenceUtils mPreferenceUtils;
	private XbAlarmOption alarmOption;
	private FancyButton save_ok;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_police_setup);
		mPreferenceUtils = PreferenceUtils
				.getInstance(PoliceSetupActivity.this);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub

		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("设置");
		push_switch = (SwitchButton) findViewById(R.id.push_switch);
		voice_switch = (SwitchButton) findViewById(R.id.voice_switch);
		shock_switch = (SwitchButton) findViewById(R.id.shock_switch);
		push_switch.setChecked(mPreferenceUtils.getPush());
		voice_switch.setChecked(mPreferenceUtils.getVoice());
		shock_switch.setChecked(mPreferenceUtils.getShock());
		push_switch.setOnCheckedChangeListener(check);
		shock_switch.setOnCheckedChangeListener(check);
		voice_switch.setOnCheckedChangeListener(check);

		setup_message = (LinearLayout) findViewById(R.id.setup_message);

		setup_message.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PoliceSetupActivity.this,
						PoliceTypeActivity.class);
				intent.putExtra("options", JSON.toJSONString(alarmOption));
				startActivityForResult(intent, RESULT_OK);
			}
		});
		save_ok = (FancyButton) findViewById(R.id.save_ok);
		save_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setData();
			}
		});
		getData();
	}

	private OnCheckedChangeListener check = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			case R.id.push_switch:
				mPreferenceUtils.setPush(arg1);
				alarmOption.setAcceptAlarm(arg1);
				break;
			case R.id.shock_switch:
				mPreferenceUtils.setShock(arg1);
				alarmOption.setVibration(arg1);
				break;
			case R.id.voice_switch:
				mPreferenceUtils.setVoice(arg1);
				alarmOption.setSound(arg1);
				break;

			}

		}
	};

	private void getData() {
		showProgressDialog(PoliceSetupActivity.this, "正在加载您的个人设置");
		String url = UrlConfig.getVehicleAlarmsOtions();
		HttpUtil.get(PoliceSetupActivity.this, url,
				new jsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						LogUtils.DebugLog("result json", response.toString());
						alarmOption = JSON.parseObject(response.toString(),
								XbAlarmOption.class);
						setValue();
						super.onSuccess(statusCode, headers, response);
					}
				});
	}

	private void setValue() {
		// TODO Auto-generated method stub
		push_switch.setChecked(alarmOption.isAcceptAlarm());
		shock_switch.setChecked(alarmOption.isVibration());
		voice_switch.setChecked(alarmOption.isSound());
	}

	private void setData() {
		showProgressDialog(PoliceSetupActivity.this, "正在配置您的个人设置");
		String url = UrlConfig.getVehicleSetAlarms();
		StringEntity entity = null;
		
		try {
			
			entity = new StringEntity(JSON.toJSONString(alarmOption));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LogUtils.DebugLog("post json", JSON.toJSONString(alarmOption));
		HttpUtil.post(PoliceSetupActivity.this, url, entity,
				"application/json", new jsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						if (Utils.requestOk(response)) {
							Utils.ToastMessage(PoliceSetupActivity.this,
									"您的配置已经成功");
							mPreferenceUtils.setPush(alarmOption
									.isAcceptAlarm());
							mPreferenceUtils.setVoice(alarmOption.isSound());
							mPreferenceUtils.setShock(alarmOption.isVibration());
						}
						super.onSuccess(statusCode, headers, response);
					}
				});
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		if (arg1 == RESULT_OK) {
			XbAlarmOption type = JSON.parseObject(arg2.getStringExtra("type"),
					XbAlarmOption.class);
			alarmOption.setEmergency(type.isEmergency());
			alarmOption.setInArea(type.isInArea());
			alarmOption.setOutArea(type.isOutArea());
			alarmOption.setOverSpeed(type.isOverSpeed());
		}
		super.onActivityResult(arg0, arg1, arg2);
	}
}
