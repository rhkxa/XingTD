package com.gps808.app.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.adapter.RoutesListViewAdapter;
import com.gps808.app.adapter.XbDriver;
import com.gps808.app.bean.XbRoute;
import com.gps808.app.bean.XbVehicle;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.fragment.SearchFragment;
import com.gps808.app.fragment.SearchFragment.OnSearchClickListener;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.UrlConfig;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public class RoutesActivity extends BaseActivity {

	private PullToRefreshListView routes_list;
	private HeaderFragment headerFragment;
	private RoutesListViewAdapter rAdapter;
	private String place = "";
	private int pagenum = 0;
	private final int pageSize = 10;
	private List<XbRoute> xbRoutes = new ArrayList<XbRoute>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_routes);
		init();
		getData(false);

	}

	private void init() {
		// TODO Auto-generated method stub
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("线路列表");
		SearchFragment searchFragment = (SearchFragment) this
				.getSupportFragmentManager().findFragmentById(R.id.search_bar);
		searchFragment.setOnSearchClickListener(new OnSearchClickListener() {

			@Override
			public void onSearch(String key) {
				// TODO Auto-generated method stub

			}
		});
		routes_list = (PullToRefreshListView) findViewById(R.id.routes_list);
		rAdapter = new RoutesListViewAdapter(RoutesActivity.this, xbRoutes);
		routes_list.setAdapter(rAdapter);
		routes_list.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				getData(true);
			}
		});

	}

	private void getData(final boolean isRefresh) {
		if (!isRefresh) {
			showProgressDialog(RoutesActivity.this, "正在加载,请稍等");
		}
		String url = UrlConfig.getVehicleRoutes();
		// { "placeName":"北京","startPage":0,"pageNum":10 }
		JSONObject postData = new JSONObject();
		StringEntity entity = null;
		try {
			postData.put("placeName", place);
			postData.put("startPage", pagenum);
			postData.put("pageNo", pageSize);
			entity = new StringEntity(postData.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpUtil.post(RoutesActivity.this, url, entity, "application/json",
				new jsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONArray response) {
						// TODO Auto-generated method stub
						LogUtils.DebugLog("result json", response.toString());

						xbRoutes.addAll(JSON.parseArray(response.toString(),
								XbRoute.class));
						if (JSON.parseArray(response.toString(),
								XbVehicle.class).size() < pageSize) {
							// routes_list.setMode(Mode.DISABLED);
							// Utils.ToastMessage(CommentActivity.this,
							// "暂无更多评论");
						} else {
							routes_list.setMode(Mode.PULL_FROM_END);
							pagenum++;
						}
						rAdapter.notifyDataSetChanged();

						super.onSuccess(statusCode, headers, response);
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						routes_list.onRefreshComplete();
						super.onFinish();
					}
				});

	}
}
