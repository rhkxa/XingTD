package com.gps808.app.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ZoomControls;

import com.alibaba.fastjson.JSON;
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
import com.gps808.app.R;
import com.gps808.app.bean.XbDisplayLine;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;

public class DisplayLineActivity extends BaseActivity {

	// 地图相关
	MapView mMapView;
	BaiduMap mBaiduMap;
	Polyline mPolyline;
	Polyline mColorfulPolyline;
	Polyline mTexturePolyline;
	BitmapDescriptor endIcon = null;
	BitmapDescriptor startIcon = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_line);
		init();
		getData();
	}

	private void init() {
		// TODO Auto-generated method stub
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
	}

	/**
	 * 添加点、线、多边形、圆、文字
	 */
	public void addCustomElementsDemo(List<LatLng> points) {
		if(points.size()>=2){
		// 添加普通折线绘制
		OverlayOptions ooPolyline = new PolylineOptions().width(10)
				.color(getResources().getColor(R.color.app_green)).points(points);
		mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
		mBaiduMap.addOverlay(new MarkerOptions().position(points.get(0)).icon(
				startIcon));
		mBaiduMap.addOverlay(new MarkerOptions().position(
				points.get(points.size() - 1)).icon(endIcon));
		}
		// 添加多颜色分段的折线绘制
		// LatLng p11 = new LatLng(39.965, 116.444);
		// LatLng p21 = new LatLng(39.925, 116.494);
		// LatLng p31 = new LatLng(39.955, 116.534);
		// LatLng p41 = new LatLng(39.905, 116.594);
		// LatLng p51 = new LatLng(39.965, 116.644);
		// List<LatLng> points1 = new ArrayList<LatLng>();
		// points1.add(p11);
		// points1.add(p21);
		// points1.add(p31);
		// points1.add(p41);
		// points1.add(p51);
		// List<Integer> colorValue = new ArrayList<Integer>();
		// colorValue.add(0xAAFF0000);
		// colorValue.add(0xAA00FF00);
		// colorValue.add(0xAA0000FF);
		// OverlayOptions ooPolyline1 = new PolylineOptions().width(10)
		// .color(0xAAFF0000).points(points1).colorsValues(colorValue);
		// mColorfulPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline1);

		// 添加多纹理分段的折线绘制
		// LatLng p111 = new LatLng(39.865, 116.444);
		// LatLng p211 = new LatLng(39.825, 116.494);
		// LatLng p311 = new LatLng(39.855, 116.534);
		// LatLng p411 = new LatLng(39.805, 116.594);
		// List<LatLng> points11 = new ArrayList<LatLng>();
		// points11.add(p111);
		// points11.add(p211);
		// points11.add(p311);
		// points11.add(p411);
		// List<BitmapDescriptor> textureList = new
		// ArrayList<BitmapDescriptor>();
		// textureList.add(mRedTexture);
		// textureList.add(mBlueTexture);
		// textureList.add(mGreenTexture);
		// List<Integer> textureIndexs = new ArrayList<Integer>();
		// textureIndexs.add(0);
		// textureIndexs.add(1);
		// textureIndexs.add(2);
		// OverlayOptions ooPolyline11 = new PolylineOptions().width(20)
		// .points(points11).dottedLine(true)
		// .customTextureList(textureList).textureIndex(textureIndexs);
		// mTexturePolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline11);
		//
		// // 添加弧线
		// OverlayOptions ooArc = new ArcOptions().color(0xAA00FF00).width(4)
		// .points(p1, p2, p3);
		// mBaiduMap.addOverlay(ooArc);
		// // 添加圆
		// LatLng llCircle = new LatLng(39.90923, 116.447428);
		// OverlayOptions ooCircle = new CircleOptions().fillColor(0x000000FF)
		// .center(llCircle).stroke(new Stroke(5, 0xAA000000))
		// .radius(1400);
		// mBaiduMap.addOverlay(ooCircle);
		//
		// LatLng llDot = new LatLng(39.98923, 116.397428);
		// OverlayOptions ooDot = new DotOptions().center(llDot).radius(6)
		// .color(0xFF0000FF);
		// mBaiduMap.addOverlay(ooDot);
		// // 添加多边形
		// LatLng pt1 = new LatLng(39.93923, 116.357428);
		// LatLng pt2 = new LatLng(39.91923, 116.327428);
		// LatLng pt3 = new LatLng(39.89923, 116.347428);
		// LatLng pt4 = new LatLng(39.89923, 116.367428);
		// LatLng pt5 = new LatLng(39.91923, 116.387428);
		// List<LatLng> pts = new ArrayList<LatLng>();
		// pts.add(pt1);
		// pts.add(pt2);
		// pts.add(pt3);
		// pts.add(pt4);
		// pts.add(pt5);
		// OverlayOptions ooPolygon = new PolygonOptions().points(pts)
		// .stroke(new Stroke(5, 0xAA00FF00)).fillColor(0xAAFFFF00);
		// mBaiduMap.addOverlay(ooPolygon);
		// // 添加文字
		// LatLng llText = new LatLng(39.86923, 116.397428);
		// OverlayOptions ooText = new TextOptions().bgColor(0xAAFFFF00)
		// .fontSize(24).fontColor(0xFFFF00FF).text("百度地图SDK").rotate(-30)
		// .position(llText);
		// mBaiduMap.addOverlay(ooText);
	}

	private void getData() {
		showProgressDialog(DisplayLineActivity.this, "正在加载路线信息");
		String url = UrlConfig.getVehicleRoutesInfo(getIntent().getStringExtra(
				"rid"));
		HttpUtil.get(DisplayLineActivity.this, url,
				new jsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						LogUtils.DebugLog("result json", response.toString());
						XbDisplayLine xbDisplayLine = JSON.parseObject(
								response.toString(), XbDisplayLine.class);
						parseData(xbDisplayLine);
						super.onSuccess(statusCode, headers, response);
					}
				});
	}

	private void parseData(XbDisplayLine xbDisplayLine) {
		List<LatLng> points = new ArrayList<LatLng>();
		String[] strLng = Utils.getSplit(xbDisplayLine.getTrack(), ";");
		int size = strLng.length;
		LatLng latLng = null;
		double[] doubleLng;
		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		for (int i = 0; i < size; i++) {
			doubleLng = Utils.getLng(strLng[i], ",");
			latLng = new LatLng(doubleLng[1], doubleLng[0]);
			points.add(latLng);
			builder.include(latLng);
		}
		// 缩放地图，使所有Overlay都在合适的视野内
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngBounds(builder
				.build()));
		addCustomElementsDemo(points);
	}

	public void clearClick() {
		// 清除所有图层
		mMapView.getMap().clear();
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
		endIcon.recycle();
		startIcon.recycle();
		super.onDestroy();
	}

}
