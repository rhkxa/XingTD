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
import com.gps808.app.adapter.PoliceListViewAdapter;
import com.gps808.app.bean.XbPolice;
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

/**
 * 报警列表
 * 
 * @author JIA
 * 
 */
public class PolicesActivity extends BaseActivity {

	private PullToRefreshListView police_list;
	private HeaderFragment headerFragment;
	private PoliceListViewAdapter pAdapter;
	private int startPage = 0;
	private final int pageNum = 10;
	private List<XbPolice> xbPolices = new ArrayList<XbPolice>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_police);
		init();
		getData(false);

	}

	private void init() {
		// TODO Auto-generated method stub
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("报警列表");
		 SearchFragment searchFragment= (SearchFragment) this.getSupportFragmentManager().findFragmentById(R.id.search_bar);
		    searchFragment.setOnSearchClickListener(new OnSearchClickListener() {
				
				@Override
				public void onSearch(String key) {
					// TODO Auto-generated method stub
					
				}
			});
		police_list = (PullToRefreshListView) findViewById(R.id.police_list);
		police_list.setMode(Mode.PULL_FROM_END);
		pAdapter = new PoliceListViewAdapter(PolicesActivity.this, xbPolices);
		police_list.setAdapter(pAdapter);
		police_list.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				getData(true);
			}
		});
		

	}

	private void getData(final boolean isRefresh) {
		if (!isRefresh) {
			showProgressDialog(PolicesActivity.this, "正在加载,请稍等");
		}
		String url = UrlConfig.getVehicleAlarms(startPage, pageNum);
		HttpUtil.get(url,

		new jsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				// TODO Auto-generated method stub
				LogUtils.DebugLog("result json", response.toString());
				xbPolices.addAll(JSON.parseArray(response.toString(),
						XbPolice.class));
				if (JSON.parseArray(response.toString(), XbPolice.class).size() < pageNum) {
					police_list.setMode(Mode.DISABLED);
					// Utils.ToastMessage(CommentActivity.this,
					// "暂无更多评论");
				} else {
					startPage++;
				}
				pAdapter.notifyDataSetChanged();
			
				super.onSuccess(statusCode, headers, response);
			}
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				police_list.onRefreshComplete();
				super.onFinish();
			}
		});

	}
}
