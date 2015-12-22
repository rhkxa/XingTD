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
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
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
public class TrailFragment extends BaseFragment {

	private MapView mMapView;
	private BaiduMap mBaiduMap;
	Polyline mPolyline;
	private InfoWindow mInfoWindow;
	private String vid;

	public static TrailFragment newInstance(String id) {
		TrailFragment fragment = new TrailFragment();
		fragment.vid = id;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		SDKInitializer.initialize(getActivity().getApplicationContext());
		View rootView = inflater.inflate(R.layout.fragment_trail, null);
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
		getData();
	}

	private void getData() {
		String url = UrlConfig.getVehicleGPSHistory();

		JSONObject params = new JSONObject();
		StringEntity entity = null;
		try {
			params.put("vId", vid);
			params.put("start", "2015-10-10 08:59:34");
			params.put("end", "2015-10-11 08:59:34");
			entity = new StringEntity(params.toString());
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

		for (XbVehicle info : infos) {
			doubleLng = Utils.getLng(info.getLocation(), ":");
			// 位置
			latLng = new LatLng(doubleLng[1], doubleLng[0]);
			points.add(latLng);

		}
		mBaiduMap.clear();
		// 添加普通折线绘制
		OverlayOptions ooPolyline = new PolylineOptions().width(10)
				.color(0xAAFF0000).points(points);
		mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
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
		super.onDestroy();
	}

	



}
