package com.gps808.app.activity;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONArray;
import android.os.Bundle;
import android.widget.ListView;
import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.adapter.DriverListAdapter;
import com.gps808.app.adapter.XbDriver;
import com.gps808.app.bean.XbVehicle;
import com.gps808.app.fragment.HeaderFragment;
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

	}

	private void getData(final boolean isFeresh) {
		if (isFeresh) {
			showProgressDialog(DriverActivity.this, "正在加载,请稍等");
		}
		String url = UrlConfig.getDrivers(pageSize, pagenum);
		HttpUtil.get(url, new jsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				// TODO Auto-generated method stub
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
}
