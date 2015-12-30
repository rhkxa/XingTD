package com.gps808.app.activity;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.bean.XbWeather;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.PreferenceUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.BaseFragment.jsonHttpResponseHandler;
import com.gps808.app.view.CircleImageView;
import com.gps808.app.view.FancyButton;
import com.gps808.app.view.FancyButton;

public class MyselfActivity extends BaseActivity {
	private HeaderFragment headerFragment;
	private ImageView alter;
	private TextView mynickname;
	private CircleImageView my_headimage;
	private LinearLayout my_setup, my_about, my_help;
	private FancyButton my_driver, my_police, my_routes, my_car;
	private TextView my_weather;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself);
		init();
		getData();
	}

	private void init() {
		// TODO Auto-generated method stub
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("个人中心");
		headerFragment.setImageButtonResource(R.drawable.xtd_action_talk);
		headerFragment.setCommentBtnListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MyselfActivity.this,
						MessageActivity.class);
				startActivity(intent);
			}
		});
		// alter = (ImageView) findViewById(R.id.alter_person);
		// alter.setOnClickListener(click);
		//
		my_driver = (FancyButton) findViewById(R.id.my_driver);
		my_driver.setOnClickListener(click);

		my_police = (FancyButton) findViewById(R.id.my_police);
		my_police.setOnClickListener(click);

		my_routes = (FancyButton) findViewById(R.id.my_routes);
		my_routes.setOnClickListener(click);

		my_car = (FancyButton) findViewById(R.id.my_car);
		my_car.setOnClickListener(click);

		// linear

		my_setup = (LinearLayout) findViewById(R.id.my_setup);
		my_setup.setOnClickListener(click);
		my_about = (LinearLayout) findViewById(R.id.my_about);
		my_about.setOnClickListener(click);
		my_help = (LinearLayout) findViewById(R.id.my_help);
		my_help.setOnClickListener(click);
		mynickname = (TextView) findViewById(R.id.my_nickname);
		mynickname.setText(PreferenceUtils.getInstance(MyselfActivity.this)
				.getUserName());
		my_weather=(TextView) findViewById(R.id.my_weather);
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
				cls = DriverActivity.class;
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
	private void getData() {
		String url = UrlConfig.getWeather(0, 0);
		HttpUtil.get(MyselfActivity.this, url, new jsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub

				LogUtils.DebugLog("result json", response.toString());
				XbWeather xbWeather = JSON.parseObject(response.toString(),
						XbWeather.class);
				my_weather.setText(xbWeather.getNow().getDesc());
				super.onSuccess(statusCode, headers, response);
			}
		});
	}
}
