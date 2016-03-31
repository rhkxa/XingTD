package com.gps808.app.activity;

import android.os.Bundle;
import android.view.View;

import com.baidu.navisdk.adapter.BNRouteGuideManager;
import com.baidu.navisdk.adapter.BNRouteGuideManager.OnNavigationListener;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.LogUtils;

public class GuideVoiceActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = null;
		// 使用传统接口
		view = BNRouteGuideManager.getInstance().onCreate(this,
				mOnNavigationListener);

		if (view != null) {
			setContentView(view);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

		BNRouteGuideManager.getInstance().onResume();

	}

	protected void onPause() {
		super.onPause();

		BNRouteGuideManager.getInstance().onPause();

	};

	@Override
	protected void onDestroy() {
		super.onDestroy();

		BNRouteGuideManager.getInstance().onDestroy();

	}

	@Override
	protected void onStop() {
		super.onStop();

		BNRouteGuideManager.getInstance().onStop();

	}

	@Override
	public void onBackPressed() {

		BNRouteGuideManager.getInstance().onBackPressed(false);

	}

	public void onConfigurationChanged(
			android.content.res.Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		BNRouteGuideManager.getInstance().onConfigurationChanged(newConfig);

	};

	private OnNavigationListener mOnNavigationListener = new OnNavigationListener() {

		@Override
		public void onNaviGuideEnd() {
			setResult(1000);
			finish();
		}

		@Override
		public void notifyOtherAction(int actionType, int arg1, int arg2,
				Object obj) {

			if (actionType == 0) {
				LogUtils.DebugLog("notifyOtherAction actionType = "
						+ actionType + ",导航到达目的地！");
			}

			LogUtils.DebugLog("actionType:" + actionType + "arg1:" + arg1
					+ "arg2:" + arg2 + "obj:" + obj.toString());
		}

	};
}
