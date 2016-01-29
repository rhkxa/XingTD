package com.gps808.app.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.gps808.app.R;
import com.gps808.app.adapter.AllMapExpanListAdapter;
import com.gps808.app.adapter.HotMapListViewAdapter;
import com.gps808.app.bean.XbMap;
import com.gps808.app.bean.XbMapCityBean;
import com.gps808.app.utils.BaseFragment;
import com.gps808.app.utils.StringUtils;
import com.gps808.app.view.NoRollExpandableListView;
import com.gps808.app.view.NoRollListView;

public class OnLineMapFragment extends BaseFragment implements
		MKOfflineMapListener {
	private View view;
	private NoRollExpandableListView allCityList;
	private NoRollListView hotCityList;
	private AllMapExpanListAdapter allAdapter;
	private HotMapListViewAdapter hotAdapter;
	private MKOfflineMap mOffline = null;
	private List<XbMapCityBean> allCities;
	private List<XbMap> hotCities;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_online, null);
		init();
		return view;
	}

	private void init() {
		// TODO Auto-generated method stub
		mOffline = new MKOfflineMap();
		mOffline.init(this);
		allCityList = (NoRollExpandableListView) view
				.findViewById(R.id.all_map_list);
		hotCityList = (NoRollListView) view.findViewById(R.id.hot_map_list);
		hotCities = new ArrayList<XbMap>();
		// 获取热闹城市列表
		ArrayList<MKOLSearchRecord> records1 = mOffline.getHotCityList();
		if (records1 != null) {
			for (MKOLSearchRecord r : records1) {
				XbMap xbMap = new XbMap();
				xbMap.setId(r.cityID);
				xbMap.setName(r.cityName);
				xbMap.setSize(StringUtils.formatDataSize(r.size));
				hotCities.add(xbMap);
			}
		}
		hotAdapter = new HotMapListViewAdapter(getActivity(), hotCities);
		hotCityList.setAdapter(hotAdapter);

		// 获取所有支持离线地图的城市
		allCities = new ArrayList<XbMapCityBean>();
		ArrayList<MKOLSearchRecord> records2 = mOffline.getOfflineCityList();
		for (MKOLSearchRecord province : records2) {
			XbMapCityBean xbMap = new XbMapCityBean();
			xbMap.setId(province.cityID);
			xbMap.setName(province.cityName);
			xbMap.setSize(StringUtils.formatDataSize(province.size));
			List<XbMap> citys = new ArrayList<XbMap>();
			if (province.childCities != null) {
				for (MKOLSearchRecord city : province.childCities) {
					XbMap map = new XbMap();
					map.setId(city.cityID);
					map.setName(city.cityName);
					map.setSize(StringUtils.formatDataSize(city.size));
					citys.add(map);
				}
			} else {
				XbMap map = new XbMap();
				map.setId(province.cityID);
				map.setName(province.cityName);
				map.setSize(StringUtils.formatDataSize(province.size));
				citys.add(map);
			}
			xbMap.setCity(citys);

			allCities.add(xbMap);
		}
		allAdapter = new AllMapExpanListAdapter(getActivity(), allCities);
		allCityList.setAdapter(allAdapter);
	}

	@Override
	public void onGetOfflineMapState(int type, int state) {
		// TODO Auto-generated method stub
		switch (type) {
		case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
			MKOLUpdateElement update = mOffline.getUpdateInfo(state);
			// 处理下载进度更新提示

		}
			break;
		case MKOfflineMap.TYPE_NEW_OFFLINE:
			// 有新离线地图安装
			Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
			break;
		case MKOfflineMap.TYPE_VER_UPDATE:
			// 版本更新提示
			// MKOLUpdateElement e = mOffline.getUpdateInfo(state);

			break;
		default:
			break;
		}
	}
}
