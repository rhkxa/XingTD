package com.gps808.app.activity;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.adapter.CommentListViewAdapter;
import com.gps808.app.bean.XbVehicle;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.UrlConfig;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 车辆列表
 * 
 * @author JIA
 * 
 */
public class VehiclesActivity extends BaseActivity {
	private RadioGroup voucher_rg;
	private PullToRefreshListView voucher_list;
	private HeaderFragment headerFragment;
	private CommentListViewAdapter commentListViewAdapter;
	private int pagenum = 0;
	private final int pageSize = 10;
	private List<XbVehicle> xbVehicles = new ArrayList<XbVehicle>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voucher);
		init();
		getData("",0);

	}

	private void init() {
		// TODO Auto-generated method stub

	}

	private void getData(String key, int status) {
		// { "plateNo":"","simNo":"","status":0,"startPage":0,"pageNo":10 }
		String url = UrlConfig.getVehicleVehicleByPage();
		JSONObject postData = new JSONObject();
		StringEntity entity = null;
		try {
			postData.put("plateNo", key);
			postData.put("simNo", key);
			postData.put("status", key);
			postData.put("startPage", key);
			postData.put("pageNo", pageSize);
			entity = new StringEntity(postData.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpUtil.post(VehiclesActivity.this, url, entity, "application/json",
				new jsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONArray response) {
						// TODO Auto-generated method stub
						LogUtils.DebugLog("result json", response.toString());
						xbVehicles = JSON.parseArray(response.toString(),
								XbVehicle.class);
						super.onSuccess(statusCode, headers, response);
					}
				});

	}

}
