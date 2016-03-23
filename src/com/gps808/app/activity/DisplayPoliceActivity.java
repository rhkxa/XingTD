package com.gps808.app.activity;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.MarkerOptions.MarkerAnimateType;
import com.baidu.mapapi.model.LatLng;
import com.gps808.app.R;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.models.XbPolice;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.Common;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;

public class DisplayPoliceActivity extends BaseActivity {

	// 地图相关
	MapView mMapView;
	BaiduMap mBaiduMap;
	BitmapDescriptor locationLogo = BitmapDescriptorFactory
			.fromResource(R.drawable.map_location_icon);;

	private View mMarkerLy;
	private HeaderFragment headerFragment;

	private XbPolice xbPolice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_police);
		init();
		getData();
	}

	private void init() {
		// TODO Auto-generated method stub
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		LayoutInflater inflater = LayoutInflater
				.from(DisplayPoliceActivity.this);
		mMarkerLy = inflater.inflate(R.layout.popwindows_police, null);
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
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker arg0) {
				// TODO Auto-generated method stub
				InfoWindow mInfoWindow = new InfoWindow(popupInfo(mMarkerLy,
						xbPolice), arg0.getPosition(), Common.INFOWINDOW_POSITION);
				mBaiduMap.showInfoWindow(mInfoWindow);
				return true;
			}
		});
	}

	private void getData() {
		showProgressDialog(DisplayPoliceActivity.this, "正在加载路线信息");
		String url = UrlConfig.getVehicleAlarms(getIntent().getStringExtra(
				"aid"));
		HttpUtil.get(DisplayPoliceActivity.this, url,
				new jsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						LogUtils.DebugLog("result json", response.toString());
						xbPolice = JSON.parseObject(response.toString(),
								XbPolice.class);
						addOverlay();
						super.onSuccess(statusCode, headers, response);
					}
				});
	}

	private void addOverlay() {
		headerFragment.setTitleText(xbPolice.getPlateNo());
		double[] doubleLng = Utils.getLng(xbPolice.getLocation());
		LatLng latLng = new LatLng(doubleLng[1], doubleLng[0]);
		// 图标
		OverlayOptions overlayOptions = new MarkerOptions().position(latLng)
				.icon(locationLogo).zIndex(5);

		Marker marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
		InfoWindow mInfoWindow = new InfoWindow(popupInfo(mMarkerLy, xbPolice),
				latLng, Common.INFOWINDOW_POSITION);
		mBaiduMap.showInfoWindow(mInfoWindow);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(latLng,
				14.0f);
		mBaiduMap.setMapStatus(msu);
	}

	/**
	 * 根据info为布局上的控件设置信息
	 * 
	 * @param mMarkerInfo2
	 * @param info
	 */
	private View popupInfo(View mMarkerLy, final XbPolice xbPolice) {
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
		viewHolder.popwindows_time.setText("时间:" + xbPolice.getTime());
		viewHolder.popwindows_state.setText(xbPolice.getAlarmName());
		viewHolder.popwindows_name.setText(xbPolice.getPlateNo());
		viewHolder.popwindows_position.setText("位置:" + xbPolice.getAddr());
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

		TextView popwindows_name;
		TextView popwindows_state;
		TextView popwindows_time;
		TextView popwindows_position;
		ImageView popwindows_close;

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
		// mRedTexture.recycle();
		// mBlueTexture.recycle();
		// mGreenTexture.recycle();
		locationLogo.recycle();
		super.onDestroy();
	}
}
