package com.gps808.app.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.gps808.app.R;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.Utils;
import com.gps808.app.view.FancyButton;

public class AboutActivity extends BaseActivity {
	private HeaderFragment headerFragment;
	private FancyButton about_share;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("关于我们");
		about_share = (FancyButton) findViewById(R.id.about_share);
		about_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Utils.Share(AboutActivity.this,
						getResources().getString(R.string.app_name),
						"易路通，您身边的行车助手！",
						"http://app.gps808.com/app_download.html", "");
			}
		});
	}
}
