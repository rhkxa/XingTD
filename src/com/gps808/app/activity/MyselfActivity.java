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
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.gps808.app.R;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.FileUtils;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.PreferenceUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;
import com.gps808.app.view.CircleImageView;
import com.gps808.app.view.FancyButton;

public class MyselfActivity extends BaseActivity {
	private HeaderFragment headerFragment;
	private ImageView alter;
	private TextView mynickname;
	private CircleImageView my_headimage;
	private LinearLayout my_setup, my_about, my_help;
	private FancyButton my_driver, my_car, my_map,
			my_police;
	private TextView my_weather;
	// 定位
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();

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
		my_driver = (FancyButton) findViewById(R.id.my_driver);
		my_driver.setOnClickListener(click);
	
	
		my_car = (FancyButton) findViewById(R.id.my_car);
		my_car.setOnClickListener(click);

		// linear

		my_setup = (LinearLayout) findViewById(R.id.my_setup);
		my_setup.setOnClickListener(click);
		my_about = (LinearLayout) findViewById(R.id.my_about);
		my_about.setOnClickListener(click);
		my_help = (LinearLayout) findViewById(R.id.my_help);
		my_help.setOnClickListener(click);
		my_map = (FancyButton) findViewById(R.id.my_map);
		my_map.setOnClickListener(click);
		my_police = (FancyButton) findViewById(R.id.my_police);
		my_police.setOnClickListener(click);
		mynickname = (TextView) findViewById(R.id.my_nickname);
		mynickname.setText(PreferenceUtils.getInstance(MyselfActivity.this)
				.getUserNick());
		my_weather = (TextView) findViewById(R.id.my_weather);
		if (PreferenceUtils.getInstance(MyselfActivity.this).getUserState() > 3) {
			my_driver.setBackgroundColor(getResources().getColor(R.color.gray));
			my_driver.setEnabled(false);
		}
		if (System.currentTimeMillis()
				- PreferenceUtils.getInstance(MyselfActivity.this).getValue(
						"NowWeather") > 60 * 60 * 1000) {
			mLocClient = new LocationClient(MyselfActivity.this);
			mLocClient.registerLocationListener(myListener);
			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(false);// 打开gps
			option.setCoorType("bd09ll"); // 设置坐标类型
			mLocClient.setLocOption(option);
			mLocClient.start();
		} else {
			my_weather.setText(FileUtils.read(MyselfActivity.this,
					"FileNowWeather"));
		}
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
			
			case R.id.my_help:
				cls = HelpActivity.class;
				break;
			case R.id.my_car:
				cls = VehiclesActivity.class;
				break;
			case R.id.my_map:
				cls = OfflineMapActivity.class;
				break;
			case R.id.my_police:
				cls = PolicesActivity.class;
				break;
			}
			intent.setClass(MyselfActivity.this, cls);
			startActivityForResult(intent, 1);
		}
	};

	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg1 == RESULT_OK) {
			Intent intent = new Intent();
			intent.putExtra("vid", arg2.getStringExtra("vid"));
			setResult(RESULT_OK, intent);
			finish();
		}

	};

	private void getData(double lng, double lat) {
		String url = UrlConfig.getNowWeather(lng, lat);
		HttpUtil.get(MyselfActivity.this, url, new jsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				LogUtils.DebugLog("result json", response.toString());
				FileUtils.write(MyselfActivity.this, "FileNowWeather",
						Utils.getKey(response, "desc"));
				PreferenceUtils.getInstance(MyselfActivity.this).setValue(
						"NowWeather", System.currentTimeMillis());
				my_weather.setText(Utils.getKey(response, "desc"));
				if (mLocClient != null) {
					// 退出时销毁定位
					mLocClient.stop();
				}
				super.onSuccess(statusCode, headers, response);
			}
		});
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			getData(location.getLongitude(), location.getLatitude());
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

}
