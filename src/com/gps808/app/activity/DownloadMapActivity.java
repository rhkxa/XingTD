package com.gps808.app.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.gps808.app.R;
import com.gps808.app.adapter.LocalMapAdapter;
import com.gps808.app.adapter.LocalMapAdapter.OnLocalMapListener;
import com.gps808.app.adapter.RemoteMapListViewAdapter;
import com.gps808.app.adapter.RemoteMapListViewAdapter.RemoteMapListener;
import com.gps808.app.bean.XbMap;
import com.gps808.app.fragment.HeaderFragment;

import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.Utils;

public class DownloadMapActivity extends BaseActivity {
	private RadioGroup map_rg;
	private ListView offline_map_list, online_map_list;
	private MKOfflineMap mOffline = null;
	private LocalMapAdapter lAdapter;
	private ArrayList<MKOLUpdateElement> localMapList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offline_map);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		HeaderFragment headerFragment = (HeaderFragment) this
				.getSupportFragmentManager().findFragmentById(R.id.title);
		headerFragment.setTitleText("地图下载");
		mOffline = new MKOfflineMap();
		mOffline.init(new MKOfflineMapListener() {

			@Override
			public void onGetOfflineMapState(int arg0, int arg1) {
				// TODO Auto-generated method stub

			}
		});
		map_rg = (RadioGroup) findViewById(R.id.map_rg);
		offline_map_list = (ListView) findViewById(R.id.offline_map_list);
		online_map_list = (ListView) findViewById(R.id.online_map_list);
		map_rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch (arg0.getCheckedRadioButtonId()) {
				case R.id.offline_map:
					online_map_list.setVisibility(View.GONE);
					offline_map_list.setVisibility(View.VISIBLE);

					break;
				case R.id.online_map:
					online_map_list.setVisibility(View.VISIBLE);
					offline_map_list.setVisibility(View.GONE);

					break;
				}
			}
		});
		// 获取所有支持离线地图的城市
		ArrayList<XbMap> allCities = new ArrayList<XbMap>();
		ArrayList<MKOLSearchRecord> records2 = mOffline.getOfflineCityList();
		for (MKOLSearchRecord r : records2) {
			XbMap xbMap = new XbMap();
			xbMap.setId(r.cityID);
			xbMap.setName(r.cityName);
			xbMap.setSize(this.formatDataSize(r.size));
			allCities.add(xbMap);
		}
		RemoteMapListViewAdapter mAdapter = new RemoteMapListViewAdapter(
				DownloadMapActivity.this, allCities);
		mAdapter.setRemoteMapListener(new RemoteMapListener() {

			@Override
			public void onStart(TextView textView, int cityid) {
				// TODO Auto-generated method stub
				start(cityid);
			}

			@Override
			public void onStop(TextView textView, int cityid) {
				// TODO Auto-generated method stub
				stop(cityid);
			}
		});
		online_map_list.setAdapter(mAdapter);
		// 获取已下过的离线地图信息
		localMapList = mOffline.getAllUpdateInfo();
		if (localMapList == null) {
			localMapList = new ArrayList<MKOLUpdateElement>();
		}

		LocalMapAdapter lAdapter = new LocalMapAdapter(
				DownloadMapActivity.this, localMapList);
		lAdapter.setOnLocalMapListener(new OnLocalMapListener() {

			@Override
			public void onRemove(int cityId) {
				// TODO Auto-generated method stub
				remove(cityId);
			}
		});
		offline_map_list.setAdapter(lAdapter);

	}

	/**
	 * 开始下载
	 * 
	 * @param view
	 */
	public void start(int cityid) {
		mOffline.start(cityid);
		Toast.makeText(this, "开始下载离线地图. cityid: " + cityid, Toast.LENGTH_SHORT)
				.show();
		updateView();
	}

	/**
	 * 暂停下载
	 * 
	 * @param view
	 */
	public void stop(int cityid) {
		mOffline.pause(cityid);
		Toast.makeText(this, "暂停下载离线地图. cityid: " + cityid, Toast.LENGTH_SHORT)
				.show();
		updateView();
	}

	/**
	 * 删除离线地图
	 * 
	 * @param view
	 */
	public void remove(int cityid) {
		mOffline.remove(cityid);
		Toast.makeText(this, "删除离线地图. cityid: " + cityid, Toast.LENGTH_SHORT)
				.show();
		updateView();
	}

	/**
	 * 更新状态显示
	 */
	public void updateView() {
		localMapList = mOffline.getAllUpdateInfo();
		if (localMapList == null) {
			localMapList = new ArrayList<MKOLUpdateElement>();
		}
		lAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onPause() {

		// MKOLUpdateElement temp = mOffline.getUpdateInfo(cityid);
		// if (temp != null && temp.status == MKOLUpdateElement.DOWNLOADING) {
		// mOffline.pause(cityid);
		// }
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public String formatDataSize(int size) {
		String ret = "";
		if (size < (1024 * 1024)) {
			ret = String.format("%dK", size / 1024);
		} else {
			ret = String.format("%.1fM", size / (1024 * 1024.0));
		}
		return ret;
	}

	@Override
	protected void onDestroy() {
		/**
		 * 退出时，销毁离线地图模块
		 */
		mOffline.destroy();
		super.onDestroy();
	}

}
