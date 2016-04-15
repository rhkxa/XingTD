package com.gps808.app.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.dialog.CustomChoseDialog;
import com.gps808.app.dialog.CustomOkDialog;
import com.gps808.app.models.Update;
import com.gps808.app.models.XbUser;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.CyptoUtils;
import com.gps808.app.utils.FileUtils;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.PreferenceUtils;
import com.gps808.app.utils.StringUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;
import com.gps808.app.view.FancyButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
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

	private EditText userName;
	private EditText passWord;
	private CheckBox autoLoginBox;
	private CheckBox savePwdBox;
	private TextView login_to_register;
	private TextView login_forget_pass;
	private boolean isReLogin;
	private PreferenceUtils mPreferences;
	private TextView login_test;
	private boolean isTest = false;
	private FancyButton logo_beidou, logo_gps;
	private String beidou = "http://app.bdgps808.com/ws/0.1/";
	private String gps = "http://app.gps808.com/ws/0.1/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		init();
		checkUpdate();
		// UpdateManager.getUpdateManager().checkAppUpdate(LoginActivity.this,
		// false);
	}

	private void init() {
		// TODO Auto-generated method stub
		isReLogin = getIntent().getBooleanExtra("reset", false);
		mPreferences = PreferenceUtils.getInstance(LoginActivity.this);
		userName = (EditText) findViewById(R.id.username);
		passWord = (EditText) findViewById(R.id.password);
		autoLoginBox = (CheckBox) findViewById(R.id.autoLoginBox);
		savePwdBox = (CheckBox) findViewById(R.id.savePwdBox);
		login = (FancyButton) findViewById(R.id.login);

		login_to_register = (TextView) findViewById(R.id.login_to_register);
		login_forget_pass = (TextView) findViewById(R.id.login_forget_pass);
		login_test = (TextView) findViewById(R.id.login_test);
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

		login_to_register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String showStr = "1、安装我公司的车辆定位设备，将免费使用该应用。<br>2、购买、安装设备请与我们联系，联系电话：0317-4227916。";
				CustomOkDialog register = new CustomOkDialog(
						LoginActivity.this, "新用户", showStr, null);
				register.show();
			}
		});
		login_forget_pass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Utils.toExploer(
						LoginActivity.this,
						"http://mokebao.molink.cn/index.php?g=Wap&m=Index&a=index&token=bldbtc1458693848");
			}
		});
		login_test.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				isTest = true;
				getLogin("ty", "123123");
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
				handler.removeCallbacks(runnable);
				HttpUtil.cancelAllRequest();
				getLogin(userName.getText().toString(), passWord.getText()
						.toString());
			}
		});
		logo_beidou = (FancyButton) findViewById(R.id.logo_beidou);
		logo_gps = (FancyButton) findViewById(R.id.logo_gps);
		logo_beidou.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mPreferences.setPingTai(0);
				logo_beidou.setBackgroundColor(getResources().getColor(
						R.color.app_blue));
				logo_beidou.setIconResource(R.drawable.xtd_icon_blue);
				logo_beidou
						.setTextColor(getResources().getColor(R.color.white));
				logo_gps.setBackgroundColor(getResources().getColor(
						R.color.app_backgroud));
				logo_gps.setIconResource(R.drawable.xtd_icon_gray);
				logo_gps.setTextColor(getResources().getColor(R.color.text));
				UrlConfig.SERVER = beidou;

			}
		});

		logo_gps.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mPreferences.setPingTai(1);
				logo_gps.setBackgroundColor(getResources().getColor(
						R.color.app_blue));
				logo_gps.setIconResource(R.drawable.xtd_icon_blue);
				logo_gps.setTextColor(getResources().getColor(R.color.white));

				logo_beidou.setBackgroundColor(getResources().getColor(
						R.color.app_backgroud));
				logo_beidou.setIconResource(R.drawable.xtd_icon_gray);
				logo_beidou.setTextColor(getResources().getColor(R.color.text));
				UrlConfig.SERVER = gps;
			}
		});

		if (mPreferences.getPingTai() == 0) {
			logo_beidou.setBackgroundColor(getResources().getColor(
					R.color.app_blue));
			logo_beidou.setIconResource(R.drawable.xtd_icon_blue);
			logo_beidou.setTextColor(getResources().getColor(R.color.white));
			UrlConfig.SERVER = beidou;
		} else {
			logo_gps.setBackgroundColor(getResources().getColor(
					R.color.app_blue));
			logo_gps.setIconResource(R.drawable.xtd_icon_blue);
			logo_gps.setTextColor(getResources().getColor(R.color.white));
			UrlConfig.SERVER = gps;
		}

	}

	// 登录
	private void getLogin(final String userName, final String passWord) {
		if (Utils.isNetWorkConnected(LoginActivity.this)) {
			String url = UrlConfig.getLogin();
			JSONObject params = new JSONObject();
			StringEntity entity = null;
			try {
				params.put("username", userName);
				params.put("password", CyptoUtils.MD5(passWord));
				entity = new StringEntity(params.toString(), "UTF-8");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			LogUtils.DebugLog("post json", params.toString());
			HttpUtil.post(LoginActivity.this, url, entity, "application/json",
					new jsonHttpResponseHandler() {
						@Override
						public void onStart() {
							// TODO Auto-generated method stub
							showProgressDialog(LoginActivity.this, "登录中，请稍等");
							super.onStart();
						}

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
								if (!isTest) {
									mPreferences.setUserState(xbUser
											.getUserType());
									mPreferences.setUserPW(passWord);
									mPreferences.setUserName(userName);
								}
								mPreferences.setUserNick(xbUser.getUserName());

								if (!isReLogin) {
									Intent intent = new Intent(
											LoginActivity.this,
											MainActivity.class);
									startActivity(intent);
								}
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

						}

						@Override
						public void onFinish() {
							// TODO Auto-generated method stub
							dismissProgressDialog();
							super.onFinish();
						}
					});

		} else {
			Utils.showSuperCardToast(LoginActivity.this, getResources()
					.getString(R.string.network_not_connected));
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
				if (mPreferences.getUserState() > 0) {
					login.performClick();
				}
			}
		}
	};

	// APP检查更新
	private void checkUpdate() {
		String url = UrlConfig.getAppVersion();
		HttpUtil.get(LoginActivity.this, url, new JsonHttpResponseHandler() {
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
					// 启动线程,延迟2秒
					handler.postDelayed(runnable, 2000);
				}
				super.onSuccess(statusCode, headers, response);

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				// 启动线程,延迟2秒
				handler.postDelayed(runnable, 2000);
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
						// 启动线程,延迟2秒
						handler.postDelayed(runnable, 2000);
					}
				});
		builder.create().show();
	}

	/**
	 * 显示下载对话框
	 */
	private ProgressBar mProgress;
	private TextView mProgressText;
	private TextView mUpdataBrower;

	private void showDownloadDialog(Update update) {
		AlertDialog.Builder builder = new Builder(this);
		final LayoutInflater inflater = LayoutInflater.from(this);
		View v = inflater.inflate(R.layout.widget_update_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		mProgressText = (TextView) v.findViewById(R.id.update_progress_text);
		mUpdataBrower = (TextView) v.findViewById(R.id.update_to_brower);

		builder.setView(v);
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				HttpUtil.cancelRequest(LoginActivity.this);
				dialog.dismiss();

			}
		});
		builder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				HttpUtil.cancelRequest(LoginActivity.this);
			}
		});
		mUpdataBrower.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Utils.toExploer(LoginActivity.this,
						"http://app.gps808.com/update.jsp");
			}
		});
		Dialog downloadDialog = builder.create();
		downloadDialog.setCanceledOnTouchOutside(false);
		downloadDialog.show();
		downloadApk(update);
	}

	private void downloadApk(final Update update) {
		// TODO Auto-generated method stub
		final String apkFile = FileUtils.getSDRoot() + "/XingTD" + "/XtdApp_"
				+ update.getReleaseTime() + ".apk";
		LogUtils.DebugLog(apkFile);
		HttpUtil.get(LoginActivity.this, update.getUpdateUrl(),
				new FileAsyncHttpResponseHandler(LoginActivity.this) {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, File arg3) {
						// TODO Auto-generated method stub
						LogUtils.DebugLog("下载失败" + arg3.toString());
					}

					public void onProgress(long bytesWritten, long totalSize) {
						int progressPercentage = (int) (100 * bytesWritten / totalSize);
						mProgress.setProgress(progressPercentage);
						mProgressText.setText(progressPercentage + "%");
					}

					@Override
					protected byte[] getResponseData(HttpEntity entity)
							throws IOException {
						// TODO Auto-generated method stub
						if (entity != null) {
							InputStream instream = entity.getContent();
							long contentLength = entity.getContentLength();
							FileOutputStream buffer = new FileOutputStream(
									getTargetFile(), this.append);
							if (instream != null) {
								try {
									byte[] tmp = new byte[BUFFER_SIZE];
									int l, count = 0;
									// do not send messages if request has been
									// cancelled
									while ((l = instream.read(tmp)) != -1
											&& !Thread.currentThread()
													.isInterrupted()) {
										count += l;
										buffer.write(tmp, 0, l);
										sendProgressMessage(count,
												(int) contentLength);
									}

								} finally {
									AsyncHttpClient
											.silentCloseInputStream(instream);
									buffer.flush();
									AsyncHttpClient
											.silentCloseOutputStream(buffer);
								}
							}
						}
						return null;
					}

					@Override
					public File getTargetFile() {
						// TODO Auto-generated method stub
						return new File(apkFile);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, File arg2) {
						// TODO Auto-generated method stub
						Utils.installApk(LoginActivity.this, apkFile);
						getAppContext().exit();
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
				getAppContext().exit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
