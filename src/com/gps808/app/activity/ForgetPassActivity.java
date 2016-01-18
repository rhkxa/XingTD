package com.gps808.app.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.gps808.app.R;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;

public class ForgetPassActivity extends BaseActivity {

	private EditText vercode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_pass);
		init();
		sendCode();
	}

	private void init() {
		// TODO Auto-generated method stub
		HeaderFragment headerFragment = (HeaderFragment) this
				.getSupportFragmentManager().findFragmentById(R.id.title);
		headerFragment.setTitleText("忘记密码");
		vercode = (EditText) findViewById(R.id.vercode);
		vercode.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				// SMSSDK.submitVerificationCode("86", "18519501970",
				// arg0.toString());
			}
		});
	}

	private void sendCode() {
		// SMSSDK.initSDK(this, "edc4244c360d",
		// "3497a68039718733ff3cf1cba8f264af");
		// EventHandler eh = new EventHandler() {
		//
		// @Override
		// public void afterEvent(int event, int result, Object data) {
		//
		// if (result == SMSSDK.RESULT_COMPLETE) {
		// // 回调完成
		// if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
		// // 提交验证码成功
		// LogUtils.DebugLog("验证码输入正确");
		// } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
		// // 获取验证码成功
		// } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
		// // 返回支持发送验证码的国家列表
		// }
		// } else {
		// ((Throwable) data).printStackTrace();
		// }
		// }
		// };
		// SMSSDK.getVerificationCode("86", "18519501970");

	}
}
