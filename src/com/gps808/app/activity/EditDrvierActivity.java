package com.gps808.app.activity;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.adapter.XbDriver;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.CyptoUtils;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.PreferenceUtils;
import com.gps808.app.utils.StringUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;
import com.gps808.app.view.FancyButton;
import com.gps808.app.view.Switch.SwitchButton;

public class EditDrvierActivity extends BaseActivity {

	private EditText driver_phone, driver_name, driver_user, driver_pass,
			driver_pass_again;
	private SwitchButton driver_state;
	private FancyButton save_ok;
	private XbDriver xbDriver = new XbDriver();
	private int driverId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_driver);
		init();

	}

	private void init() {
		// TODO Auto-generated method stub
		HeaderFragment headerFragment = (HeaderFragment) this
				.getSupportFragmentManager().findFragmentById(R.id.title);

		driverId = getIntent().getIntExtra("driverId", 0);
		driver_phone = (EditText) findViewById(R.id.driver_phone);
		driver_name = (EditText) findViewById(R.id.driver_name);
		driver_user = (EditText) findViewById(R.id.driver_user);
		driver_pass = (EditText) findViewById(R.id.driver_pass);
		driver_pass_again = (EditText) findViewById(R.id.driver_pass_again);
		driver_state = (SwitchButton) findViewById(R.id.driver_state);
		save_ok = (FancyButton) findViewById(R.id.save_ok);
		driver_pass.setKeyListener(inputType);
		driver_user.setKeyListener(inputType);
		driver_pass_again.setKeyListener(inputType);
		save_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (StringUtils.isEmpty(driver_phone.getText().toString())) {
					Utils.ToastMessage(EditDrvierActivity.this, "手机号不能为空");
					return;
				}
				if (StringUtils.isEmpty(driver_name.getText().toString())) {
					Utils.ToastMessage(EditDrvierActivity.this, "名称不能为空");
					return;
				}
				if (StringUtils.isEmpty(driver_user.getText().toString())) {
					Utils.ToastMessage(EditDrvierActivity.this, "登录名称不能为空");
					return;
				}
				if (driverId == 0) {
					if (StringUtils.isEmpty(driver_pass.getText().toString())) {
						Utils.ToastMessage(EditDrvierActivity.this, "密码不能为空");
						return;
					}
					if (StringUtils.isEmpty(driver_pass_again.getText()
							.toString())) {
						Utils.ToastMessage(EditDrvierActivity.this,
								"再次输入密码不能为空");
						return;
					}
					if (driver_pass.length() < 6) {
						Utils.ToastMessage(EditDrvierActivity.this, "密码长度最少6位");
						return;
					}
					if (!driver_pass.getText().toString()
							.equals(driver_pass_again.getText().toString())) {
						Utils.ToastMessage(EditDrvierActivity.this,
								"确认密码与密码不一致,请重新输入");
						return;
					}
				}
				if (!StringUtils.isPhone(driver_phone.getText().toString())) {
					Utils.ToastMessage(EditDrvierActivity.this, "手机号码不正确,请重新输入");
					return;
				}
				xbDriver.setLoginName(driver_user.getText().toString());
				xbDriver.setDriverId(driverId);
				xbDriver.setDriverName(driver_name.getText().toString());
				xbDriver.setPassword(CyptoUtils.MD5(driver_pass.getText()
						.toString()));
				xbDriver.setPhone(driver_phone.getText().toString());
				if (driver_state.isChecked()) {
					xbDriver.setStatus(1);
				} else {
					xbDriver.setStatus(0);
				}
				setDriver(xbDriver);
			}
		});
		if (driverId > 0) {
			getDriver();
			xbDriver.setPassword(PreferenceUtils.getInstance(
					EditDrvierActivity.this).getUserPW());
			headerFragment.setTitleText("编辑司机");
			driver_phone.setText(xbDriver.getPhone());
			driver_name.setText(xbDriver.getDriverName());
			driver_user.setText(xbDriver.getLoginName());
			driver_pass.setText(xbDriver.getPassword());
			driver_pass_again.setText(xbDriver.getPassword());
			if (xbDriver.getStatus() == 1) {
				driver_state.setChecked(true);
			} else {
				driver_state.setChecked(false);
			}
			driver_user.setEnabled(false);
		} else {
			headerFragment.setTitleText("添加司机");
		}
	}

	private void getDriver() {

		String url = UrlConfig.getDriverInfo(driverId);
		HttpUtil.get(EditDrvierActivity.this, url,
				new jsonHttpResponseHandler() {
					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						showProgressDialog(EditDrvierActivity.this, "正在加载司机信息");
						super.onStart();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						LogUtils.DebugLog("result json", response.toString());
						xbDriver = JSON.parseObject(response.toString(),
								XbDriver.class);
						setValue();
						super.onSuccess(statusCode, headers, response);
					}
				});
	}

	protected void setValue() {
		// TODO Auto-generated method stub
		driver_phone.setText(xbDriver.getPhone());
		driver_name.setText(xbDriver.getDriverName());
		driver_user.setText(xbDriver.getLoginName());
		driver_pass.setText(xbDriver.getPassword());
		driver_pass_again.setText(xbDriver.getPassword());
		if (xbDriver.getStatus() == 1) {
			driver_state.setChecked(true);
		} else {
			driver_state.setChecked(false);
		}

	}

	private DigitsKeyListener inputType = new DigitsKeyListener() {
		@Override
		public int getInputType() {
			return InputType.TYPE_TEXT_VARIATION_PASSWORD;
		}

		@Override
		protected char[] getAcceptedChars() {
			char[] data = getResources().getString(
					R.string.login_only_can_input).toCharArray();
			return data;
		}

	};

	private void setDriver(XbDriver xbDriver) {
		String url = UrlConfig.getAddDriver();
		StringEntity entity = null;
		try {
			LogUtils.DebugLog("post json", JSON.toJSONString(xbDriver));
			entity = new StringEntity(JSON.toJSONString(xbDriver), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpUtil.post(EditDrvierActivity.this, url, entity, "application/json",
				new jsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						if (Utils.requestOk(response)) {
							Utils.ToastMessage(EditDrvierActivity.this, "操作成功");
							Intent intent = new Intent();
							setResult(RESULT_OK, intent);
							finish();
						} else {
							Utils.ToastMessage(EditDrvierActivity.this, Utils
									.getKey(response, "errorMsg" + "请重新填写"));
						}
						super.onSuccess(statusCode, headers, response);
					}
				});

	}
}
