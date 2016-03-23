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
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.MarkerOptions.MarkerAnimateType;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.gps808.app.R;
import com.gps808.app.models.XbTrack;
import com.gps808.app.utils.BaseFragment;
import com.gps808.app.utils.Common;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.PreferenceUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;

/**
 * 跟踪
 * 
 * @author JIA
 * 
 */
public class TrackFragment extends BaseFragment {

	private TextureMapView mMapView;
	private BaiduMap mBaiduMap;
	private InfoWindow mInfoWindow;
	private String vid;
	private BitmapDescriptor car;
	BitmapDescriptor online = BitmapDescriptorFactory
			.fromResource(R.drawable.xtd_carlogo_on);
	BitmapDescriptor offline = BitmapDescriptorFactory
			.fromResource(R.drawable.xtd_carlogo_off);
	BitmapDescriptor startIcon = BitmapDescriptorFactory
			.fromResource(R.drawable.map_start_icon);
	LatLng latLng = null;
	OverlayOptions overlayOptions = null;
	Marker marker = null;
	double[] doubleLng;
	List<LatLng> points = new ArrayList<LatLng>();
	Polyline mPolyline;
	private int handler_runnable_time;
	private View mMarkerLy;
	private XbTrack xbTrack;

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

		mMapView = (TextureMapView) root.findViewById(R.id.bmapView);
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
		// 对Marker的点击弹出PopWindows
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		mMarkerLy = inflater.inflate(R.layout.popwindows_trace, null);
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(final Marker marker) {
				// 获得marker中的数据

				mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(marker
						.getPosition()));
				mInfoWindow = new InfoWindow(popupInfo(mMarkerLy, xbTrack),
						marker.getPosition(), Common.INFOWINDOW_POSITION);
				mBaiduMap.showInfoWindow(mInfoWindow);
				return true;
			}
		});
		getData();
	}

	private void getData() {
		String url = UrlConfig.getLocationById(vid);
		HttpUtil.get(getActivity(), url, new jsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				xbTrack = JSON.parseObject(response.toString(), XbTrack.class);
				LogUtils.DebugLog("result json" + response.toString());
				addInfosOverlay();
				super.onSuccess(statusCode, headers, response);
			}
		});
	}

	/**
	 * 初始化图层
	 */
	private void addInfosOverlay() {
		doubleLng = Utils.getLng(xbTrack.getLocation());
		// 位置
		latLng = new LatLng(doubleLng[1], doubleLng[0]);
		points.add(latLng);
		if (marker == null) {
			if (xbTrack.isOnline()) {
				handler_runnable_time = PreferenceUtils.getInstance(
						getActivity()).getTrackTime() * 1000;
				handler.postDelayed(runnable, handler_runnable_time);
				car = online;
			} else {
				handler.removeCallbacks(runnable);
				Utils.ToastMessage(getActivity(), "车辆离线无法跟踪");
				car = offline;
				handler.removeCallbacks(runnable);
			}
			// 图标
			overlayOptions = new MarkerOptions().position(latLng).icon(car)
					.zIndex(5).rotate(360 - xbTrack.getDirection())
					.animateType(MarkerAnimateType.grow);// 生长动画
			marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));

		} else {
			marker.setPosition(latLng);
			marker.setRotate(360 - xbTrack.getDirection());

			if (mPolyline == null) {
				OverlayOptions ooPolyline = new PolylineOptions().width(10)
						.color(getResources().getColor(R.color.app_green))
						.points(points);
				mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
				mBaiduMap.addOverlay(new MarkerOptions()
						.position(points.get(0)).icon(startIcon));
			} else {
				mPolyline.setPoints(points);
			}
		}
		mInfoWindow = new InfoWindow(popupInfo(mMarkerLy, xbTrack),
				marker.getPosition(), Common.INFOWINDOW_POSITION);
		mBaiduMap.showInfoWindow(mInfoWindow);
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(latLng));

	}

	/**
	 * 根据info为布局上的控件设置信息
	 * 
	 * @param mMarkerInfo2
	 * @param info
	 */
	private View popupInfo(View mMarkerLy, final XbTrack xbTrack) {
		ViewHolder viewHolder = null;
		if (mMarkerLy.getTag() == null) {
			viewHolder = new ViewHolder();
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
			mMarkerLy.setTag(viewHolder);
		}
		viewHolder = (ViewHolder) mMarkerLy.getTag();
		viewHolder.popwindows_time.setText("时间:" + xbTrack.getTime());
		if (xbTrack.isOnline()) {
			viewHolder.popwindows_state.setText("在线:" + xbTrack.getSpeed()
					+ "km/h");
			viewHolder.popwindows_state.setTextColor(getResources().getColor(
					R.color.app_green));
		} else {
			viewHolder.popwindows_state.setText("离线");
			viewHolder.popwindows_state.setTextColor(getResources().getColor(
					R.color.text));
		}
		viewHolder.popwindows_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mBaiduMap.hideInfoWindow();
			}
		});
		viewHolder.popwindows_name.setText(xbTrack.getPlateNo());
		viewHolder.popwindows_position.setText("位置:" + xbTrack.getAddr());
		return mMarkerLy;
	}

	/**
	 * 复用弹出面板mMarkerLy的控件
	 * 
	 */
	private class ViewHolder {

		TextView popwindows_name;
		TextView popwindows_state;
		TextView popwindows_time;
		TextView popwindows_position;
		ImageView popwindows_close;

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
			if (handler_runnable_time != 0) {
				handler.postDelayed(runnable, handler_runnable_time);
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
		mMapView.onDestroy();
		handler.removeCallbacks(runnable);
		if (car != null) {
			car.recycle();
		}
		startIcon.recycle();
		super.onDestroy();
	}

}
