package com.gps808.app.activity;

import org.apache.http.Header;
import org.json.JSONObject;

import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.bean.XbCar;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.view.CircleImageView;
import com.gps808.app.view.PengButton;

/**
 * 车辆的详情
 * 
 * @author JIA
 * 
 */
public class CarDetailsActivity extends BaseActivity {
	private HeaderFragment headerFragment;
	private CircleImageView car_detail_image;
	private TextView car_detail_name, car_detail_num, car_detail_position,
			car_phone_text, car_number_text, car_type_text, car_start_text,
			car_end_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_details);
		init();
		getData();
	}

	private void init() {
		// TODO Auto-generated method stub
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("车辆详情");
		car_detail_name = (TextView) findViewById(R.id.car_detail_name);
		car_detail_num = (TextView) findViewById(R.id.car_detail_num);
		car_detail_position = (TextView) findViewById(R.id.car_detail_position);
		car_phone_text = (TextView) findViewById(R.id.car_phone_text);
		car_number_text = (TextView) findViewById(R.id.car_number_text);
		car_type_text = (TextView) findViewById(R.id.car_type_text);
		car_start_text = (TextView) findViewById(R.id.car_start_text);
		car_end_text = (TextView) findViewById(R.id.car_end_text);

	}

	private void setValue(XbCar car) {
		car_detail_name.setText("设备名称：" + car.getTmnlName());
		car_detail_num.setText("车牌号：：" + car.getPlateNo());
		car_detail_position.setText(car.getAddr());
		car_phone_text.setText(car.getSimNo());
		car_number_text.setText(car.getTmnlNo());
		car_type_text.setText(car.getTmnlType());
		car_start_text.setText(car.getStart());
		car_end_text.setText(car.getEnd());
	}

	private void getData() {
		showProgressDialog(CarDetailsActivity.this, "正在加载，请稍等");
		String url = UrlConfig.getVehicleVehInfo(getIntent().getStringExtra(
				"vid"));
		HttpUtil.get(url, new jsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				XbCar car = JSON.parseObject(response.toString(), XbCar.class);
				LogUtils.DebugLog("result json" + response.toString());
				setValue(car);
				super.onSuccess(statusCode, headers, response);
			}
		});
	}
}
