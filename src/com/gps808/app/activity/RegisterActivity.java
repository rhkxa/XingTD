package com.gps808.app.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import com.gps808.app.R;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.StringUtils;
import com.gps808.app.utils.Utils;
import com.gps808.app.view.TimeButton;

public class RegisterActivity extends BaseActivity {

	private EditText username, password, again_password, vercode, jq_code;
	private Button register;
	private TimeButton send_code;
	private Context context;
	private String code = null;
	private ImageButton backBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		context = RegisterActivity.this;
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		backBtn = (ImageButton) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		again_password = (EditText) findViewById(R.id.again_password);
		vercode = (EditText) findViewById(R.id.vercode);
		send_code = (TimeButton) findViewById(R.id.send_code);
		jq_code = (EditText) findViewById(R.id.jq_code);

		register = (Button) findViewById(R.id.register);
		send_code.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (StringUtils.isEmpty(username.getText().toString())) {
					Utils.ToastMessage(context, "请填写手机号码");
					return;
				}
				if (!StringUtils.isPhone(username.getText().toString())) {
					Utils.ToastMessage(context, "手机号码不正确请重新填写");
					return;
				}
				sendCode(username.getText().toString());
			}
		});
		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (StringUtils.isEmpty(username.getText().toString())) {
					Utils.ToastMessage(context, "请填写手机号码");
					return;
				}
				if (!StringUtils.isPhone(username.getText().toString())) {
					Utils.ToastMessage(context, "手机号码不正确请重新填写");
					return;
				}
				if (StringUtils.isEmpty(password.getText().toString())) {
					Utils.ToastMessage(context, "请填写密码");
					return;
				}
				if (StringUtils.isEmpty(again_password.getText().toString())) {
					Utils.ToastMessage(context, "请填写再次输入密码");
					return;
				}
				if (!password.getText().toString()
						.equals(again_password.getText().toString())) {
					Utils.ToastMessage(context, "两次密码输入一致请重新输入");
					return;
				}

				if (StringUtils.isEmpty(vercode.getText().toString())) {
					Utils.ToastMessage(context, "请填写验证号");
					return;
				}
				if (!(vercode.getText().toString()).equals(code)) {
					Utils.ToastMessage(context, "验证号不正确");
					return;
				}
				toRegister(username.getText().toString(), password.getText()
						.toString(), vercode.getText().toString(), jq_code
						.getText().toString());
			}
		});

	}

	private void sendCode(String mobi) {
		// HttpUtil.get(UrlConfig.getCaptcha(mobi), new
		// jsonHttpResponseHandler() {
		// @Override
		// public void onSuccess(JSONObject arg0) {
		// // TODO Auto-generated method stub
		// if (Utils.requestOk(arg0)) {
		// BnCaptcha captcha = JSON.parseObject(Utils.getResult(arg0),
		// BnCaptcha.class);
		// code = captcha.getCaptcha();
		// Utils.ToastMessage(context, "验证码发送成功");
		// send_code.setTextAfter("秒后重新获取").setTextBefore("点击获取验证码")
		// .setBackgroundResource(R.color.grayslate);
		// send_code.start();
		// } else {
		// if (Utils.getKey(arg0, "code").equals("200104")) {
		// Utils.ToastMessage(context, Utils.getKey(arg0, "msg"));
		// }
		// }
		// super.onSuccess(arg0);
		// }
		// });

	}

	private void toRegister(String name, String word, String code, String promo) {
		// HttpUtil.get(UrlConfig.getRegister(name, word, code, promo),
		// new jsonHttpResponseHandler() {
		// @Override
		// public void onSuccess(JSONObject arg0) {
		// // TODO Auto-generated method stub
		// if (Utils.requestOk(arg0)) {
		// Utils.ToastMessage(context, "注册成功");
		// finish();
		// }
		// super.onSuccess(arg0);
		// }
		// });
	}
}
