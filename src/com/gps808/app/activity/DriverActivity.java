package com.gps808.app.activity;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONArray;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.adapter.DriverListAdapter;
import com.gps808.app.adapter.XbDriver;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.models.XbVehicle;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.UrlConfig;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public class DriverActivity extends BaseActivity {
	private PullToRefreshListView driver_list;
	private HeaderFragment headerFragment;
	private DriverListAdapter dAdapter;
	private int pagenum = 0;
	private final int pageSize = 10;
	private List<XbDriver> xbDrivers = new ArrayList<XbDriver>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_driver);
		init();
		getData(true);

	}

	private void init() {
		// TODO Auto-generated method stub
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("司机列表");
		headerFragment.setImageButtonResource(R.drawable.xtd_action_add);
		headerFragment.setCommentBtnListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DriverActivity.this,
						EditDrvierActivity.class);
				startActivityForResult(intent, 0);
			}
		});
		driver_list = (PullToRefreshListView) findViewById(R.id.driver_list);
		dAdapter = new DriverListAdapter(DriverActivity.this, xbDrivers);
		driver_list.setAdapter(dAdapter);
		driver_list.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				getData(false);
			}
		});
		driver_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DriverActivity.this,
						EditDrvierActivity.class);
				intent.putExtra("driverId", xbDrivers.get(arg2 - 1)
						.getDriverId());
				startActivityForResult(intent, 1);
			}
		});

	}

	private void getData(final boolean isFeresh) {

		String url = UrlConfig.getDrivers(pagenum, pageSize);
		HttpUtil.get(DriverActivity.this, url, new jsonHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				if (isFeresh) {
					showProgressDialog(DriverActivity.this, "正在加载,请稍等");
				}
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				// TODO Auto-generated method stub
				if (isFeresh) {
					xbDrivers.clear();
				}
				LogUtils.DebugLog("result json", response.toString());
				xbDrivers.addAll(JSON.parseArray(response.toString(),
						XbDriver.class));
				if (JSON.parseArray(response.toString(), XbVehicle.class)
						.size() < pageSize) {
					driver_list.setMode(Mode.DISABLED);
					// Utils.ToastMessage(CommentActivity.this,
					// "暂无更多评论");
				} else {
					driver_list.setMode(Mode.PULL_FROM_END);
					pagenum++;
				}
				dAdapter.notifyDataSetChanged();
				super.onSuccess(statusCode, headers, response);
			}
		});

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		LogUtils.DebugLog("返回" + arg1);
		if (arg1 == RESULT_OK) {
			getData(true);
		}
		super.onActivityResult(arg0, arg1, arg2);
	}

}
