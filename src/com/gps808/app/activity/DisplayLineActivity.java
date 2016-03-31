package com.gps808.app.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ZoomControls;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.navisdk.adapter.BaiduNaviManager.NaviInitListener;
import com.baidu.navisdk.adapter.BaiduNaviManager.RoutePlanListener;
import com.gps808.app.R;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.models.MatchInfo;
import com.gps808.app.models.XbDisplayLine;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.FileUtils;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.StringUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;
import com.gps808.app.view.FancyButton;
import com.loopj.android.http.JsonHttpResponseHandler;

public class DisplayLineActivity extends BaseActivity {

	private static final String APP_FOLDER_NAME = "XingTD";

	private String mSDCardPath = null;

	// 地图相关
	MapView mMapView;
	BaiduMap mBaiduMap;
	Polyline mPolyline;

	BitmapDescriptor endIcon = null;
	BitmapDescriptor startIcon = null;
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	String rid;
	private final String saveFile = "Routes";

	private XbDisplayLine xbDisplayLine;
	private FancyButton normal_navi;
	private FancyButton voice_navi;
	private int guideType = 0;
	private boolean isMatch = false;
	int macthNum = 0;

	BNRoutePlanNode oneNode = new BNRoutePlanNode(116.822153, 38.585029, "第一",
			null, CoordinateType.BD09LL);
	BNRoutePlanNode twoNode = new BNRoutePlanNode(116.820955, 38.591473, "第二",
			null, CoordinateType.BD09LL);
	BNRoutePlanNode threeNode = new BNRoutePlanNode(116.809637, 38.592197,
			"第三", null, CoordinateType.BD09LL);
	BNRoutePlanNode fourNode = new BNRoutePlanNode(116.810925, 38.579453, "第四",
			null, CoordinateType.BD09LL);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_line);
		init();

	}

	private String getSdcardDir() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(
				Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}

	private boolean initDirs() {
		mSDCardPath = getSdcardDir();
		if (mSDCardPath == null) {
			return false;
		}
		File f = new File(mSDCardPath, APP_FOLDER_NAME);
		if (!f.exists()) {
			try {
				f.mkdir();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	private void init() {
		// TODO Auto-generated method stub
		rid = getIntent().getStringExtra("rid");
		HeaderFragment headerFragment = (HeaderFragment) this
				.getSupportFragmentManager().findFragmentById(R.id.title);
		headerFragment.setTitleText("路线详情");
		endIcon = BitmapDescriptorFactory.fromResource(R.drawable.map_end_icon);
		startIcon = BitmapDescriptorFactory
				.fromResource(R.drawable.map_start_icon);
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

		// 语音导航
		voice_navi = (FancyButton) findViewById(R.id.voice_navi);
		voice_navi.setOnClickListener(guideClick);
		// 普通导航
		normal_navi = (FancyButton) findViewById(R.id.normal_navi);
		normal_navi.setOnClickListener(guideClick);
		// 从本地获取
		String content = FileUtils.read(DisplayLineActivity.this, saveFile
				+ rid);
		LogUtils.DebugLog("read content ", saveFile + rid + content);
		if (StringUtils.isEmpty(content)) {
			getData();
		} else {
			xbDisplayLine = JSON.parseObject(content, XbDisplayLine.class);
			parseData();
		}

	}

	/**
	 * 添加点、线、多边形、圆、文字
	 */
	public void addCustomElementsDemo(List<LatLng> points) {
		if (points.size() >= 2) {
			// 添加普通折线绘制
			OverlayOptions ooPolyline = new PolylineOptions().width(10)
					.color(getResources().getColor(R.color.app_green))
					.points(points);
			mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
			mBaiduMap.addOverlay(new MarkerOptions().position(points.get(0))
					.icon(startIcon));
			mBaiduMap.addOverlay(new MarkerOptions().position(
					points.get(points.size() - 1)).icon(endIcon));
		}
	}

	private void getData() {
		String url = UrlConfig.getVehicleRoutesInfo(rid);
		HttpUtil.get(DisplayLineActivity.this, url,
				new jsonHttpResponseHandler() {
					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						showProgressDialog(DisplayLineActivity.this, "正在加载路线信息");
						super.onStart();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						LogUtils.DebugLog("result json", response.toString());
						xbDisplayLine = JSON.parseObject(response.toString(),
								XbDisplayLine.class);
						FileUtils.write(DisplayLineActivity.this, saveFile
								+ rid, response.toString());
						parseData();
						super.onSuccess(statusCode, headers, response);
					}
				});
	}

	private void parseData() {
		List<LatLng> points = new ArrayList<LatLng>();
		String[] strLng = Utils.getSplit(xbDisplayLine.getTrack(), ";");
		int size = strLng.length;
		LatLng latLng = null;
		double[] doubleLng;
		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		for (int i = 0; i < size; i++) {
			doubleLng = Utils.getLng(strLng[i]);
			latLng = new LatLng(doubleLng[1], doubleLng[0]);
			points.add(latLng);
			builder.include(latLng);
		}
		// 缩放地图，使所有Overlay都在合适的视野内
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngBounds(builder
				.build()));
		addCustomElementsDemo(points);
	}

	private void startLocation() {
		// 定位初始化
		mLocClient = new LocationClient(getApplicationContext());
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(5000);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	private void stopLocation() {
		if (mLocClient != null) {
			// 退出时销毁定位
			mLocClient.stop();
			// 关闭定位图层
			mBaiduMap.setMyLocationEnabled(false);
		}
	}

	public void clearClick() {
		// 清除所有图层
		mMapView.getMap().clear();
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
				return;
			}
			if (!isMatch) {
				getMatch(location.getLongitude() + "," + location.getLatitude());
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
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
		endIcon.recycle();
		startIcon.recycle();
		stopLocation();
		super.onDestroy();
	}

	String showStr = "";

	private void getMatch(String loc) {
		macthNum++;
		if (macthNum <= 12) {
			String url = UrlConfig.getMatchVichcle(loc, rid);
			HttpUtil.get(DisplayLineActivity.this, url,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							// TODO Auto-generated method stub
							MatchInfo matchInfo = JSON.parseObject(
									response.toString(), MatchInfo.class);
							if (matchInfo.isSuccess()) {
								dismissProgressDialog();
								isMatch = true;
								toGuide();
							}

							super.onSuccess(statusCode, headers, response);
						}
					});
		} else {
			macthNum = 0;
			stopLocation();
			dismissProgressDialog();
			Utils.ToastMessage(DisplayLineActivity.this, "对不起，附近没有可导航的车辆");
		}

	}

	String authinfo = null;

	private void initNavi() {
		BaiduNaviManager.getInstance().init(this, mSDCardPath, APP_FOLDER_NAME,
				new NaviInitListener() {
					@Override
					public void onAuthResult(int status, String msg) {
						if (0 == status) {
							authinfo = "key校验成功!";
						} else {
							authinfo = "key校验失败, " + msg;
						}
						LogUtils.DebugLog(authinfo);
					}

					public void initSuccess() {
						LogUtils.DebugLog("导航引擎初始化成功");
						BNaviSettingManager
								.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
						BNaviSettingManager
								.setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
						BNaviSettingManager
								.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
						BNaviSettingManager
								.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
						BNaviSettingManager
								.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
						routeplanToNavi();
					}

					public void initStart() {

						LogUtils.DebugLog("导航引擎初始化开始");
					}

					public void initFailed() {

						LogUtils.DebugLog("导航引擎初始化失败");
					}

				}, null, ttsHandler, null);

	}

	/**
	 * 内部TTS播报状态回传handler
	 */
	private Handler ttsHandler = new Handler() {
		public void handleMessage(Message msg) {
			int type = msg.what;
			switch (type) {
			case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {

				break;
			}
			case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {

				break;
			}
			default:
				break;
			}
		}
	};

	private void routeplanToNavi() {
		List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
		list.add(oneNode);
		list.add(twoNode);
		list.add(threeNode);
		list.add(fourNode);
		BaiduNaviManager.getInstance().launchNavigator(this, list, 1, false,
				new RoutePlanListener() {

					@Override
					public void onRoutePlanFailed() {
						// TODO Auto-generated method stub
						LogUtils.DebugLog("算路失败");
					}

					@Override
					public void onJumpToNavigator() {
						// TODO Auto-generated method stub
						LogUtils.DebugLog("算路成功");
						Intent intent = new Intent(DisplayLineActivity.this,
								GuideVoiceActivity.class);
						startActivity(intent);
					}
				});
	}

	private OnClickListener guideClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			startLocation();
			switch (arg0.getId()) {
			case R.id.voice_navi:
				guideType = 1;
				break;
			case R.id.normal_navi:
				guideType = 0;
				break;
			}
			toGuide();
		}
	};

	private void toGuide() {
		switch (guideType) {
		case 0:
			Intent intent = new Intent(DisplayLineActivity.this,
					GuideNormalActivity.class);
			intent.putExtra("route", JSON.toJSONString(xbDisplayLine));
			startActivity(intent);
			break;

		case 1:
			break;
		}

	}

}
