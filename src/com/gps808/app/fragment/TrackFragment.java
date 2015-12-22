package com.gps808.app.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
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
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;

import com.gps808.app.R;
import com.gps808.app.bean.XbTrack;
import com.gps808.app.bean.XbVehicle;
import com.gps808.app.utils.BaseFragment;
import com.gps808.app.utils.Common;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;

/**
 * 跟踪
 * 
 * @author JIA
 * 
 */
public class TrackFragment extends BaseFragment {

	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private InfoWindow mInfoWindow;
	private String vid;
	BitmapDescriptor locationIcon = null;

	public static TrackFragment newInstance(String id) {
		TrackFragment fragment = new TrackFragment();
		fragment.vid = id;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		SDKInitializer.initialize(getActivity().getApplicationContext());
		View rootView = inflater.inflate(R.layout.fragment_track, null);
		init(rootView);
		return rootView;
	}

	private void init(View root) {
		// TODO Auto-generated method stub
		locationIcon = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_gcoding);
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
		String url = UrlConfig.getLocationById(vid);
		HttpUtil.get(getActivity(), url, new jsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				XbTrack xbTrack = JSON.parseObject(response.toString(),
						XbTrack.class);
				LogUtils.DebugLog("result json" + response.toString());
				addInfosOverlay(xbTrack);
				super.onSuccess(statusCode, headers, response);
			}
		});
	}

	/**
	 * 初始化图层
	 */
	private void addInfosOverlay(XbTrack xbTrack) {
		mBaiduMap.clear();
		LatLng latLng = null;
		OverlayOptions overlayOptions = null;
		Marker marker = null;
		double[] doubleLng;
		doubleLng = Utils.getLng(xbTrack.getLocation(), ":");
		// 位置
		latLng = new LatLng(doubleLng[1], doubleLng[0]);
		// 图标
		overlayOptions = new MarkerOptions().position(latLng)
				.icon(locationIcon).zIndex(5).rotate(xbTrack.getDirection());
		// .animateType(MarkerAnimateType.drop);下降动画
		marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
		// 缩放地图，使所有Overlay都在合适的视野内
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(latLng));

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
		locationIcon = null;
		super.onDestroy();
	}

}
