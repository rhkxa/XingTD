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
import com.baidu.mapapi.map.MyLocationData;
import com.gps808.app.R;
import com.gps808.app.bean.XbVehicle;
import com.gps808.app.fragment.SearchFragment;
import com.gps808.app.fragment.SearchFragment.OnSearchClickListener;
import com.gps808.app.map.ZoomControlView;
import com.gps808.app.push.PushUtils;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.PreferenceUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;
import com.gps808.app.utils.XtdApplication;
import com.gps808.app.view.FancyButton;
import com.gps808.app.view.PengButton;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ZoomControls;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;

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
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;
	boolean isFirstLoc = true;// 是否首次定位

	private long mExitTime = 0;
	List<XbVehicle> vehicle = new ArrayList<XbVehicle>();
	// private BadgeView badge;
	// 初始化全局 bitmap 信息，不用时及时 recycle
	BitmapDescriptor online = BitmapDescriptorFactory
			.fromResource(R.drawable.xtd_carlogo_on);
	BitmapDescriptor offline = BitmapDescriptorFactory
			.fromResource(R.drawable.xtd_carlogo_off);
	private String key = "";
	private FancyButton main_refresh;
	int flag = 0;
	private View mMarkerLy;
	private int handler_runnable_time;
	private FancyButton main_person;

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
		MapStatus mapStatus = new MapStatus.Builder()
				.target(new LatLng(38.31056, 116.844697)).zoom(10.0f).build();
		MapStatusUpdate msu = MapStatusUpdateFactory.newMapStatus(mapStatus);
		mBaiduMap.setMapStatus(msu);
		// 隐藏百度logo和 ZoomControl
		int count = mMapView.getChildCount();
		for (int i = 0; i < count; i++) {
			View child = mMapView.getChildAt(i);
			if (child instanceof ImageView || child instanceof ZoomControls) {
				child.setVisibility(View.INVISIBLE);
			}
		}
		// 加载数据
		getVehicleLocation(false);
		// 对Marker的点击弹出PopWindows
		LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
		mMarkerLy = inflater.inflate(R.layout.popwindows_show, null);
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(final Marker marker) {
				// 获得marker中的数据
				final XbVehicle xbVehicle = JSON.parseObject(marker
						.getExtraInfo().getString("info"), XbVehicle.class);
				OnInfoWindowClickListener onInfoWindowClickListener = new OnInfoWindowClickListener() {

					@Override
					public void onInfoWindowClick() {
						// TODO Auto-generated method stub

					}
				};

				mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(marker
						.getPosition()));
				mInfoWindow = new InfoWindow(popupInfo(mMarkerLy, xbVehicle),
						marker.getPosition(), -100);
				// mInfoWindow = new InfoWindow(BitmapDescriptorFactory
				// .fromView(popupInfo(mMarkerLy, xbVehicle)), marker
				// .getPosition(), -100, onInfoWindowClickListener);
				// 显示InfoWindow
			
				mBaiduMap.showInfoWindow(mInfoWindow);
				return true;
			}
		});
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				mBaiduMap.hideInfoWindow();
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
		SearchFragment searchFragment = (SearchFragment) this
				.getSupportFragmentManager().findFragmentById(R.id.search_bar);
		searchFragment.setOnSearchClickListener(new OnSearchClickListener() {

			@Override
			public void onSearch(String k) {
				// TODO Auto-generated method stub
				key = k;
				getVehicleLocation(true);
			}
		});
		main_refresh = (FancyButton) findViewById(R.id.main_refresh);
		main_refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getVehicleLocation(true);
			}
		});
		// 设置比例尺位置与缩小放大的按钮
		ZoomControlView mZoomControlView = (ZoomControlView) findViewById(R.id.ZoomControlView);
		mZoomControlView.setMapView(mMapView);
		mBaiduMap.setOnMapLoadedCallback(new OnMapLoadedCallback() {

			@Override
			public void onMapLoaded() {
				// TODO Auto-generated method stub
				int x = main_refresh.getRight() + 20;
				int y = main_refresh.getBottom() - 50;
				Point point = new Point(x, y);
				mMapView.setScaleControlPosition(point);
			}
		});
		// 用户位置
		main_person = (FancyButton) findViewById(R.id.main_person);
		main_person.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 开启定位图层
				mBaiduMap.setMyLocationEnabled(true);
				// 定位初始化
				mLocClient = new LocationClient(MainActivity.this);
				mLocClient.registerLocationListener(myListener);
				LocationClientOption option = new LocationClientOption();
				option.setOpenGps(true);// 打开gps
				option.setCoorType("bd09ll"); // 设置坐标类型
				option.setScanSpan(1000);
				mLocClient.setLocOption(option);
				mLocClient.start();
			}
		});
	}

	/**
	 * 初始化图层
	 */
	public void addInfosOverlay(List<XbVehicle> infos) {
		mBaiduMap.clear();
		LatLng latLng = null;
		OverlayOptions overlayOptions = null;
		Marker marker = null;
		double[] doubleLng;
		BitmapDescriptor car;
		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		for (XbVehicle info : infos) {
			doubleLng = Utils.getLng(info.getLocation());
			// 位置
			latLng = new LatLng(doubleLng[1], doubleLng[0]);
			builder.include(latLng);
			if (info.isOnline()) {
				car = online;
			} else {
				car = offline;
			}
			// 图标
			overlayOptions = new MarkerOptions().position(latLng).icon(car)
					.zIndex(5).rotate(info.getDirection());
			// .animateType(MarkerAnimateType.drop);下降动画

			marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
			Bundle bundle = new Bundle();
			bundle.putString("info", JSON.toJSONString(info));
			marker.setExtraInfo(bundle);

		}
		// 缩放地图，使所有Overlay都在合适的视野内
		// mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngBounds(builder
		// .build()));

	}

	/**
	 * 清除所有Overlay
	 * 
	 * @param view
	 */
	public void clearOverlay(View view) {
		mBaiduMap.clear();
	}

	/**
	 * 重新添加Overlay
	 * 
	 * @param view
	 */
	public void resetOverlay(View view) {
		clearOverlay(null);

	}

	private void getVehicleLocation(boolean isRefresh) {
		if (isRefresh) {
			showProgressDialog(MainActivity.this, "正在加载车辆信息");
		}
		JSONObject postData = new JSONObject();
		StringEntity entity = null;
		try {
			postData.put("search", key);
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
					public void onSuccess(int statusCode, Header[] headers,
							JSONArray response) {
						// TODO Auto-generated method stub
						vehicle = JSON.parseArray(response.toString(),
								XbVehicle.class);
						LogUtils.DebugLog("result json", response.toString());
						addInfosOverlay(vehicle);
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
			viewHolder.popwindows_state.setText("在线" + xbVehicle.getSpeed());
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
				mBaiduMap.hideInfoWindow();
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
	 * 定时任务
	 */

	Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			// 要做的事情
			getVehicleLocation(false);
			handler.postDelayed(this, handler_runnable_time);
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
		handler.postDelayed(runnable, handler_runnable_time);
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

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		if (arg1 == RESULT_OK) {
			String vid = arg2.getStringExtra("vid");
			LogUtils.DebugLog("activity resutl", vid);
			for (XbVehicle info : vehicle) {
				if (info.getVid().equals(vid)) {
					double[] doubleLng = Utils.getLng(info.getLocation());
					LatLng latLng = new LatLng(doubleLng[1], doubleLng[0]);
					mInfoWindow = new InfoWindow(popupInfo(mMarkerLy, info),
							latLng, -100);
					mBaiduMap.showInfoWindow(mInfoWindow);
					MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(
							latLng, 14.0f);
					mBaiduMap.setMapStatus(msu);
				}
			}
		}
		super.onActivityResult(arg0, arg1, arg2);
	}

	// 以apikey的方式绑定
	private void initWithApiKey() {
		// Push: 无账号初始化，用api key绑定
		PushManager.startWork(getApplicationContext(),
				PushConstants.LOGIN_TYPE_API_KEY,
				PushUtils.getMetaValue(MainActivity.this, "api_key"));
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
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
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
				XtdApplication.getInstance().exit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}