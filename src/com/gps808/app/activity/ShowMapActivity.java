package com.gps808.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.gps808.app.R;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;

public class ShowMapActivity extends BaseActivity {

	private MapView mMapView;
	private BaiduMap mBaiduMap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_map);
		init();

	}

	private void init() {
		// TODO Auto-generated method stub
		HeaderFragment headerFragment = (HeaderFragment) this
				.getSupportFragmentManager().findFragmentById(R.id.title);
		headerFragment.setTitleText("离线地图显示");
		// 初始化地图
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// 隐藏百度LOGO和 ZoomControl
		int count = mMapView.getChildCount();
		for (int i = 0; i < count; i++) {
			View child = mMapView.getChildAt(i);
			if (child instanceof ImageView || child instanceof ZoomControls) {
				child.setVisibility(View.INVISIBLE);
			}
		}
		Intent intent = getIntent();
		if (intent.hasExtra("x") && intent.hasExtra("y")) {
			// 当用intent参数时，设置中心点为指定点
			Bundle b = intent.getExtras();
			LatLng p = new LatLng(b.getDouble("y"), b.getDouble("x"));
			MapStatus mapStatus = new MapStatus.Builder().target(p).build();
			MapStatusUpdate msu = MapStatusUpdateFactory
					.newMapStatus(mapStatus);
			mBaiduMap.setMapStatus(msu);
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		// activity 暂停时同时暂停地图控件
		mMapView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// activity 恢复时同时恢复地图控件
		mMapView.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// activity 销毁时同时销毁地图控件
		mMapView.onDestroy();
	}

}
