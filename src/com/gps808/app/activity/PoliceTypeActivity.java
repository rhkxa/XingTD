package com.gps808.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.bean.XbAlarmOption;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.view.FancyButton;
import com.gps808.app.view.Switch.SwitchButton;

public class PoliceTypeActivity extends BaseActivity {

	private SwitchButton police_type1, police_type2, police_type3,
			police_type4;
	private XbAlarmOption alarmOption;

	private FancyButton
	 save_ok;

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
		police_type1.setChecked(alarmOption.isEmergency());
		police_type2.setChecked(alarmOption.isOverSpeed());
		police_type3.setChecked(alarmOption.isInArea());
		police_type4.setChecked(alarmOption.isOutArea());
		police_type1.setOnCheckedChangeListener(check);
		police_type2.setOnCheckedChangeListener(check);
		police_type3.setOnCheckedChangeListener(check);
		police_type4.setOnCheckedChangeListener(check);
		save_ok=(FancyButton) findViewById(R.id.save_ok);
		save_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 Intent intent = new Intent();
					intent.putExtra("type", JSON.toJSONString(alarmOption));
					setResult(RESULT_OK, intent);
					finish();
			}
		});

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
			}
		
		}

	};

}
