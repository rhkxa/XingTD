package com.gps808.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ZoomControls;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.gps808.app.R;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.models.XbDisplayLine;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.Utils;
import com.gps808.app.view.FancyButton;

public class GuideNormalActivity extends BaseActivity {
	// 地图相关
	MapView mMapView;
	BaiduMap mBaiduMap;
	Polyline mPolyline;
	BitmapDescriptor endIcon = null;
	BitmapDescriptor startIcon = null;
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode = LocationMode.NORMAL;
	BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
			.fromResource(R.drawable.xtd_car_position);
	private XbDisplayLine xbDisplayLine;
	private FancyButton normal_navi;
	private boolean isNavi = false;
	BitmapDescriptor mBlueTexture = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_road_blue_arrow);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_guide_normal);
		init();

	}

	private void init() {
		// TODO Auto-generated method stub
		xbDisplayLine = JSON.parseObject(getIntent().getStringExtra("route"),
				XbDisplayLine.class);
		HeaderFragment headerFragment = (HeaderFragment) this
				.getSupportFragmentManager().findFragmentById(R.id.title);
		headerFragment.setTitleText("普通导航");
		endIcon = BitmapDescriptorFactory.fromResource(R.drawable.map_end_icon);
		startIcon = BitmapDescriptorFactory
				.fromResource(R.drawable.map_start_icon);
		// 初始化地图
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		mMapView.showZoomControls(false);
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
		mBaiduMap.setMapStatus(msu);
		// 隐藏百度LOGO和 ZoomControl
		int count = mMapView.getChildCount();
		for (int i = 0; i < count; i++) {
			View child = mMapView.getChildAt(i);
			if (child instanceof ImageView || child instanceof ZoomControls) {
				child.setVisibility(View.INVISIBLE);
			}
		}
		parseData();
		startLocation();
		// 普通导航
		normal_navi = (FancyButton) findViewById(R.id.normal_navi);
		normal_navi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (isNavi) {
					normal_navi.setText("结束导航");
					Utils.ToastMessage(GuideNormalActivity.this, "导航功能开启");
					startLocation();
				} else {
					normal_navi.setText("开始导航");
					Utils.ToastMessage(GuideNormalActivity.this, "导航功能关闭");
					stopLocation();
				}
				isNavi = !isNavi;
			}
		});

	}

	/**
	 * 添加点、线、多边形、圆、文字
	 */
	public void addCustomElementsDemo(List<LatLng> points) {
		if (points.size() >= 2) {
			// 添加普通折线绘制
			OverlayOptions ooPolyline = new PolylineOptions().width(15)
					.customTexture(mBlueTexture).dottedLine(true)
					.points(points);
			mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
			mBaiduMap.addOverlay(new MarkerOptions().position(points.get(0))
					.icon(startIcon));
			mBaiduMap.addOverlay(new MarkerOptions().position(
					points.get(points.size() - 1)).icon(endIcon));
		}
	}

	private void startLocation() {
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true, mCurrentMarker));
		// 定位初始化
		mLocClient = new LocationClient(getApplicationContext());
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setNeedDeviceDirect(true);
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(5000);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	private void stopLocation() {
		if (mLocClient != null) {
			// 退出时销毁定位
			mLocClient.stop();
			// 关闭定位图层
			mBaiduMap.setMyLocationEnabled(false);
		}
	}

	private void parseData() {
		List<LatLng> points = new ArrayList<LatLng>();
		String[] strLng = Utils.getSplit(xbDisplayLine.getTrack(), ";");
		int size = strLng.length;
		LatLng latLng = null;
		double[] doubleLng;
		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		for (int i = 0; i < size; i++) {
			doubleLng = Utils.getLng(strLng[i]);
			latLng = new LatLng(doubleLng[1], doubleLng[0]);
			points.add(latLng);
			builder.include(latLng);
		}
		// 缩放地图，使所有Overlay都在合适的视野内
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngBounds(builder
				.build()));
		addCustomElementsDemo(points);
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
				return;
			}
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(location.getDirection())
					.latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			LatLng ll = new LatLng(location.getLatitude(),
					location.getLongitude());
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
			mBaiduMap.animateMapStatus(u);

		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mMapView.onDestroy();
		mCurrentMarker.recycle();
		endIcon.recycle();
		startIcon.recycle();
		mBlueTexture.recycle();
		stopLocation();
		super.onDestroy();
	}

}
