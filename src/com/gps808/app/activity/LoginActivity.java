package com.gps808.app.activity;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.gps808.app.bean.Update;
import com.gps808.app.bean.XbUser;
import com.gps808.app.dialog.CustomChoseDialog;
import com.gps808.app.dialog.CustomOkDialog;
import com.gps808.app.push.PushUtils;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.CyptoUtils;
import com.gps808.app.utils.FileUtils;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.PreferenceUtils;
import com.gps808.app.utils.StringUtils;
import com.gps808.app.utils.UpdateManager;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;
import com.gps808.app.utils.XtdApplication;
import com.gps808.app.view.FancyButton;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

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
		checkUpdate();
	}

	private void init() {
		// TODO Auto-generated method stub
		initWithApiKey();
		// 启动线程,延迟2秒
		handler.postDelayed(runnable, 2000);
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
		userName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				handler.removeCallbacks(runnable);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

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
				entity = new StringEntity(JSON.toJSONString(pUser), "UTF-8");
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
		if (PreferenceUtils.getInstance(LoginActivity.this).getPush()) {
			PushManager.startWork(getApplicationContext(),
					PushConstants.LOGIN_TYPE_API_KEY,
					PushUtils.getMetaValue(LoginActivity.this, "api_key"));
		} else {
			PushManager.stopWork(getApplicationContext());
		}
	}

	// 自动登录线程,2秒后自动登陆
	Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			// 要做的事情
			if (mPreferences.getAutoLogin()) {
				if (!StringUtils.isEmpty(mPreferences.getUserState())) {
					login.performClick();
				}
			}
		}
	};

	// app检查更新
	private void checkUpdate() {
		String url = UrlConfig.getAppVersion();
		HttpUtil.get(url, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				LogUtils.DebugLog("result", response.toString());

				Update update = JSON.parseObject(response.toString(),
						Update.class);
				int currentVersion = Utils
						.getCurrentVersion(LoginActivity.this);
				if (update.getAppVer() > currentVersion) {
					if (update.isForceUpdate()) {
						showDownloadDialog(update);
					} else {
						showNoticeDialog(update);
					}
				} else {
					init();
				}
				super.onSuccess(statusCode, headers, response);

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				init();
				super.onFailure(statusCode, headers, responseString, throwable);
			}

		});
	}

	private void showNoticeDialog(final Update update) {

		CustomChoseDialog.Builder builder = new CustomChoseDialog.Builder(
				LoginActivity.this);
		builder.setMessage(update.getVersionDesc());
		builder.setTitle("版本更新");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 设置你的操作事项
				showDownloadDialog(update);
			}
		});

		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						init();
					}
				});
		builder.create().show();
	}

	private void showDownloadDialog(Update update) {
		// TODO Auto-generated method stub
		String apkFile = FileUtils.getSDRoot() + "XingTD" + "XtdApp_"
				+ update.getReleaseTime() + ".apk";
		HttpUtil.get(update.getUpdateUrl(), new BinaryHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgress(long bytesWritten, long totalSize) {
				// TODO Auto-generated method stub
				super.onProgress(bytesWritten, totalSize);
			}
		});
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
