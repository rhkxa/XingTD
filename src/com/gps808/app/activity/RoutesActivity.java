package com.gps808.app.activity;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.adapter.RoutesExpandableListAdapter;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.fragment.SearchFragment;
import com.gps808.app.fragment.SearchFragment.OnSearchClickListener;
import com.gps808.app.models.RoutesInfo;
import com.gps808.app.models.XbVehicle;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.FileUtils;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.StringUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

public class RoutesActivity extends BaseActivity {
	private final String CacheName = "RoutesCache";
	private PullToRefreshExpandableListView routes_expandablelist;
	private ExpandableListView mExpandableListView;
	private HeaderFragment headerFragment;
	private String place = "";
	private int startPage = 0;
	private final int pageNum = 10;
	private List<RoutesInfo> routesList = new ArrayList<RoutesInfo>();
	private RoutesExpandableListAdapter routesExpandableListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_routes);
		init();

	}

	private void init() {
		// TODO Auto-generated method stub
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("线路列表");
		SearchFragment searchFragment = (SearchFragment) this
				.getSupportFragmentManager().findFragmentById(R.id.search_bar);
		searchFragment.setHint("请输入起始地点");
		searchFragment.setOnSearchClickListener(new OnSearchClickListener() {

			@Override
			public void onSearch(String key) {
				// TODO Auto-generated method stub
				place = key;
				getData(true);
			}

			@Override
			public void onSearchClose() {
				// TODO Auto-generated method stub
				place = "";
				getData(true);
			}
		});
		routes_expandablelist = (PullToRefreshExpandableListView) findViewById(R.id.routes_expandablelist);
		routesExpandableListAdapter = new RoutesExpandableListAdapter(
				RoutesActivity.this, routesList);
		mExpandableListView = routes_expandablelist.getRefreshableView();
		mExpandableListView.setAdapter(routesExpandableListAdapter);
		mExpandableListView.setGroupIndicator(null);
		routes_expandablelist.setMode(Mode.BOTH);
		routes_expandablelist
				.setOnRefreshListener(new OnRefreshListener2<ExpandableListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ExpandableListView> refreshView) {
						// TODO Auto-generated method stub
						startPage = 0;
						getData(false);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ExpandableListView> refreshView) {
						// TODO Auto-generated method stub
						getData(false);
					}
				});
		mExpandableListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1,
					int arg2, int arg3, long arg4) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(RoutesActivity.this,
						DisplayLineActivity.class);
				intent.putExtra("rid",
						routesList.get(arg2).getSub().get(arg3).getRid());
				startActivity(intent);
				LogUtils.DebugLog("位置一"+arg2+"位置二"+arg3);
				return true;
			}
		});

		String cacheStr = FileUtils.read(RoutesActivity.this, CacheName);
		if (StringUtils.isEmpty(cacheStr)) {
			getData(false);
		} else {
			routesList.addAll(JSON.parseArray(cacheStr.toString(),
					RoutesInfo.class));
			refreshList();
		}

	}

	private void getData(final boolean isRefresh) {
		String url = UrlConfig.getVehicleRoutes();
		JSONObject postData = new JSONObject();
		StringEntity entity = null;
		try {
			postData.put("placeName", place);
			postData.put("startPage", startPage);
			postData.put("pageNum", pageNum);
			entity = new StringEntity(postData.toString(), "UTF-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LogUtils.DebugLog("post json", postData.toString());
		HttpUtil.post(RoutesActivity.this, url, entity, "application/json",
				new jsonHttpResponseHandler() {
					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						if (isRefresh) {
							showProgressDialog(RoutesActivity.this, "正在加载,请稍等");
						}
						super.onStart();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONArray response) {
						// TODO Auto-generated method stub
						LogUtils.DebugLog("result json", response.toString());
						if (startPage == 0) {
							FileUtils.write(RoutesActivity.this, CacheName,
									response.toString());
							routesList.clear();
						}
						routesList.addAll(JSON.parseArray(response.toString(),
								RoutesInfo.class));
						if (JSON.parseArray(response.toString(),
								XbVehicle.class).size() < pageNum) {
							routes_expandablelist.setMode(Mode.PULL_FROM_START);
							Utils.ToastMessage(RoutesActivity.this, "暂无更多路线信息");
						} else {
							routes_expandablelist.setMode(Mode.BOTH);
							startPage++;
						}
						refreshList();

						super.onSuccess(statusCode, headers, response);
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						routes_expandablelist.onRefreshComplete();
						super.onFinish();
					}
				});

	}

	private void refreshList() {
		if (routesList.size() > 0) {
			routesExpandableListAdapter.notifyDataSetChanged();
			mExpandableListView.expandGroup(0);
		}
	}
}
