package com.gps808.app.activity;

import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;

import android.widget.CheckBox;

import android.widget.ImageView;
import android.widget.ListView;

import android.widget.TextView;


import com.gps808.app.R;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.Utils;


public class ActiveDetailActivity extends BaseActivity {
	private HeaderFragment headerFragment;
	private ImageView active_details_image;
	private ListView active_detail_good_list;
	private TextView active_details_content;
	private TextView active_details_name;
	private CheckBox active_details_laud;
	private String aid = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_active_details);
		init();

	}

	private void init() {
		// TODO Auto-generated method stub
		aid = getIntent().getStringExtra("aid");
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("活动详情");
		headerFragment.setImageButtonResource(R.drawable.smyk_share);
		headerFragment.setCommentBtnListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Utils.Share(ActiveDetailActivity.this, "数码衣库", "名字",
						"点击后跳 转的地址 ", "分享的图片地址");

			}
		});
	}
}
