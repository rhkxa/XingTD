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
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.adapter.VehicleListAdapter;
import com.gps808.app.bean.XbVehicle;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.fragment.SearchFragment;
import com.gps808.app.fragment.SearchFragment.OnSearchClickListener;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.UrlConfig;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;

/**
 * 车辆列表
 * 
 * @author JIA
 * 
 */
public class VehiclesActivity extends BaseActivity {
	private RadioGroup vehicle_rg;
	private PullToRefreshListView vehicle_list;
	private HeaderFragment headerFragment;
	private VehicleListAdapter vAdapter;
	private int status = 0;
	private String search = "";
	private int startPage = 0;
	private final int pageNum = 10;
	private List<XbVehicle> xbVehicles = new ArrayList<XbVehicle>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vehicle);
		init();
		getData(true);

	}

	private void init() {
		// TODO Auto-generated method stub
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("车辆列表");
		SearchFragment searchFragment = (SearchFragment) this
				.getSupportFragmentManager().findFragmentById(R.id.search_bar);
		searchFragment.setOnSearchClickListener(new OnSearchClickListener() {

			@Override
			public void onSearch(String key) {
				// TODO Auto-generated method stub
				search=key;
				getData(true);

			}
		});
		vehicle_list = (PullToRefreshListView) findViewById(R.id.vehicle_list);
		vAdapter = new VehicleListAdapter(VehiclesActivity.this, xbVehicles);
		vehicle_list.setAdapter(vAdapter);
		vehicle_list.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				getData(false);
			}
		});
		vehicle_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(VehiclesActivity.this,
						CarDetailsActivity.class);
				intent.putExtra("vid", xbVehicles.get(arg2 - 1).getVid());
				startActivity(intent);
			}
		});
		vehicle_rg = (RadioGroup) findViewById(R.id.vehicle_rg);
		vehicle_rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				startPage = 0;
				switch (arg0.getCheckedRadioButtonId()) {
				case R.id.vehicle_all:
					status = 0;
					break;
				case R.id.vehicle_onlion:
					status = 1;
					break;
				case R.id.vehicle_offline:
					status = 2;
					break;
				}
				getData(true);
			}

		});

	}

	private void getData(final boolean isClear) {
		if (isClear) {
			showProgressDialog(VehiclesActivity.this, "正在加载,请稍等");
		}
		String url = UrlConfig.getVehicleVehicleByPage();
		JSONObject postData = new JSONObject();
		StringEntity entity = null;
		try {
			postData.put("search", search);
			postData.put("status", status);
			postData.put("startPage", startPage);
			postData.put("pageNum", pageNum);
			entity = new StringEntity(postData.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LogUtils.DebugLog("post json" + postData.toString());
		HttpUtil.post(VehiclesActivity.this, url, entity, "application/json",
				new jsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONArray response) {
						// TODO Auto-generated method stub
						LogUtils.DebugLog("result json", response.toString());
						if (isClear) {
							xbVehicles.clear();
						}
						xbVehicles.addAll(JSON.parseArray(response.toString(),
								XbVehicle.class));
						if (JSON.parseArray(response.toString(),
								XbVehicle.class).size() < pageNum) {
							vehicle_list.setMode(Mode.DISABLED);
							// Utils.ToastMessage(CommentActivity.this,
							// "暂无更多评论");
						} else {
							vehicle_list.setMode(Mode.PULL_FROM_END);
							startPage++;
						}
						vAdapter.notifyDataSetChanged();
						super.onSuccess(statusCode, headers, response);
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						vehicle_list.onRefreshComplete();
						super.onFinish();
					}
				});

	}

}
