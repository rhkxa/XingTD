package com.gps808.app.activity;

import android.os.Bundle;

import com.gps808.app.R;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;

public class MessageActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		HeaderFragment headerFragment = (HeaderFragment) this
				.getSupportFragmentManager().findFragmentById(R.id.title);
		headerFragment.setTitleText("消息");
	}
}
