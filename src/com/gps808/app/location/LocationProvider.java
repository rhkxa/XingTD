package com.gps808.app.location;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.gps808.app.utils.PreferenceUtils;

public class LocationProvider {
	public LocationClient mLocationClient;
	public GeofenceClient mGeofenceClient;
	public MyLocationListener mMyLocationListener;
	public Vibrator mVibrator;
	Context context;
	public LocationProvider(Context context) {
		super();
		this.context = context;
	}

	public void startLocation() {
		mLocationClient = new LocationClient(context);
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		mGeofenceClient = new GeofenceClient(context);
		mVibrator = (Vibrator) context.getApplicationContext()
				.getSystemService(Service.VIBRATOR_SERVICE);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Battery_Saving);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
		option.setScanSpan(300000);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);
        option.setProdName("shumayiku"); // 设置产品线名称
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}

	/**
	 * 停止，减少资源消耗
	 */
	public void stopListener() {
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.stop();
			mLocationClient = null;
		}
	}

	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			if (location == null) {
				mLocationClient.requestLocation();
			}
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("error code : ");
			sb.append(location.getLocType());
			sb.append("latitude : ");
			sb.append(location.getLatitude());
			sb.append("lontitude : ");
			sb.append(location.getLongitude());
			sb.append("radius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("speed : ");
				sb.append(location.getSpeed());
				sb.append("satellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("irection : ");
				sb.append("addr : ");
				sb.append(location.getAddrStr());
				sb.append(location.getDirection());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("addr : ");
				sb.append(location.getAddrStr());
				// 运营商信息
				sb.append("operationers : ");
				sb.append(location.getOperators());
			}

			sb.append(location.getProvince());
			sb.append(location.getCity());
			sb.append(location.getDistrict());
			// System.out.println(sb.toString());
			// PreferenceUtils.getInstance(getApplicationContext()).setLocal(
			// location.getProvince() + location.getCity()
			// + location.getDistrict());
//			PreferenceUtils.getInstance(context.getApplicationContext())
//					.setLocal(location.getCity());
			// PreferenceUtils.getInstance(getApplicationContext()).setLocal(
			// sb.toString());
		}
	}
}
