package com.gps808.app.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
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
import com.baidu.mapapi.map.MarkerOptions.MarkerAnimateType;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;

import com.gps808.app.R;
import com.gps808.app.bean.XbTrack;
import com.gps808.app.utils.BaseFragment;
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
	BitmapDescriptor startIcon = null;

	LatLng latLng = null;
	OverlayOptions overlayOptions = null;
	Marker marker = null;
	double[] doubleLng;
	List<LatLng> points = new ArrayList<LatLng>();
	Polyline mPolyline;

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
				.fromResource(R.drawable.xtd_carlogo_on);
		startIcon = BitmapDescriptorFactory
				.fromResource(R.drawable.map_start_icon);
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
		doubleLng = Utils.getLng(xbTrack.getLocation(), ":");
		// 位置
		latLng = new LatLng(doubleLng[1], doubleLng[0]);
		points.add(latLng);
		if (marker == null) {
			// 图标
			overlayOptions = new MarkerOptions().position(latLng)
					.icon(locationIcon).zIndex(5)
					.rotate(xbTrack.getDirection())
					.animateType(MarkerAnimateType.grow);// 生长动画
			marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));

		} else {
			marker.setPosition(latLng);
			marker.setRotate(xbTrack.getDirection());
			if (mPolyline == null) {
				OverlayOptions ooPolyline = new PolylineOptions().width(10)
						.color(getResources().getColor(R.color.app_green)).points(points);
				mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
				mBaiduMap.addOverlay(new MarkerOptions()
						.position(points.get(0)).icon(startIcon));
			} else {
				mPolyline.setPoints(points);
			}
		}
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(latLng));

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
			getData();
			handler.postDelayed(this, 6000);
		}
	};

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		mMapView.onPause();
		handler.removeCallbacks(runnable);
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		mMapView.onResume();
		handler.postDelayed(runnable, 6000);// 每两秒执行一次runnable.
		super.onResume();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mMapView.onDestroy();
		locationIcon = null;
		startIcon = null;
		super.onDestroy();
	}

}
