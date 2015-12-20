package com.gps808.app.activity;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.gps808.app.R;
import com.gps808.app.bean.PUser;
import com.gps808.app.bean.XbUser;
import com.gps808.app.dialog.CustomOkDialog;
import com.gps808.app.push.PushUtils;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.CyptoUtils;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.PreferenceUtils;
import com.gps808.app.utils.StringUtils;
import com.gps808.app.utils.UpdateManager;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;
import com.gps808.app.utils.XtdApplication;
import com.gps808.app.view.FancyButton;

/**
 * 登录界面
 * 
 * @author JIA
 * 
 */
public class LoginActivity extends BaseActivity {
	private long mExitTime = 0;
	private FancyButton login;
	private TextView phonenumber;
	private EditText userName;
	private EditText passWord;
	private CheckBox autoLoginBox;
	private CheckBox savePwdBox;
	private RelativeLayout call;
	// /private TextView login_serve;
	private TextView login_to_register;
	private TextView login_forget_pass;

	private PreferenceUtils mPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// UpdateManager.getUpdateManager().checkAppUpdate(LoginActivity.this,
		// false);
//		initWithApiKey();
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		mPreferences = PreferenceUtils.getInstance(LoginActivity.this);
		userName = (EditText) findViewById(R.id.username);
		passWord = (EditText) findViewById(R.id.password);
		autoLoginBox = (CheckBox) findViewById(R.id.autoLoginBox);
		savePwdBox = (CheckBox) findViewById(R.id.savePwdBox);
		login = (FancyButton) findViewById(R.id.login);
		phonenumber = (TextView) findViewById(R.id.phonenumber);
		// call = (RelativeLayout) findViewById(R.id.call_phone);
		// login_serve = (TextView) findViewById(R.id.login_serve);
		login_to_register = (TextView) findViewById(R.id.login_to_register);
		login_forget_pass = (TextView) findViewById(R.id.login_forget_pass);
		autoLoginBox.setChecked(mPreferences.getAutoLogin());
		savePwdBox.setChecked(mPreferences.getSavePassWord());

		if (mPreferences.getSavePassWord()) {
			if (!StringUtils.isEmpty(mPreferences.getUserName())) {
				userName.setText(mPreferences.getUserName());
			}
			if (!StringUtils.isEmpty(mPreferences.getUserPW())) {
				passWord.setText(mPreferences.getUserPW());
			}
		}
		autoLoginBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				mPreferences.setAutoLogin(arg1);
			}
		});

		savePwdBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				mPreferences.setSavePassWord(arg1);
			}
		});

		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (StringUtils.isEmpty(userName.getText().toString())) {
					Utils.ToastMessage(LoginActivity.this, getResources()
							.getString(R.string.msg_login_name_null));
					return;
				}
				if (StringUtils.isEmpty(passWord.getText().toString())) {
					Utils.ToastMessage(LoginActivity.this, getResources()
							.getString(R.string.msg_login_pwd_null));
					return;
				}
				getLogin();
			}
		});
		if (mPreferences.getAutoLogin()) {
			if (!StringUtils.isEmpty(mPreferences.getUserState())) {
				login.performClick();
			}
		}

		// call.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// // 打电话
		// Utils.callPhone(LoginActivity.this, phonenumber.getText()
		// .toString());
		// }
		// });
		// login_serve.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent(LoginActiivty.this,
		// AgreementActivity.class);
		// startActivity(intent);
		//
		// }
		// });
		login_to_register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// Intent intent = new Intent(LoginActivity.this,
				// RegisterActivity.class);
				// startActivity(intent);
				CustomOkDialog register = new CustomOkDialog(
						LoginActivity.this, "新用户", "请电话联系", null);
				register.show();
			}
		});

	}

	// 登录
	private void getLogin() {
		if (Utils.isNetWorkConnected(LoginActivity.this)) {
			showProgressDialog(LoginActivity.this, "登录中，请稍等");
			String url = UrlConfig.getLogin();
			JSONObject params = new JSONObject();
			PUser pUser = new PUser();
			pUser.setUsername(userName.getText().toString());
			pUser.setPassword(CyptoUtils.MD5(passWord.getText().toString()));
			StringEntity entity = null;
			try {
				params.put("username", userName.getText().toString());
				params.put("password",
						CyptoUtils.MD5(passWord.getText().toString()));
				entity = new StringEntity(JSON.toJSONString(pUser));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			LogUtils.DebugLog("post json", params.toString());
			HttpUtil.post(LoginActivity.this, url, entity, "application/json",
					new jsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode,
								org.apache.http.Header[] headers,
								JSONObject response) {
							if (Utils.requestOk(response)) {

								XbUser xbUser = JSON.parseObject(
										response.toString(), XbUser.class);
								Utils.ToastMessage(LoginActivity.this, "登录成功");
								mPreferences.setUserNick(xbUser.getUserName());
								mPreferences.setUserId(xbUser.getUserId());
								mPreferences.setUserState(xbUser.getUserType());
								mPreferences.setUserPW(passWord.getText()
										.toString());
								mPreferences.setUserName(userName.getText()
										.toString());
								Intent intent = new Intent(LoginActivity.this,
										MainActivity.class);
								startActivity(intent);
								finish();

							} else {
								Utils.ToastMessage(LoginActivity.this,
										Utils.getKey(response, "errorMsg"));
							}

							LogUtils.DebugLog(response.toString());
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							// TODO Auto-generated method stub
							Utils.ToastMessage(LoginActivity.this, "登陆失败，请重试");
							super.onFailure(statusCode, headers, throwable,
									errorResponse);
						}
					});

		} else {
			Utils.showSuperCardToast(LoginActivity.this, getResources()
					.getString(R.string.network_not_connected));
		}
	}

	// 以apikey的方式绑定
	private void initWithApiKey() {
		// Push: 无账号初始化，用api key绑定
		PushManager.startWork(getApplicationContext(),
				PushConstants.LOGIN_TYPE_API_KEY,
				PushUtils.getMetaValue(LoginActivity.this, "api_key"));
	}

	
	// 双击退出
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Utils.showSuperCardToast(LoginActivity.this, getResources()
						.getString(R.string.exit));
				mExitTime = System.currentTimeMillis();
			} else {
				XtdApplication.getInstance().exit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
