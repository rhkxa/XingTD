package com.gps808.app.activity;

import org.json.JSONObject;

import android.os.Bundle;

import com.gps808.app.R;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.PreferenceUtils;
import com.gps808.app.utils.UrlConfig;

/**
 * 代金券
 * 
 * @author yourname
 * 
 */
public class VoucherActivity extends BaseActivity {
	private HeaderFragment headerFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vehicle);
		init();
		loadData();
	}

	private void init() {
		// TODO Auto-generated method stub
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("我的代金券");
	}

	private void loadData() {
//		String uid = PreferenceUtils.getInstance(VoucherActivity.this)
//				.getStringValue("uid");
//		HttpUtil.get(UrlConfig.getCoupons(uid), new jsonHttpResponseHandler() {
//			@Override
//			public void onSuccess(JSONObject arg0) {
//				// TODO Auto-generated method stub
//				super.onSuccess(arg0);
//			}
//		});

	}
}
