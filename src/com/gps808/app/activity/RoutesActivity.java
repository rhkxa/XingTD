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
import android.widget.ListView;
import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.adapter.RoutesListViewAdapter;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.fragment.SearchFragment;
import com.gps808.app.fragment.SearchFragment.OnSearchClickListener;
import com.gps808.app.models.XbRoute;
import com.gps808.app.models.XbVehicle;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.FileUtils;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.StringUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

public class RoutesActivity extends BaseActivity {
	private final String CacheName = "RoutesCache";
	private PullToRefreshListView routes_list;
	private HeaderFragment headerFragment;
	private RoutesListViewAdapter rAdapter;
	private String place = "";
	private int startPage = 0;
	private final int pageNum = 10;
	private List<XbRoute> xbRoutes = new ArrayList<XbRoute>();

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
		routes_list = (PullToRefreshListView) findViewById(R.id.routes_list);
		rAdapter = new RoutesListViewAdapter(RoutesActivity.this, xbRoutes);
		routes_list.setAdapter(rAdapter);
		routes_list.setMode(Mode.BOTH);
		routes_list.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				startPage = 0;
				getData(false);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				getData(false);
			}
		});

		routes_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(RoutesActivity.this,
						DisplayLineActivity.class);
				intent.putExtra("rid", xbRoutes.get(arg2 - 1).getRid());
				startActivity(intent);
			}
		});
		String cacheStr = FileUtils.read(RoutesActivity.this, CacheName);
		if (StringUtils.isEmpty(cacheStr)) {
			getData(false);
		} else {
			xbRoutes.addAll(JSON.parseArray(cacheStr.toString(), XbRoute.class));
			rAdapter.notifyDataSetChanged();
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
							xbRoutes.clear();
						}
						xbRoutes.addAll(JSON.parseArray(response.toString(),
								XbRoute.class));
						if (JSON.parseArray(response.toString(),
								XbVehicle.class).size() < pageNum) {
							routes_list.setMode(Mode.PULL_FROM_START);
							Utils.ToastMessage(RoutesActivity.this, "暂无更多路线信息");
						} else {
							routes_list.setMode(Mode.BOTH);
							startPage++;
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
