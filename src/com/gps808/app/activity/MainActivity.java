package com.gps808.app.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.gps808.app.R;
import com.gps808.app.bean.XbVehicle;
import com.gps808.app.dialog.DateDialog;
import com.gps808.app.fragment.SearchFragment;
import com.gps808.app.fragment.SearchFragment.OnSearchClickListener;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.Common;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;
import com.gps808.app.utils.XtdApplication;
import com.gps808.app.view.FancyButton;
import com.gps808.app.view.PengButton;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomControls;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// UpdateManager.getUpdateManager().checkAppUpdate(MainActivity.this,
		// false);

		init();
	}

	private void init() {
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
		// 加载数据
		getVehicleLocation();
		// 对Marker的点击弹出PopWindows
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
				LayoutInflater inflater = LayoutInflater
						.from(MainActivity.this);
				View mMarkerLy = inflater.inflate(R.layout.popwindows_show,
						null);
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
				getVehicleLocation();
			}
		});
		main_refresh = (FancyButton) findViewById(R.id.main_refresh);
		main_refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DateDialog dateDialog = new DateDialog(MainActivity.this);
				dateDialog.show();
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
			doubleLng = Utils.getLng(info.getLocation(), ":");
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
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngBounds(builder
				.build()));

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

	

	private void getVehicleLocation() {
		JSONObject postData = new JSONObject();
		StringEntity entity = null;
		try {
			postData.put("search", key);
			entity = new StringEntity(postData.toString());
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
			viewHolder.popwindows_trail = (Button) mMarkerLy
					.findViewById(R.id.popwindows_trail);
			viewHolder.popwindows_weather = (Button) mMarkerLy
					.findViewById(R.id.popwindows_weather);
			viewHolder.popwindows_name = (TextView) mMarkerLy
					.findViewById(R.id.popwindows_name);
			viewHolder.popwindows_state = (TextView) mMarkerLy
					.findViewById(R.id.popwindows_state);
			viewHolder.popwindows_time = (TextView) mMarkerLy
					.findViewById(R.id.popwindows_time);

			mMarkerLy.setTag(viewHolder);
		}
		viewHolder = (ViewHolder) mMarkerLy.getTag();
		viewHolder.popwindows_time.setText("时间:" + xbVehicle.getTime());
		if (xbVehicle.isOnline()) {
			viewHolder.popwindows_state.setText("在线:" + xbVehicle.getSpeed());
		} else {
			viewHolder.popwindows_state.setText("离线:");
		}
		viewHolder.popwindows_name.setText(xbVehicle.getPlateNo());
		OnClickListener click = new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				switch (arg0.getId()) {
				case R.id.popwindows_track:
					flag = 0;
					break;
				case R.id.popwindows_weather:
					flag = 3;
					break;
				case R.id.popwindows_trail:
					flag = 1;
					break;
				}
				Intent intent = new Intent(MainActivity.this,
						CarDetailsActivity.class);
				intent.putExtra("vid", xbVehicle.getVid());
				intent.putExtra("flag", flag);
				startActivity(intent);
			}
		};
		viewHolder.popwindows_weather.setOnClickListener(click);
		viewHolder.popwindows_trail.setOnClickListener(click);
		viewHolder.popwindows_track.setOnClickListener(click);
		return mMarkerLy;
	}

	/**
	 * 复用弹出面板mMarkerLy的控件
	 * 
	 */
	private class ViewHolder {
		Button popwindows_track;
		Button popwindows_trail;
		Button popwindows_weather;
		TextView popwindows_name;
		TextView popwindows_state;
		TextView popwindows_time;
	}

	/**
	 * 定时任务
	 */
	// handler.postDelayed(runnable,
	// Common.HANDLER_RUNNABLE_TIME);//每两秒执行一次runnable.
	Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			// 要做的事情
			getVehicleLocation();
			handler.postDelayed(this, Common.HANDLER_RUNNABLE_TIME);
		}
	};

	@Override
	protected void onPause() {
		// MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		mMapView.onPause();
		super.onPause();
	}
	@Override
	protected void onResume() {
		// MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
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
		handler.removeCallbacks(runnable);

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