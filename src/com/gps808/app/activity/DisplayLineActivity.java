package com.gps808.app.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.gps808.app.R;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.models.XbDisplayLine;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.FileUtils;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.PreferenceUtils;
import com.gps808.app.utils.StringUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;
import com.gps808.app.view.FancyButton;
import com.loopj.android.http.JsonHttpResponseHandler;

public class DisplayLineActivity extends BaseActivity {

	// 地图相关
	MapView mMapView;
	BaiduMap mBaiduMap;
	Polyline mPolyline;
	Polyline mColorfulPolyline;
	Polyline mTexturePolyline;
	BitmapDescriptor endIcon = null;
	BitmapDescriptor startIcon = null;
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode = LocationMode.NORMAL;
	BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
			.fromResource(R.drawable.xtd_car_position);;
	String rid;
	private final String saveFile = "Routes";
	private int handler_runnable_time;
	private FancyButton line_navi;
	private boolean isNavi = true;
	private boolean isMatch = false;
	private TextView show;
	int macthNum = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_display_line);
		init();

	}

	private void init() {
		// TODO Auto-generated method stub
		rid = getIntent().getStringExtra("rid");
		HeaderFragment headerFragment = (HeaderFragment) this
				.getSupportFragmentManager().findFragmentById(R.id.title);
		headerFragment.setTitleText("路线详情");
		handler_runnable_time = PreferenceUtils.getInstance(
				DisplayLineActivity.this).getTrackTime() * 1000;
		endIcon = BitmapDescriptorFactory.fromResource(R.drawable.map_end_icon);
		startIcon = BitmapDescriptorFactory
				.fromResource(R.drawable.map_start_icon);
		// 初始化地图
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
		mBaiduMap.setMapStatus(msu);
		// 隐藏百度logo和 ZoomControl
		int count = mMapView.getChildCount();
		for (int i = 0; i < count; i++) {
			View child = mMapView.getChildAt(i);
			if (child instanceof ImageView || child instanceof ZoomControls) {
				child.setVisibility(View.INVISIBLE);
			}
		}
		show = (TextView) findViewById(R.id.show);
		// 开始导航
		line_navi = (FancyButton) findViewById(R.id.line_navi);
		line_navi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (isNavi) {
					showProgressDialog(DisplayLineActivity.this, "正在匹配导航车辆，请稍等");
					startLocation();
				} else {
					stopLocation();
					line_navi.setText("开始导航");
					Utils.ToastMessage(DisplayLineActivity.this, "导航已关闭");
					isMatch = false;
					macthNum = 0;
					isNavi=true;
				}
			}
		});
		// 从本地获取
		String content = FileUtils.read(DisplayLineActivity.this, saveFile
				+ rid);
		LogUtils.DebugLog("read content ", saveFile + rid + content);
		if (StringUtils.isEmpty(content)) {
			getData();
		} else {
			XbDisplayLine xbDisplayLine = JSON.parseObject(content,
					XbDisplayLine.class);
			parseData(xbDisplayLine);
		}

	}

	/**
	 * 添加点、线、多边形、圆、文字
	 */
	public void addCustomElementsDemo(List<LatLng> points) {
		if (points.size() >= 2) {
			// 添加普通折线绘制
			OverlayOptions ooPolyline = new PolylineOptions().width(10)
					.color(getResources().getColor(R.color.app_green))
					.points(points);
			mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
			mBaiduMap.addOverlay(new MarkerOptions().position(points.get(0))
					.icon(startIcon));
			mBaiduMap.addOverlay(new MarkerOptions().position(
					points.get(points.size() - 1)).icon(endIcon));
		}
	}

	private void getData() {
		String url = UrlConfig.getVehicleRoutesInfo(rid);
		HttpUtil.get(DisplayLineActivity.this, url,
				new jsonHttpResponseHandler() {
					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						showProgressDialog(DisplayLineActivity.this, "正在加载路线信息");
						super.onStart();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						LogUtils.DebugLog("result json", response.toString());
						XbDisplayLine xbDisplayLine = JSON.parseObject(
								response.toString(), XbDisplayLine.class);
						FileUtils.write(DisplayLineActivity.this, saveFile
								+ rid, response.toString());
						parseData(xbDisplayLine);
						super.onSuccess(statusCode, headers, response);
					}
				});
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

	private void parseData(XbDisplayLine xbDisplayLine) {
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

	public void clearClick() {
		// 清除所有图层
		mMapView.getMap().clear();
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
			if (!isMatch) {
				getMatch(location.getLongitude() + "," + location.getLatitude());
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

		public void onReceivePoi(BDLocation poiLocation) {
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
		stopLocation();
		super.onDestroy();
	}

	String showStr = "";

	private void getMatch(String loc) {
		macthNum++;
		if (macthNum <= 12) {
			String url = UrlConfig.getMatchVichcle(loc);
			HttpUtil.get(DisplayLineActivity.this, url,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							// TODO Auto-generated method stub
							if (Utils.requestOk(response)) {
								dismissProgressDialog();
								isMatch = true;
								isNavi = false;
								line_navi.setText("结束导航");
							}
							super.onSuccess(statusCode, headers, response);
						}
					});
		} else {
			macthNum = 0;
			stopLocation();
			dismissProgressDialog();
			Utils.ToastMessage(DisplayLineActivity.this, "对不起，附近没有可导航的车辆");
		}

	}

}
