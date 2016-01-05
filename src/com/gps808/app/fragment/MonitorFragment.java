package com.gps808.app.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ZoomControls;

import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.MarkerOptions.MarkerAnimateType;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.gps808.app.R;
import com.gps808.app.bean.XbTrack;
import com.gps808.app.bean.XbVehicle;
import com.gps808.app.dialog.DateDialog;
import com.gps808.app.dialog.DateDialog.OnTimeClickListener;
import com.gps808.app.utils.BaseFragment;
import com.gps808.app.utils.Common;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;

/**
 * 轨迹回放
 * 
 * @author JIA
 * 
 */
public class MonitorFragment extends BaseFragment {

	BitmapDescriptor endIcon = BitmapDescriptorFactory
			.fromResource(R.drawable.map_end_icon);
	BitmapDescriptor startIcon = BitmapDescriptorFactory
			.fromResource(R.drawable.xtd_map_start);
	BitmapDescriptor locationIcon = BitmapDescriptorFactory
			.fromResource(R.drawable.xtd_carlogo_on);
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	Polyline mPolyline;
	private InfoWindow mInfoWindow;
	private String vid;
	private ToggleButton play_toogle;
	private TextView play_text;
	private ProgressBar play_progress;
	private LinearLayout play_layout;
	private Marker marker = null;
	private LatLng latLng = null;
	private double[] doubleLng;
	private List<XbVehicle> xbVehicles;

	public static MonitorFragment newInstance(String id) {
		MonitorFragment fragment = new MonitorFragment();
		fragment.vid = id;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		SDKInitializer.initialize(getActivity().getApplicationContext());
		View rootView = inflater.inflate(R.layout.fragment_monitor, null);
		init(rootView);
		return rootView;
	}

	private void init(View root) {
		// TODO Auto-generated method stub

		mMapView = (MapView) root.findViewById(R.id.bmapView);
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
		play_toogle = (ToggleButton) root.findViewById(R.id.play_toogle);
		play_text = (TextView) root.findViewById(R.id.play_text);
		play_progress = (ProgressBar) root.findViewById(R.id.play_progress);
		play_layout = (LinearLayout) root.findViewById(R.id.play_layout);
		DateDialog dateDialog = new DateDialog(getActivity());
		dateDialog.setOnTimeClickListener(new OnTimeClickListener() {

			@Override
			public void onTimeOk(String start, String end) {
				// TODO Auto-generated method stub
				getData(start, end);
			}
		});
		dateDialog.show();
		play_toogle.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub

				if (arg1) {
					handler.post(runnable);
					Utils.ToastMessage(getActivity(), "开始回放");
				} else {
					handler.removeCallbacks(runnable);
					Utils.ToastMessage(getActivity(), "暂停回放");
				}
			}
		});

	}

	// 获取服务器数据
	private void getData(String start, String end) {
		String url = UrlConfig.getVehicleGPSHistory();
		String testParams = "{\"vId\":\"1010001\",\"start\":\"2015-12-22 08:00:00\",\"end\":\"2015-12-23 20:59:34\"}";

		JSONObject params = new JSONObject();
		StringEntity entity = null;
		try {
			params.put("vId", vid);
			params.put("start", start);
			params.put("end", end);
			entity = new StringEntity(params.toString(), "UTF-8");
			// entity = new StringEntity(testParams, "UTF-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LogUtils.DebugLog("post json", params.toString());
		HttpUtil.post(getActivity(), url, entity, "application/json",
				new jsonHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONArray response) {
						// TODO Auto-generated method stub
						xbVehicles = JSON.parseArray(response.toString(),
								XbVehicle.class);
						LogUtils.DebugLog("result json", response.toString());
						if (xbVehicles.size() > 0) {
							play_progress.setMax(xbVehicles.size());
							parseData();
							play_layout.setVisibility(View.VISIBLE);
						} else {
							play_layout.setVisibility(View.GONE);
							Utils.ToastMessage(getActivity(), "对不起,暂无数据");
						}
						super.onSuccess(statusCode, headers, response);
					}
				});
	}

	// 解析数据,画出轨迹
	private void parseData() {
		List<LatLng> points = new ArrayList<LatLng>();

		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		for (XbVehicle info : xbVehicles) {
			doubleLng = Utils.getLng(info.getLocation());
			// 位置
			latLng = new LatLng(doubleLng[1], doubleLng[0]);
			points.add(latLng);
			builder.include(latLng);
		}
		mBaiduMap.clear();
		// 添加普通折线绘制
		if (points.size() >= 2) {

			OverlayOptions ooPolyline = new PolylineOptions().width(10)
					.color(getResources().getColor(R.color.app_green))
					.points(points);
			mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
			mBaiduMap.addOverlay(new MarkerOptions().position(points.get(0))
					.icon(startIcon));
			mBaiduMap.addOverlay(new MarkerOptions().position(
					points.get(points.size() - 1)).icon(endIcon));

		}
		// 缩放地图，使所有Overlay都在合适的视野内
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngBounds(builder
				.build()));
	}

	// 设置汽车的位置
	private void playMonitor(XbVehicle car) {
		doubleLng = Utils.getLng(car.getLocation());
		// 位置
		latLng = new LatLng(doubleLng[1], doubleLng[0]);
		if (marker == null) {
			// 图标
			OverlayOptions overlayOptions = new MarkerOptions()
					.position(latLng).icon(locationIcon).zIndex(5)
					.rotate(car.getDirection());
			marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));

		} else {
			marker.setPosition(latLng);
			marker.setRotate(car.getDirection());
		}
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(latLng));

	}

	/**
	 * 定时任务
	 */
	int i = 0;
	Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			// 要做的事情
			playMonitor(xbVehicles.get(i));

			play_text.setText("位置:" + xbVehicles.get(i).getAddr() + "时间:"
					+ xbVehicles.get(i).getTime() + "速度:"
					+ xbVehicles.get(i).getSpeed());
			i++;
			play_progress.setProgress(i);
			if (i < xbVehicles.size()) {
				handler.postDelayed(this, 500);
			} else {
				play_toogle.setChecked(false);
				play_toogle.setEnabled(false);
			}
		}
	};

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		mMapView.onPause();
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		mMapView.onResume();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		handler.removeCallbacks(runnable);
		mMapView.onDestroy();
		endIcon.recycle();
		startIcon.recycle();
		locationIcon.recycle();
		super.onDestroy();
	}

}
