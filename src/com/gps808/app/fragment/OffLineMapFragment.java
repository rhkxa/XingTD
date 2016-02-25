package com.gps808.app.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.gps808.app.R;
import com.gps808.app.adapter.LocalMapAdapter;
import com.gps808.app.utils.BaseFragment;

public class OffLineMapFragment extends BaseFragment implements
		MKOfflineMapListener {

	private View view;
	private ListView down_finish_list, down_load_list;
	private MKOfflineMap mOffline = null;
	/**
	 * 已下载的离线地图信息列表
	 */
	private ArrayList<MKOLUpdateElement> localMapList = null;
	private LocalMapAdapter lAdapter = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_offline, null);
		init();
		return view;
	}

	private void init() {
		// TODO Auto-generated method stub
		mOffline = new MKOfflineMap();
		mOffline.init(this);
		lAdapter = new LocalMapAdapter(getActivity(), localMapList);
		down_finish_list = (ListView) view.findViewById(R.id.down_finish_list);
		down_load_list = (ListView) view.findViewById(R.id.down_load_list);

		// 获取已下过的离线地图信息
		localMapList = mOffline.getAllUpdateInfo();
		if (localMapList == null) {
			localMapList = new ArrayList<MKOLUpdateElement>();
		}
		lAdapter = new LocalMapAdapter(getActivity(), localMapList);
		down_finish_list.setAdapter(lAdapter);
	}

	@Override
	public void onGetOfflineMapState(int arg0, int arg1) {
		// TODO Auto-generated method stub

	}
}
