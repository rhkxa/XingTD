package com.gps808.app.activity;

import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.bean.XbAlarmOption;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.view.Switch.SwitchButton;

public class PoliceTypeActivity extends BaseActivity {

	private SwitchButton police_type1,police_type2,police_type3,police_type4;
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
		alarmOption=JSON.parseObject(getIntent().getStringExtra("options"),XbAlarmOption.class);
		police_type1=(SwitchButton) findViewById(R.id.police_type1);
		police_type2=(SwitchButton) findViewById(R.id.police_type2);
		police_type3=(SwitchButton) findViewById(R.id.police_type3);
		police_type4=(SwitchButton) findViewById(R.id.police_type4);
		police_type1.setChecked(alarmOption.isEmergency());
		police_type2.setChecked(alarmOption.isOverSpeed());
		police_type3.setChecked(alarmOption.isInArea());
		police_type4.setChecked(alarmOption.isOutArea());
	}
	
}
