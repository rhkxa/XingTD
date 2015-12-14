package com.gps808.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.bean.BnUser;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.Utils;
import com.gps808.app.view.CircleImageView;
import com.gps808.app.view.Switch.SwitchButton;
import com.nostra13.universalimageloader.core.ImageLoader;


public class SetupActivity extends BaseActivity {

	private HeaderFragment headerFragment;
	private RelativeLayout setup_user, setup_push;
	private LinearLayout setup_score, setup_agrement, setup_introduce,
			setup_update;
	private CircleImageView personal_headimage;
	private SwitchButton push_switch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub

		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("设置");
		setup_user = (RelativeLayout) findViewById(R.id.setup_user);
		setup_push = (RelativeLayout) findViewById(R.id.setup_push);
		setup_score = (LinearLayout) findViewById(R.id.setup_score);
		setup_agrement = (LinearLayout) findViewById(R.id.setup_agrement);
		setup_introduce = (LinearLayout) findViewById(R.id.setup_introduce);
		setup_update = (LinearLayout) findViewById(R.id.setup_update);
		personal_headimage = (CircleImageView) findViewById(R.id.personal_headimage);
		push_switch = (SwitchButton) findViewById(R.id.push_switch);

		setup_agrement.setOnClickListener(click);
		setup_score.setOnClickListener(click);
		setup_introduce.setOnClickListener(click);
		setup_user.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
					Intent intent = new Intent(SetupActivity.this,
							UserManagerActivity.class);
					startActivity(intent);
				
			}
		});
	}

	private OnClickListener click = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Class cls = null;
			switch (arg0.getId()) {
			case R.id.setup_score:
				cls = ScoreActivity.class;
				break;
			case R.id.setup_agrement:
				cls = AgreementActivity.class;
				break;
			case R.id.setup_introduce:
				cls = AboutActivity.class;
				break;
			case R.id.setup_update:
				break;

			}
			Intent intent = new Intent(SetupActivity.this, cls);
			startActivity(intent);
		}
	};
}
