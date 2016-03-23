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
import com.gps808.app.adapter.PoliceListViewAdapter;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.fragment.SearchFragment;
import com.gps808.app.fragment.SearchFragment.OnSearchClickListener;
import com.gps808.app.models.XbPolice;
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

/**
 * 报警列表
 * 
 * @author JIA
 * 
 */
public class PolicesActivity extends BaseActivity {

	private final String CacheName = "PoliceCache";
	private PullToRefreshListView police_list;
	private HeaderFragment headerFragment;
	private PoliceListViewAdapter pAdapter;
	private int startPage = 0;
	private final int pageNum = 10;
	private List<XbPolice> xbPolices = new ArrayList<XbPolice>();
	private boolean isPush;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_police);
		init();

	}

	private void init() {
		// TODO Auto-generated method stub
		isPush = getIntent().getBooleanExtra("push", false);
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("报警列表");
		headerFragment.setImageButtonResource(R.drawable.xtd_action_setup);
		headerFragment.setCommentBtnListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PolicesActivity.this,
						PoliceSetupActivity.class);
				startActivity(intent);
			}
		});
		SearchFragment searchFragment = (SearchFragment) this
				.getSupportFragmentManager().findFragmentById(R.id.search_bar);
		searchFragment.setOnSearchClickListener(new OnSearchClickListener() {

			@Override
			public void onSearch(String key) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSearchClose() {
				// TODO Auto-generated method stub

			}
		});
		police_list = (PullToRefreshListView) findViewById(R.id.police_list);
		police_list.setMode(Mode.BOTH);
		pAdapter = new PoliceListViewAdapter(PolicesActivity.this, xbPolices);
		police_list.setAdapter(pAdapter);

		police_list.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				startPage = 0;
				getData(true);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				getData(true);
			}

		});

		police_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PolicesActivity.this,
						DisplayPoliceActivity.class);
				intent.putExtra("aid", xbPolices.get(arg2 - 1).getAid());
				startActivity(intent);
			}
		});
		if (!isPush) {
			String cacheStr = FileUtils.read(PolicesActivity.this, CacheName);
			if (StringUtils.isEmpty(cacheStr)) {
				getData(false);
			} else {
				xbPolices.addAll(JSON.parseArray(cacheStr.toString(),
						XbPolice.class));
				pAdapter.notifyDataSetChanged();
			}
		}

	}

	private void getData(final boolean isRefresh) {

		String url = UrlConfig.getVehicleAlarms(startPage, pageNum);
		HttpUtil.get(PolicesActivity.this, url, new jsonHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				if (!isRefresh) {
					showProgressDialog(PolicesActivity.this, "正在加载,请稍等");
				}
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				// TODO Auto-generated method stub
				LogUtils.DebugLog("result json", response.toString());
				if (startPage == 0) {
					FileUtils.write(PolicesActivity.this, CacheName,
							response.toString());
					xbPolices.clear();
				}
				xbPolices.addAll(JSON.parseArray(response.toString(),
						XbPolice.class));
				if (JSON.parseArray(response.toString(), XbPolice.class).size() < pageNum) {
					police_list.setMode(Mode.PULL_FROM_START);
					Utils.ToastMessage(PolicesActivity.this, "暂无更多");
				} else {
					startPage++;
				}
				pAdapter.notifyDataSetChanged();
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				if (("302").equals(responseString)) {
					Intent intent = new Intent(PolicesActivity.this,
							LoginActivity.class);
					intent.putExtra("reset", true);
					startActivity(intent);
				} else {
					Utils.showSuperCardToast(PolicesActivity.this, "请求失败,请重试");
				}
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				police_list.onRefreshComplete();
				super.onFinish();
			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		// 数据为空并且是推送
		if (xbPolices.size() == 0 && isPush) {
			getData(false);
		}

		super.onResume();
	}
}
