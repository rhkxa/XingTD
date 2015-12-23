package com.gps808.app.activity;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;

import com.gps808.app.bean.XbAlarmOption;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.PreferenceUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;
import com.gps808.app.view.CircleImageView;
import com.gps808.app.view.Switch.SwitchButton;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PoliceSetupActivity extends BaseActivity {

	private HeaderFragment headerFragment;
	private LinearLayout setup_message;
	private SwitchButton push_switch, shock_switch, voice_switch;
	private PreferenceUtils mPreferenceUtils;
	private XbAlarmOption alarmOption;

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
				startActivity(intent);

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
						alarmOption = JSON.parseObject(response.toString(),
								XbAlarmOption.class);
						setValue();
						super.onSuccess(statusCode, headers, response);
					}
				});
	}

	protected void setValue() {
		// TODO Auto-generated method stub
		push_switch.setChecked(alarmOption.isAcceptAlarm());
		shock_switch.setChecked(alarmOption.isVibration());
		voice_switch.setChecked(alarmOption.isSound());
	}
}
