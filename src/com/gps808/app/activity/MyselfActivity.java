package com.gps808.app.activity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gps808.app.R;
import com.gps808.app.bean.BnUser;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.view.CircleImageView;
import com.gps808.app.view.PengButton;

public class MyselfActivity extends BaseActivity {
	private HeaderFragment headerFragment;
	private ImageView alter;
	private TextView mynickname;
	private CircleImageView my_headimage;
	private LinearLayout my_setup, my_about, my_help;
	private PengButton my_driver, my_police, my_routes, my_car;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("个人中心");
		// alter = (ImageView) findViewById(R.id.alter_person);
		// alter.setOnClickListener(click);
		//
		my_driver = (PengButton) findViewById(R.id.my_driver);
		my_driver.setOnClickListener(click);

		my_police = (PengButton) findViewById(R.id.my_police);
		my_police.setOnClickListener(click);

		my_routes = (PengButton) findViewById(R.id.my_routes);
		my_routes.setOnClickListener(click);

		my_car = (PengButton) findViewById(R.id.my_car);
		my_car.setOnClickListener(click);

		// linear

		my_setup = (LinearLayout) findViewById(R.id.my_setup);
		my_setup.setOnClickListener(click);
		my_about = (LinearLayout) findViewById(R.id.my_about);
		my_about.setOnClickListener(click);
		my_help = (LinearLayout) findViewById(R.id.my_help);
		my_help.setOnClickListener(click);
		// mynickname = (TextView) findViewById(R.id.my_nickname);
		//
		// my_headimage = (CircleImageView) findViewById(R.id.my_headimage);

		// alter.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent(getActivity(), SetupActivity.class);
		// startActivity(intent);
		// }
		// });
		// myself_call = (LinearLayout) findViewById(R.id.myself_call);
		// myself_call.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// TODO Auto-generated method stub
		// Utils.callPhone(getActivity(),
		// getResources().getString(R.string.server_call));
		// }
		// });
		// myself_edit = (LinearLayout) findViewById(R.id.myself_edit);
		// my_headimage.setOnClickListener(click);

	}

	private OnClickListener click = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub

			Intent intent = new Intent();
			Class cls = null;
			switch (arg0.getId()) {
			case R.id.my_setup:
				cls = SetupActivity.class;
				break;
			case R.id.my_about:
				cls = AboutActivity.class;
				break;
			case R.id.my_driver:
				cls = VoucherActivity.class;
				break;
			case R.id.my_police:
				cls = PolicesActivity.class;
				break;
			case R.id.my_help:
				cls = HelpActivity.class;
				break;
			case R.id.my_routes:
				cls = RoutesActivity.class;
				break;
			case R.id.my_car:
				cls = VehiclesActivity.class;
				break;

			}
			intent.setClass(MyselfActivity.this, cls);
			startActivity(intent);
		}
	};
}
