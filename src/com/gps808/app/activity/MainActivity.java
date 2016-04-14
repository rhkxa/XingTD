package com.gps808.app.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import com.alibaba.fastjson.JSON;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.gps808.app.R;
import com.gps808.app.fragment.SearchFragment;
import com.gps808.app.fragment.SearchFragment.OnSearchClickListener;
import com.gps808.app.map.ZoomControlView;
import com.gps808.app.models.XbVehicle;
import com.gps808.app.push.PushUtils;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.Common;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.PreferenceUtils;
import com.gps808.app.utils.StringUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;
import com.gps808.app.utils.XtdApplication;
import com.gps808.app.view.FancyButton;
import com.gps808.app.view.PengButton;
import com.gps808.app.view.Countdown.CountdownView;
import com.gps808.app.view.Countdown.CountdownView.OnCountdownEndListener;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ZoomControls;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.navisdk.adapter.BaiduNaviManager;

/**
 * 主界面
 * 
 * @author JIA
 * 
 * 
 * 
 */
public class MainActivity extends BaseActivity {

	/**
	 * MapView 是地图主控件
	 */
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private InfoWindow mInfoWindow;

	// 定位相关
	LocationClient mLocClient;
	private LocationMode mCurrentMode = LocationMode.NORMAL;
	BitmapDescriptor mCurrentMarker;

	GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用

	private long mExitTime = 0;
	List<XbVehicle> vehicle = new ArrayList<XbVehicle>();
	// private BadgeView badge;
	// 初始化全局 bitmap 信息，不用时及时 recycle
	BitmapDescriptor online = BitmapDescriptorFactory
			.fromResource(R.drawable.xtd_carlogo_on);
	BitmapDescriptor offline = BitmapDescriptorFactory
			.fromResource(R.drawable.xtd_carlogo_off);
	private FancyButton main_refresh;
	int flag = 0;
	private View mMarkerLy;
	private int handler_runnable_time;
	private FancyButton main_traffic;
	private TextView countDown;
	private String mCurrentCar = null;

	// 覆盖物相关
	private LatLng latLng = null;
	private OverlayOptions overlayOptions = null;
	private Marker marker = null;
	private double[] doubleLng;
	private BitmapDescriptor car;
	private List<Marker> markerList = new ArrayList<Marker>();
	private boolean isFirstLoad = true;
	private int state = 0;
	// 是否打开交通图
	private boolean isTraffic = false;
	// 是否首次定位
	boolean isFirstLoc = true;
	// 倒计时控件
	private CountdownView show_countdown;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initWithApiKey();
		init();
	}

	private void init() {
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.getUiSettings().setCompassEnabled(true);
		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(new MyGetGeoCoderResultListener());
		// 隐藏百度logo和 ZoomControl
		int count = mMapView.getChildCount();
		for (int i = 0; i < count; i++) {
			View child = mMapView.getChildAt(i);
			if (child instanceof ImageView || child instanceof ZoomControls) {
				child.setVisibility(View.INVISIBLE);
			}
		}
		//
		mBaiduMap.getUiSettings().setCompassEnabled(true);
		// 对Marker的点击弹出PopWindows
		LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
		mMarkerLy = inflater.inflate(R.layout.popwindows_show, null);
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(final Marker marker) {
				// 获得marker中的数据
				final XbVehicle xbVehicle = JSON.parseObject(marker
						.getExtraInfo().getString("info"), XbVehicle.class);
				mCurrentCar = xbVehicle.getVid();
				mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(marker
						.getPosition()));
				mInfoWindow = new InfoWindow(popupInfo(mMarkerLy, xbVehicle),
						marker.getPosition(), Common.INFOWINDOW_POSITION);
				mBaiduMap.showInfoWindow(mInfoWindow);
				return true;
			}
		});
		// 点击地图事件
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				closeInfoWindow();
			}
		});
		show_countdown = (CountdownView) findViewById(R.id.show_countdown);
		show_countdown.setOnCountdownEndListener(new OnCountdownEndListener() {

			@Override
			public void onEnd(CountdownView cv) {
				// TODO Auto-generated method stub
				startCountdown();
			}
		});

		// 底部四个按钮
		PengButton main_vehicles = (PengButton) findViewById(R.id.main_vehicles);
		PengButton main_police = (PengButton) findViewById(R.id.main_police);
		PengButton main_routes = (PengButton) findViewById(R.id.main_routes);
		PengButton main_myself = (PengButton) findViewById(R.id.main_myself);
		OnClickListener bottomClick = new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Class cls = null;
				switch (arg0.getId()) {
				case R.id.main_vehicles:
					cls = VehiclesActivity.class;
					break;
				case R.id.main_police:
					cls = PolicesActivity.class;
					break;
				case R.id.main_routes:
					cls = RoutesActivity.class;
					break;
				case R.id.main_myself:
					cls = MyselfActivity.class;
					break;
				}
				Intent intent = new Intent(MainActivity.this, cls);
				startActivityForResult(intent, 0);
			}
		};
		main_vehicles.setOnClickListener(bottomClick);
		main_police.setOnClickListener(bottomClick);
		main_routes.setOnClickListener(bottomClick);
		main_myself.setOnClickListener(bottomClick);
		// 搜索刷新
		SearchFragment searchFragment = (SearchFragment) this
				.getSupportFragmentManager().findFragmentById(R.id.search_bar);
		searchFragment.setHint("请输入要搜索的地区名称,如:沧州");
		searchFragment.setOnSearchClickListener(new OnSearchClickListener() {

			@Override
			public void onSearch(String k) {
				// TODO Auto-generated method stub
				// Geo搜索
				mSearch.geocode(new GeoCodeOption().city(k).address(k));
			}

			@Override
			public void onSearchClose() {
				// TODO Auto-generated method stub

			}
		});
		// 手动刷新
		main_refresh = (FancyButton) findViewById(R.id.main_refresh);
		main_refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getVehicleLocation(true);
			}
		});

		// 路况
		main_traffic = (FancyButton) findViewById(R.id.main_traffic);
		main_traffic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				isTraffic = !isTraffic;
				mBaiduMap.setTrafficEnabled(isTraffic);
				if (isTraffic) {
					main_traffic
							.setIconResource(R.drawable.xtd_icon_traffic_pr);
					Utils.ToastMessage(MainActivity.this, "实时路况已开启");
				} else {
					main_traffic.setIconResource(R.drawable.xtd_icon_traffic);
					Utils.ToastMessage(MainActivity.this, "实时路况已关闭");
				}
			}
		});
		// 设置比例尺位置与缩小放大的按钮,指南针位置
		ZoomControlView mZoomControlView = (ZoomControlView) findViewById(R.id.ZoomControlView);
		mZoomControlView.setMapView(mMapView);
		mBaiduMap.setOnMapLoadedCallback(new OnMapLoadedCallback() {
			@Override
			public void onMapLoaded() {
				// TODO Auto-generated method stub
				Point scalePoint = new Point(main_refresh.getRight() + 20,
						main_refresh.getBottom() - 50);
				mMapView.setScaleControlPosition(scalePoint);
				Point compassPoint = new Point(
						(main_refresh.getLeft() + main_refresh.getRight()) / 2,
						(main_traffic.getBottom() + main_traffic.getTop()) / 2);
				mBaiduMap.setCompassPosition(compassPoint);
				startLocation();
			}
		});

		// 加载数据
		getVehicleLocation(false);

	}

	/**
	 * 初始化图层
	 */
	public void addInfosOverlay() {
		int size = vehicle.size();
		int markerPosition = -1;
		for (int i = 0; i < size; i++) {
			XbVehicle info = vehicle.get(i);
			doubleLng = Utils.getLng(info.getLocation());
			// 位置
			latLng = new LatLng(doubleLng[1], doubleLng[0]);
			if (info.isOnline()) {
				car = online;
			} else {
				car = offline;
			}

			Bundle bundle = new Bundle();
			bundle.putString("info", JSON.toJSONString(info));
			bundle.putString("id", info.getVid());
			if (isFirstLoad) {
				overlayOptions = new MarkerOptions().position(latLng).icon(car)
						.rotate(360 - info.getDirection());
				marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));

				marker.setExtraInfo(bundle);
				markerList.add(marker);
			} else {
				markerPosition = loadMarkerPosition(info.getVid());
				if (markerPosition >= 0) {
					markerList.get(markerPosition).setIcon(car);
					markerList.get(markerPosition).setPosition(latLng);
					markerList.get(markerPosition).setRotate(
							360 - info.getDirection());
					markerList.get(markerPosition).setExtraInfo(bundle);
				}
			}
		}
		if (!StringUtils.isEmpty(mCurrentCar)) {
			showInfowindow();
		}

	}

	private int loadMarkerPosition(String vid) {
		int size = markerList.size();
		for (int i = 0; i < size; i++) {
			if (markerList.get(i).getExtraInfo().getString("id").equals(vid)) {
				LogUtils.DebugLog("获取到marklist中的marker" + i);
				return i;
			}
		}
		return -1;
	}

	// 加载首页车辆信息
	private void getVehicleLocation(final boolean isRefresh) {
		JSONObject postData = new JSONObject();
		StringEntity entity = null;
		try {
			postData.put("state", state);
			entity = new StringEntity(postData.toString(), "UTF-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LogUtils.DebugLog("post json", postData.toString());
		String url = UrlConfig.getVehicleLocations();
		HttpUtil.post(MainActivity.this, url, entity, "application/json",
				new jsonHttpResponseHandler() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						if (isRefresh) {
							showProgressDialog(MainActivity.this, "正在加载车辆信息");
							closeInfoWindow();
						}
						super.onStart();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONArray response) {
						// TODO Auto-generated method stub
						vehicle = JSON.parseArray(response.toString(),
								XbVehicle.class);
						LogUtils.DebugLog("result json", response.toString());
						addInfosOverlay();
						if (isFirstLoad && vehicle.size() > 0) {
							state = 1;
							isFirstLoad = !isFirstLoad;
						}
						super.onSuccess(statusCode, headers, response);
					}
				});
	}

	/**
	 * 根据info为布局上的控件设置信息
	 * 
	 * @param mMarkerInfo2
	 * @param info
	 */
	private View popupInfo(View mMarkerLy, final XbVehicle xbVehicle) {
		ViewHolder viewHolder = null;
		if (mMarkerLy.getTag() == null) {
			viewHolder = new ViewHolder();
			viewHolder.popwindows_track = (Button) mMarkerLy
					.findViewById(R.id.popwindows_track);
			viewHolder.popwindows_monitor = (Button) mMarkerLy
					.findViewById(R.id.popwindows_monitor);
			viewHolder.popwindows_weather = (Button) mMarkerLy
					.findViewById(R.id.popwindows_weather);
			viewHolder.popwindows_name = (TextView) mMarkerLy
					.findViewById(R.id.popwindows_name);
			viewHolder.popwindows_state = (TextView) mMarkerLy
					.findViewById(R.id.popwindows_state);
			viewHolder.popwindows_time = (TextView) mMarkerLy
					.findViewById(R.id.popwindows_time);
			viewHolder.popwindows_close = (ImageView) mMarkerLy
					.findViewById(R.id.popwindows_close);
			viewHolder.popwindows_position = (TextView) mMarkerLy
					.findViewById(R.id.popwindows_position);
			viewHolder.popwindows_details = (RelativeLayout) mMarkerLy
					.findViewById(R.id.popwindows_details);

			mMarkerLy.setTag(viewHolder);
		}
		viewHolder = (ViewHolder) mMarkerLy.getTag();
		viewHolder.popwindows_time.setText("时间:" + xbVehicle.getTime());
		if (xbVehicle.isOnline()) {
			if (xbVehicle.getSpeed() > 1) {
				viewHolder.popwindows_state.setText("行驶中 "
						+ xbVehicle.getSpeed() + "km/h");
			} else {
				viewHolder.popwindows_state.setText("停车中  "
						+ xbVehicle.getSpeed() + "km/h");
			}

			viewHolder.popwindows_state.setTextColor(getResources().getColor(
					R.color.app_green));
		} else {
			viewHolder.popwindows_state.setText("离线");
			viewHolder.popwindows_state.setTextColor(getResources().getColor(
					R.color.text));
		}
		viewHolder.popwindows_name.setText(xbVehicle.getPlateNo());
		viewHolder.popwindows_position.setText("位置:" + xbVehicle.getAddr());

		OnClickListener click = new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				switch (arg0.getId()) {
				case R.id.popwindows_track:
					flag = 0;
					break;
				case R.id.popwindows_monitor:
					flag = 1;
					break;
				case R.id.popwindows_details:
					flag = 2;
					break;
				case R.id.popwindows_weather:
					flag = 3;
					break;
				}
				Intent intent = new Intent(MainActivity.this,
						CarDetailsActivity.class);
				intent.putExtra("car", JSON.toJSONString(xbVehicle));
				intent.putExtra("flag", flag);
				startActivity(intent);
			}
		};
		viewHolder.popwindows_weather.setOnClickListener(click);
		viewHolder.popwindows_monitor.setOnClickListener(click);
		viewHolder.popwindows_track.setOnClickListener(click);
		viewHolder.popwindows_details.setOnClickListener(click);
		viewHolder.popwindows_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				closeInfoWindow();
			}
		});
		return mMarkerLy;
	}

	/**
	 * 复用弹出面板mMarkerLy的控件
	 * 
	 */
	private class ViewHolder {
		Button popwindows_track;
		Button popwindows_monitor;
		Button popwindows_weather;
		TextView popwindows_name;
		TextView popwindows_state;
		TextView popwindows_time;
		TextView popwindows_position;
		ImageView popwindows_close;
		RelativeLayout popwindows_details;
	}

	/**
	 * 关闭InfoWindow
	 */
	private void closeInfoWindow() {
		mCurrentCar = null;
		// 隐藏InfoWindow
		mBaiduMap.hideInfoWindow();
	}

	/**
	 * 定时任务,刷新首页
	 */
	Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			getVehicleLocation(false);
			if (handler_runnable_time != 0) {
				handler.postDelayed(this, handler_runnable_time);
			}
		}
	};

	@Override
	protected void onPause() {
		// MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		mMapView.onPause();
		handler.removeCallbacks(runnable);
		super.onPause();
	}

	@Override
	protected void onResume() {
		// MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		handler_runnable_time = PreferenceUtils.getInstance(MainActivity.this)
				.getMonitorTime() * 1000;
		LogUtils.DebugLog("main time" + handler_runnable_time);
		handler.postDelayed(runnable, handler_runnable_time);
		startCountdown();
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		mMapView.onDestroy();
		super.onDestroy();
		// 回收 bitmap 资源
		online.recycle();
		offline.recycle();
		if (mLocClient != null) {
			// 退出时销毁定位
			mLocClient.stop();
			// 关闭定位图层
			mBaiduMap.setMyLocationEnabled(false);
		}
		if (mSearch != null) {
			mSearch.destroy();
		}

	}

	// 点击车辆列表后,首页显示该车
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		if (arg1 == RESULT_OK) {
			mCurrentCar = arg2.getStringExtra("vid");
			showInfowindow();
		}
		super.onActivityResult(arg0, arg1, arg2);
	}

	private void showInfowindow() {
		int markerPosition = loadMarkerPosition(mCurrentCar);
		XbVehicle xbVehicle = JSON.parseObject(markerList.get(markerPosition)
				.getExtraInfo().getString("info"), XbVehicle.class);
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(markerList.get(
				markerPosition).getPosition()));
		mInfoWindow = new InfoWindow(popupInfo(mMarkerLy, xbVehicle),
				markerList.get(markerPosition).getPosition(),
				Common.INFOWINDOW_POSITION);
		mBaiduMap.showInfoWindow(mInfoWindow);
	}

	// 启动定位
	private void startLocation() {
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true, mCurrentMarker));
		// 定位初始化
		mLocClient = new LocationClient(getApplicationContext());
		mLocClient.registerLocationListener(new MyLocationListenner());
		LocationClientOption option = new LocationClientOption();
		option.setNeedDeviceDirect(true);
		option.setOpenGps(false);// 打开GPS
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(60 * 1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	// 启动Push
	private void initWithApiKey() {
		// Push: 无账号初始化，用API key绑定
		if (PreferenceUtils.getInstance(MainActivity.this).getUserId()
				.equals("59")) {
			LogUtils.DebugLog("体验用户不启动push");
			return;
		}
		PushManager.startWork(getApplicationContext(),
				PushConstants.LOGIN_TYPE_API_KEY,
				PushUtils.getMetaValue(MainActivity.this, "api_key"));
	}

	/**
	 * GEO搜索监听函数
	 */
	public class MyGetGeoCoderResultListener implements
			OnGetGeoCoderResultListener {

		@Override
		public void onGetGeoCodeResult(GeoCodeResult result) {
			// TODO Auto-generated method stub
			LogUtils.DebugLog("Geo搜索返回值" + result.error);
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				Utils.ToastMessage(MainActivity.this, "抱歉，未能找到结果");
				return;
			}
			mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
					.getLocation()));
		}

		@Override
		public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
			// TODO Auto-generated method stub
		}

	}

	private void startCountdown() {
		if (handler_runnable_time != 0) {
			show_countdown.start(handler_runnable_time);
		} else {
			show_countdown.setVisibility(View.GONE);
		}
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
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll,
						14.0f);
				mBaiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	// 双击退出
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Utils.showSuperCardToast(MainActivity.this, getResources()
						.getString(R.string.exit));
				mExitTime = System.currentTimeMillis();
			} else {
				BaiduNaviManager.getInstance().uninit();
				XtdApplication.getInstance().exit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}