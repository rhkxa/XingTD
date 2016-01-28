package com.gps808.app.utils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

import com.gps808.app.R;
import com.gps808.app.activity.LoginActivity;
import com.gps808.app.timeout.ScreenObserver;
import com.gps808.app.timeout.TimeoutService;
import com.gps808.app.timeout.ScreenObserver.ScreenStateListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 
 * @author JIA
 * 
 */
public class BaseActivity extends FragmentActivity {
	public XtdApplication xtdApplication;
	private ProgressDialog progressDialog = null;
	private ScreenObserver mScreenObserver;
	private boolean activityIsActive;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		xtdApplication = (XtdApplication) getApplication();
		XtdApplication.getInstance().addActivity(this);
		// mScreenObserver = new ScreenObserver(this);
		// mScreenObserver.requestScreenStateUpdate(new ScreenStateListener() {
		// @Override
		// public void onScreenOn() {
		// if (!ScreenObserver
		// .isApplicationBroughtToBackground(BaseActivity.this)) {
		// cancelAlarmManager();
		// }
		// }
		//
		// @Override
		// public void onScreenOff() {
		// if (!ScreenObserver
		// .isApplicationBroughtToBackground(BaseActivity.this)) {
		// cancelAlarmManager();
		// setAlarmManager();
		// }
		// }
		// });
	}

	public XtdApplication getAppContext() {
		return xtdApplication;

	}

	public void showProgressDialog(final Activity activity, CharSequence message) {
		if (!activity.isFinishing()) {
			progressDialog = new ProgressDialog(activity);
			progressDialog.setIndeterminate(true);
			progressDialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface arg0) {
					// TODO Auto-generated method stub
					HttpUtil.cancelRequest(activity);
				}
			});
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setMessage(message);
			progressDialog.show();
		}
	}

	public void dismissProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// mScreenObserver.stopScreenStateUpdate();
		dismissProgressDialog();
		super.onDestroy();
	}

	public class jsonHttpResponseHandler extends JsonHttpResponseHandler {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				String responseString) {
			// TODO Auto-generated method stub
			LogUtils.DebugLog("result string", responseString);
			super.onSuccess(statusCode, headers, responseString);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			// TODO Auto-generated method stub
			LogUtils.DebugLog("failure json code=", statusCode + "throwable="
					+ throwable.toString());
			Utils.showSuperCardToast(BaseActivity.this, "请求失败,请重试");
			super.onFailure(statusCode, headers, throwable, errorResponse);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				String responseString, Throwable throwable) {
			// TODO Auto-generated method stub
			LogUtils.DebugLog("failure string code=", statusCode + "  result="
					+ responseString + " throwable=" + throwable.toString());
			if (("302").equals(responseString)) {
				reLogin();
			} else {
				Utils.showSuperCardToast(BaseActivity.this, "请求失败,请重试");
			}
			super.onFailure(statusCode, headers, responseString, throwable);
		}

		@Override
		public void onFinish() {
			super.onFinish();
			dismissProgressDialog();
		}
	}

	public static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	/**
	 * 设置定时器管理器
	 */
	private void setAlarmManager() {
		long numTimeout = 60 * 1000 * 30;// 半小时
		LogUtils.DebugLog("isTimeOutMode=yes,timeout=" + numTimeout);
		Intent alarmIntent = new Intent(getBaseContext(), TimeoutService.class);
		alarmIntent.putExtra("action", "timeout"); // 自定义参数
		PendingIntent pi = PendingIntent.getService(getBaseContext(), 1024,
				alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		long triggerAtTime = (System.currentTimeMillis() + numTimeout);
		am.set(AlarmManager.RTC_WAKEUP, triggerAtTime, pi); // 设定的一次性闹钟，这里决定是否使用绝对时间
		LogUtils.DebugLog("----->设置定时器");
	}

	/**
	 * 取消定时管理器
	 */
	private void cancelAlarmManager() {
		AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(getBaseContext(), TimeoutService.class);
		PendingIntent pi = PendingIntent.getService(getBaseContext(), 1024,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		// 与上面的intent匹配（filterEquals(intent)）的闹钟会被取消
		alarmMgr.cancel(pi);
		LogUtils.DebugLog("----->取消定时器");
	}

	@Override
	protected void onResume() {

		super.onResume();
		// cancelAlarmManager();
		// activityIsActive = true;
		// LogUtils.DebugLog("activityIsActive=" + activityIsActive);
	}

	@Override
	protected void onStop() {
		HttpUtil.cancelRequest(this);
		super.onStop();
		// 当Activity停止Stop时停止此Activity的所有网络请求

		// if (ScreenObserver.isApplicationBroughtToBackground(this)) {
		// cancelAlarmManager();
		// setAlarmManager();
		// }
	}

	private void reLogin() {
		Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
		startActivity(intent);
	}

}
