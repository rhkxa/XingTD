package com.gps808.app.map;

import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.gps808.app.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.view.View.OnClickListener;

public class ZoomControlView extends RelativeLayout implements OnClickListener {
	private ImageView mButtonZoomin;
	private ImageView mButtonZoomout;
	private MapView mapView;
	private float maxZoomLevel;
	private float minZoomLevel;

	public ZoomControlView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ZoomControlView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.zoom_controls_layout, null);
		mButtonZoomin = (ImageView) view.findViewById(R.id.zoomin);
		mButtonZoomout = (ImageView) view.findViewById(R.id.zoomout);
		mButtonZoomin.setOnClickListener(this);
		mButtonZoomout.setOnClickListener(this);
		addView(view);
	}

	@Override
	public void onClick(View v) {
		if (mapView == null) {
			throw new NullPointerException(
					"you can call setMapView(MapView mapView) at first");
		}
		switch (v.getId()) {
		case R.id.zoomin: {
			mapView.getMap().setMapStatus(MapStatusUpdateFactory.zoomIn());
			break;
		}
		case R.id.zoomout: {
			mapView.getMap().setMapStatus(MapStatusUpdateFactory.zoomOut());
			break;
		}
		}
	}

	/**
	 * 与MapView设置关联
	 * 
	 * @param mapView
	 */
	public void setMapView(MapView mapView) {
		this.mapView = mapView;
		// 获取最大的缩放级别
		maxZoomLevel = mapView.getMap().getMaxZoomLevel();
		// 获取最大的缩放级别
		minZoomLevel = mapView.getMap().getMinZoomLevel();
	}

	/**
	 * 根据MapView的缩放级别更新缩放按钮的状态，当达到最大缩放级别，设置mButtonZoomin
	 * 为不能点击，反之设置mButtonZoomout
	 * 
	 * @param level
	 */
	public void refreshZoomButtonStatus(float level) {
		if (mapView == null) {
			throw new NullPointerException(
					"you can call setMapView(MapView mapView) at first");
		}
		if (level > minZoomLevel && level < maxZoomLevel) {
			if (!mButtonZoomout.isEnabled()) {
				mButtonZoomout.setEnabled(true);
			}
			if (!mButtonZoomin.isEnabled()) {
				mButtonZoomin.setEnabled(true);
			}
		} else if (level == minZoomLevel) {
			mButtonZoomout.setEnabled(false);
		} else if (level == maxZoomLevel) {
			mButtonZoomin.setEnabled(false);
		}
	}

}
