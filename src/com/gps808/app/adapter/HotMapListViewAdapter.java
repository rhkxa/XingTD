package com.gps808.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.gps808.app.R;
import com.gps808.app.models.XbMap;
import com.gps808.app.models.XbMapCityBean;
import com.gps808.app.models.XbMap.Flag;

public class HotMapListViewAdapter extends BaseAdapter {
	private Context mContext;
	private List<XbMap> datalist;

	public HotMapListViewAdapter(Context mContext, List<XbMap> datalist) {
		this.mContext = mContext;
		this.datalist = datalist;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datalist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		final ViewHolder vh;
		if (arg1 == null) {
			arg1 = LayoutInflater.from(mContext).inflate(
					R.layout.item_map_list, null);
			vh = new ViewHolder(arg1);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}
		XbMap bean = datalist.get(arg0);
		vh.item_map_name.setText(bean.getName());
		vh.item_map_size.setText(bean.getSize());
		int progress = bean.getProgress();
		String progressMsg = "";
		// 根据进度情况，设置显示
		if (progress == 0) {
			progressMsg = "未下载";
		} else if (progress == 100) {
			bean.setFlag(Flag.NO_STATUS);
			progressMsg = "已下载";
		} else {
			progressMsg = progress + "%";
		}
		// 根据当前状态，设置显示
		switch (bean.getFlag()) {
		case PAUSE:
			progressMsg += "【等待下载】";
			break;
		case DOWNLOADING:
			progressMsg += "【正在下载】";
			break;
		default:
			break;
		}
		vh.item_map_state.setText(progressMsg);

		return arg1;
	}

	class ViewHolder {

		TextView item_map_name;
		TextView item_map_state;
		TextView item_map_size;
		ImageView item_map_download;

		public ViewHolder(View arg1) {

			item_map_name = (TextView) arg1.findViewById(R.id.item_map_name);
			item_map_state = (TextView) arg1.findViewById(R.id.item_map_state);
			item_map_size = (TextView) arg1.findViewById(R.id.item_map_size);
			item_map_download = (ImageView) arg1
					.findViewById(R.id.item_map_download);
		}

	}

	RemoteMapListener remoteMapListener;

	public interface RemoteMapListener {
		public void onStart(int cityid);

		public void onStop(int cityid);
	}

	public void setRemoteMapListener(RemoteMapListener listener) {
		remoteMapListener = listener;
	}

}
