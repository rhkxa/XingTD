package com.gps808.app.activity;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.models.XbAlarmOption;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;
import com.gps808.app.view.Switch.SwitchButton;

public class PoliceTypeActivity extends BaseActivity {

	private SwitchButton police_type1, police_type2, police_type3,
			police_type4, police_type5, police_type6, police_type7,
			police_type8, police_type9, police_type10, police_type11,
			police_type12;
	private XbAlarmOption alarmOption;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_police_type);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		HeaderFragment headerFragment = (HeaderFragment) this
				.getSupportFragmentManager().findFragmentById(R.id.title);
		headerFragment.setTitleText("报警消息类型");
		alarmOption = JSON.parseObject(getIntent().getStringExtra("options"),
				XbAlarmOption.class);
		police_type1 = (SwitchButton) findViewById(R.id.police_type1);
		police_type2 = (SwitchButton) findViewById(R.id.police_type2);
		police_type3 = (SwitchButton) findViewById(R.id.police_type3);
		police_type4 = (SwitchButton) findViewById(R.id.police_type4);
		police_type5 = (SwitchButton) findViewById(R.id.police_type5);
		police_type6 = (SwitchButton) findViewById(R.id.police_type6);
		police_type7 = (SwitchButton) findViewById(R.id.police_type7);
		police_type8 = (SwitchButton) findViewById(R.id.police_type8);
		police_type9 = (SwitchButton) findViewById(R.id.police_type9);
		police_type10 = (SwitchButton) findViewById(R.id.police_type10);
		police_type11 = (SwitchButton) findViewById(R.id.police_type11);
		police_type12 = (SwitchButton) findViewById(R.id.police_type12);
		if (alarmOption != null) {
			police_type1.setChecked(alarmOption.isEmergency());
			police_type2.setChecked(alarmOption.isOverSpeed());
			police_type3.setChecked(alarmOption.isInArea());
			police_type4.setChecked(alarmOption.isOutArea());
			police_type5.setChecked(alarmOption.isGnssModeFault());
			police_type6.setChecked(alarmOption.isGnssDisconnect());
			police_type7.setChecked(alarmOption.isGnssShortCircuit());
			police_type8.setChecked(alarmOption.isTmnlPowerDown());
			police_type9.setChecked(alarmOption.isVssFault());
			police_type10.setChecked(alarmOption.isCrashAlarm());
			police_type11.setChecked(alarmOption.isRolloverAlarm());
			police_type12.setChecked(alarmOption.isDangerAlarm());

		}
		police_type1.setOnCheckedChangeListener(check);
		police_type2.setOnCheckedChangeListener(check);
		police_type3.setOnCheckedChangeListener(check);
		police_type4.setOnCheckedChangeListener(check);
		police_type5.setOnCheckedChangeListener(check);
		police_type6.setOnCheckedChangeListener(check);
		police_type7.setOnCheckedChangeListener(check);
		police_type8.setOnCheckedChangeListener(check);
		police_type9.setOnCheckedChangeListener(check);
		police_type10.setOnCheckedChangeListener(check);
		police_type11.setOnCheckedChangeListener(check);
		police_type12.setOnCheckedChangeListener(check);

	}

	private OnCheckedChangeListener check = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			case R.id.police_type1:
				alarmOption.setEmergency(arg1);
				break;
			case R.id.police_type2:
				alarmOption.setOverSpeed(arg1);
				break;
			case R.id.police_type3:
				alarmOption.setInArea(arg1);
				break;
			case R.id.police_type4:
				alarmOption.setOutArea(arg1);
				break;
			case R.id.police_type5:
				alarmOption.setGnssModeFault(arg1);
				break;
			case R.id.police_type6:
				alarmOption.setGnssDisconnect(arg1);
				break;
			case R.id.police_type7:
				alarmOption.setGnssShortCircuit(arg1);
				break;
			case R.id.police_type8:
				alarmOption.setTmnlPowerDown(arg1);
				break;
			case R.id.police_type9:
				alarmOption.setVssFault(arg1);
				break;
			case R.id.police_type10:
				alarmOption.setCrashAlarm(arg1);
				break;
			case R.id.police_type11:
				alarmOption.setRolloverAlarm(arg1);
				break;
			case R.id.police_type12:
				alarmOption.setDangerAlarm(arg1);
				break;

			}
			setData();
		}

	};

	private void setData() {
		String url = UrlConfig.getVehicleSetAlarms();
		StringEntity entity = null;

		try {

			entity = new StringEntity(JSON.toJSONString(alarmOption));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LogUtils.DebugLog("post json", JSON.toJSONString(alarmOption));
		HttpUtil.post(PoliceTypeActivity.this, url, entity, "application/json",
				new jsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						if (Utils.requestOk(response)) {
							Utils.ToastMessage(PoliceTypeActivity.this,
									"您的配置已经成功");

						}
						super.onSuccess(statusCode, headers, response);
					}
				});
	}

}
