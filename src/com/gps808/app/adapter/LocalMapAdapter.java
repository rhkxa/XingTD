package com.gps808.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.gps808.app.R;
import com.gps808.app.view.FancyButton;

/**
 * 离线地图管理列表适配器
 */
public class LocalMapAdapter extends BaseAdapter {

	private Context mContext;
	private List<MKOLUpdateElement> localMapList;

	public LocalMapAdapter(Context mContext,
			List<MKOLUpdateElement> localMapList) {
		this.mContext = mContext;
		this.localMapList = localMapList;
	}

	@Override
	public int getCount() {
		return localMapList.size();
	}

	@Override
	public Object getItem(int index) {
		return localMapList.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@Override
	public View getView(int index, View view, ViewGroup arg2) {
		MKOLUpdateElement e = (MKOLUpdateElement) getItem(index);
		view = View.inflate(mContext, R.layout.offline_localmap_list, null);
		initViewItem(view, e);
		return view;
	}

	void initViewItem(View view, final MKOLUpdateElement e) {
		FancyButton display = (FancyButton) view.findViewById(R.id.display);
		FancyButton remove = (FancyButton) view.findViewById(R.id.remove);
		TextView title = (TextView) view.findViewById(R.id.title);
		TextView update = (TextView) view.findViewById(R.id.update);
		TextView ratio = (TextView) view.findViewById(R.id.ratio);
		ratio.setText(e.ratio + "%");
		title.setText(e.cityName);
		if (e.update) {
			update.setText("可更新");
		} else {
			update.setText("最新");
		}
		if (e.ratio != 100) {
			display.setEnabled(false);
		} else {
			display.setEnabled(true);
		}
		remove.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				listener.onRemove(e.cityID);
			}
		});
		display.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
	}

	OnLocalMapListener listener;

	public interface OnLocalMapListener {
		public void onRemove(int cityId);
	}

	public void setOnLocalMapListener(OnLocalMapListener listener) {
		this.listener = listener;
	}

}
