package com.gps808.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.gps808.app.R;
import com.gps808.app.fragment.FragmentGift;
import com.gps808.app.fragment.FragmentHome;
import com.gps808.app.fragment.FragmentMenu;
import com.gps808.app.fragment.FragmentMyself;
import com.gps808.app.fragment.FragmentShop;
import com.gps808.app.push.PushDemoActivity;
import com.gps808.app.push.PushUtils;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.UpdateManager;
import com.gps808.app.utils.Utils;
import com.gps808.app.utils.XtdApplication;
import com.gps808.app.view.CircleImageView;

/**
 * 主界面
 * 
 * @author JIA
 * 
 * 
 * 
 */
public class MainActivity extends BaseActivity {
	private long mExitTime = 0;
	private RadioGroup group;
	private FragmentManager mFragmentMan;
	private Fragment mContent;
	private CircleImageView main_shopcar;

	// private BadgeView badge;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initWithApiKey();
		UpdateManager.getUpdateManager().checkAppUpdate(MainActivity.this,
				false);
		init();
	}

	private void init() {
	

	}

	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		// String count = PreferenceUtils.getInstance(MainActivity.this)
		// .getStringValue("count");
		// if (!StringUtils.isEmpty(count)) {
		// badge.setText(count);
		// badge.show();
		// }

		super.onResume();
	}

	// 以apikey的方式绑定
	private void initWithApiKey() {
		// Push: 无账号初始化，用api key绑定
		PushManager.startWork(getApplicationContext(),
				PushConstants.LOGIN_TYPE_API_KEY,
				PushUtils.getMetaValue(MainActivity.this, "api_key"));
	}

	// 双击退出
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Utils.showSuperCardToast(MainActivity.this, getResources()
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
