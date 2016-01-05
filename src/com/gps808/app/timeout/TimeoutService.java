package com.gps808.app.timeout;

import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.XtdApplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class TimeoutService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	boolean isrun = true;

	@Override
	public void onCreate() {
		LogUtils.DebugLog("BindService-->onCreate()");
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LogUtils.DebugLog("BindService-->onStartCommand()");
		forceApplicationExit();
		return super.onStartCommand(intent, flags, startId);

	}

	private void forceApplicationExit() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				stopSelf();
				LogUtils.DebugLog("程序超时,关闭程序");
				XtdApplication.getInstance().exit();
			}
		}).start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		isrun = false;
	}

}
