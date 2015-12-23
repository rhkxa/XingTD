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
import android.widget.ImageView;
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
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.gps808.app.R;
import com.gps808.app.activity.DisplayLineActivity;
import com.gps808.app.bean.XbDisplayLine;
import com.gps808.app.bean.XbVehicle;
import com.gps808.app.utils.BaseFragment;
import com.gps808.app.utils.Common;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;
import com.gps808.app.utils.BaseActivity.jsonHttpResponseHandler;

/**
 * 轨迹回放
 * 
 * @author JIA
 * 
 */
public class MonitorFragment extends BaseFragment {

	BitmapDescriptor endIcon = null;
	BitmapDescriptor startIcon = null;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	Polyline mPolyline;
	private InfoWindow mInfoWindow;
	private String vid;

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
		startIcon = BitmapDescriptorFactory
				.fromResource(R.drawable.xtd_map_start);
		endIcon = BitmapDescriptorFactory.fromResource(R.drawable.xtd_map_end);
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
		getData();
	}

	private void getData() {
		String url = UrlConfig.getVehicleGPSHistory();

		JSONObject params = new JSONObject();
		StringEntity entity = null;
		try {
			params.put("vId", vid);
			params.put("start", "2015-12-22 08:00:00");
			params.put("end", "2015-12-23 20:59:34");
			entity = new StringEntity(params.toString(), "UTF-8");
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
						List<XbVehicle> xbVehicles = JSON.parseArray(
								response.toString(), XbVehicle.class);
						LogUtils.DebugLog("result json", response.toString());
						parseData(xbVehicles);
						super.onSuccess(statusCode, headers, response);
					}
				});
	}

	private void parseData(List<XbVehicle> infos) {
		List<LatLng> points = new ArrayList<LatLng>();
		LatLng latLng = null;
		double[] doubleLng;
		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		for (XbVehicle info : infos) {
			doubleLng = Utils.getLng(info.getLocation(), ":");
			// 位置
			latLng = new LatLng(doubleLng[1], doubleLng[0]);
			points.add(latLng);
			builder.include(latLng);
		}
		mBaiduMap.clear();
		// 添加普通折线绘制
		if (points.size() >= 2) {

			OverlayOptions ooPolyline = new PolylineOptions().width(10)
					.color(getResources().getColor(R.color.app_green)).points(points);
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

			handler.postDelayed(this, Common.HANDLER_RUNNABLE_TIME);
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

		mMapView.onDestroy();
		endIcon = null;
		startIcon = null;
		super.onDestroy();
	}

}
