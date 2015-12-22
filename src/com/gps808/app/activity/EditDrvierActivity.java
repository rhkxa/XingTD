package com.gps808.app.activity;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import com.gps808.app.R;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.CyptoUtils;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;

public class EditDrvierActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_driver);
		addDriver();
	}

	private void addDriver() {
		String url = UrlConfig.getAddDriver();
		// {"driverId":1211,"driverName":"司机名称","phone":"13112311231"
		// ,"loginName":"登录名称","status":1,"password":"123123"}
		JSONObject params = new JSONObject();
		StringEntity entity = null;
		try {
			params.put("driverId", "0");
			params.put("driverName", "张三");
			params.put("phone", "18619501970");
			params.put("loginName", "jia");
			params.put("status", 1);
			params.put("password", CyptoUtils.MD5("123123"));
			entity = new StringEntity(params.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpUtil.post(EditDrvierActivity.this, url, entity, "application/json",
				new jsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						if (Utils.requestOk(response)) {
							Utils.ToastMessage(EditDrvierActivity.this, "添加成功");
						}
						super.onSuccess(statusCode, headers, response);
					}
				});

	}
}
