package com.gps808.app.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
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
import com.gps808.app.fragment.SearchFragment;
import com.gps808.app.fragment.SearchFragment.OnSearchClickListener;
import com.gps808.app.models.XbDisplayLine;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.FileUtils;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.PreferenceUtils;
import com.gps808.app.utils.StringUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;
import com.gps808.app.view.FancyButton;
import com.loopj.android.http.JsonHttpResponseHandler;

public class DisplayLineActivity extends BaseActivity {
	private String mSDCardPath = null;
	// 地图相关
	MapView mMapView;
	BaiduMap mBaiduMap;
	Polyline mPolyline;
	BitmapDescriptor endIcon = null;
	BitmapDescriptor startIcon = null;
	// 定位相关
	LocationClient mLocClient;
	String rid;
	private final String saveFile = "Routes";

	private XbDisplayLine xbDisplayLine;
	private FancyButton normal_navi;
	private FancyButton voice_navi;
	private int guideType = 0;
	int macthNum = 0;
	private String currentNode;
	private CheckBox navi_check;
	private LinearLayout search_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_line);
		init();
	}

	private boolean initDirs() {
		mSDCardPath = FileUtils.getSDRoot();
		if (mSDCardPath == null) {
			return false;
		}
		File f = new File(mSDCardPath, FileUtils.APP_FOLDER_NAME);
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
		mMapView.showZoomControls(false);
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
		mBaiduMap.setMapStatus(msu);
		// 隐藏百度LOGO和 ZoomControl
		int count = mMapView.getChildCount();
		for (int i = 0; i < count; i++) {
			View child = mMapView.getChildAt(i);
			if (child instanceof ImageView || child instanceof ZoomControls) {
				child.setVisibility(View.INVISIBLE);
			}
		}
		SearchFragment searchFragment = (SearchFragment) this
				.getSupportFragmentManager().findFragmentById(R.id.search_bar);
		searchFragment.setHint("输入经纬度：116.358549,39.903066");
		searchFragment.setOnSearchClickListener(new OnSearchClickListener() {

			@Override
			public void onSearch(String key) {
				// TODO Auto-generated method stub
				guideType = 1;
				currentNode = key;
				getMatch();

			}

			@Override
			public void onSearchClose() {
				// TODO Auto-generated method stub

			}
		});
		navi_check = (CheckBox) findViewById(R.id.navi_check);
		search_layout = (LinearLayout) findViewById(R.id.search_layout);
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
		if (PreferenceUtils.getInstance(DisplayLineActivity.this).getUserName()
				.equals("tf")) {
			navi_check.setVisibility(View.VISIBLE);
			search_layout.setVisibility(View.VISIBLE);
		}
		if (!BaiduNaviManager.isNaviInited()) {
			initNavi();
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

	// 加载路线信息
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

	// 解析路线数据
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

		}
		builder.include(points.get(0));
		builder.include(points.get(size - 1));
		// 缩放地图，使所有Overlay都在合适的视野内
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngBounds(builder
				.build()));
		addCustomElementsDemo(points);
	}

	// 开启定位
	private void startLocation() {
		showProgressDialog(DisplayLineActivity.this, "正在搜索附近车辆，等稍等");
		// 定位初始化
		mLocClient = new LocationClient(getApplicationContext());
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开GPS
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(5000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		mLocClient.registerLocationListener(new BDLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation location) {
				macthNum++;
				LogUtils.DebugLog("定位次数：" + macthNum);
				// map view 销毁后不在处理新接收的位置
				if (location == null || mMapView == null) {
					return;
				}
				if (macthNum < 12) {
					currentNode = location.getLongitude() + ","
							+ location.getLatitude();
					getMatch();
				} else {
					LogUtils.DebugLog("停止定位，关闭弹窗" + macthNum);
					stopLocation();
					dismissProgressDialog();
					macthNum = 0;
					Utils.ToastMessage(DisplayLineActivity.this,
							"对不起，附近没有可导航的车辆");
				}
			}
		});
	}

	private void stopLocation() {
		if (mLocClient != null) {
			// 退出时销毁定位
			mLocClient.stop();
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

	// 车辆匹配
	private void getMatch() {
		String url = UrlConfig.getMatchVichcle(currentNode, rid);
		HttpUtil.get(DisplayLineActivity.this, url,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						LogUtils.DebugLog("result" + response.toString());
						if (Utils.requestOk(response)) {
							stopLocation();
							dismissProgressDialog();
							macthNum = 0;
							toGuide(Utils.getKey(response, "wayPoints"));
						}
						super.onSuccess(statusCode, headers, response);
					}
				});

	}

	private void initNavi() {
		if (!initDirs()) {
			return;
		}

		BaiduNaviManager.getInstance().init(this, mSDCardPath,
				FileUtils.APP_FOLDER_NAME, new NaviInitListener() {
					@Override
					public void onAuthResult(int status, String msg) {
						if (0 == status) {
							LogUtils.DebugLog("key校验成功!");
						} else {
							LogUtils.DebugLog("key校验失败, " + msg);
						}

					}

					public void initSuccess() {
						LogUtils.DebugLog("导航引擎初始化成功");
						BNaviSettingManager
								.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_AUTO);
						BNaviSettingManager
								.setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
						BNaviSettingManager
								.setVoiceMode(BNaviSettingManager.VoiceMode.Novice);
						BNaviSettingManager
								.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.AUTO_MODE);
						BNaviSettingManager
								.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
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

	// 路线规划
	private void routeplanToNavi(List<BNRoutePlanNode> list) {
		showProgressDialog(DisplayLineActivity.this, "正在启动语音导航……");
		BaiduNaviManager.getInstance().launchNavigator(this, list, 1,
				navi_check.isChecked(), new RoutePlanListener() {
					@Override
					public void onRoutePlanFailed() {
						// TODO Auto-generated method stub
						dismissProgressDialog();
						Utils.ToastMessage(DisplayLineActivity.this,
								"对不起导航启动失败!");
						LogUtils.DebugLog("算路失败");
					}

					@Override
					public void onJumpToNavigator() {
						// TODO Auto-generated method stub
						dismissProgressDialog();
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
		}
	};

	private void toGuide(String wayPoints) {
		switch (guideType) {
		case 0:
			Intent intent = new Intent(DisplayLineActivity.this,
					GuideNormalActivity.class);
			intent.putExtra("route", JSON.toJSONString(xbDisplayLine));
			startActivity(intent);
			break;

		case 1:
			if (StringUtils.isEmpty(wayPoints)) {
				LogUtils.DebugLog("途经点为空");
				Utils.ToastMessage(DisplayLineActivity.this, "对不起,当前路线不能进行导航");
				return;
			}
			List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
			double[] doubleLng = Utils.getLng(currentNode);
			BNRoutePlanNode node = new BNRoutePlanNode(doubleLng[0],
					doubleLng[1], "", null, CoordinateType.BD09LL);
			list.add(node);
			String[] strLng = Utils.getSplit(wayPoints, ";");
			int size = strLng.length > 4 ? 4 : strLng.length;
			for (int i = 0; i < size; i++) {
				doubleLng = Utils.getLng(strLng[i]);
				node = new BNRoutePlanNode(doubleLng[0], doubleLng[1], "",
						null, CoordinateType.BD09LL);
				list.add(node);
			}
			if (BaiduNaviManager.isNaviInited()) {
				routeplanToNavi(list);
			}
			break;
		}

	}

}
