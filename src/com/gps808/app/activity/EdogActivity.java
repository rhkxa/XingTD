package com.gps808.app.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.gps808.app.R;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;

public class EdogActivity extends BaseActivity {

	private TextView navi_voice_show;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_edog);

		init();
	}

	/**
	 * 初始化各种对象
	 */
	private void init() {
		HeaderFragment headerFragment = (HeaderFragment) this
				.getSupportFragmentManager().findFragmentById(R.id.title);
		headerFragment.setTitleText("电子狗巡航");
		navi_voice_show = (TextView) findViewById(R.id.navi_voice_show);

	}

}