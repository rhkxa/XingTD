package com.gps808.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.gps808.app.R;
import com.gps808.app.bean.XbMap;

public class RemoteMapListViewAdapter extends BaseAdapter {
	private Context mContext;
	private List<XbMap> datalist;

	public RemoteMapListViewAdapter(Context mContext, List<XbMap> datalist) {
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
		vh.item_map_name.setText(datalist.get(arg0).getName());
		vh.item_map_state.setText(datalist.get(arg0).getSize());
		vh.item_map_play
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton compoundButton,
							boolean arg1) {
						// TODO Auto-generated method stub
						if (arg1) {
							remoteMapListener.onStart(vh.item_map_state, datalist
									.get(arg0).getId());
						} else {
							remoteMapListener.onStop(vh.item_map_state, datalist
									.get(arg0).getId());
						}
					}
				});

		return arg1;
	}

	class ViewHolder {

		TextView item_map_name;
		TextView item_map_state;
		ToggleButton item_map_play;

		public ViewHolder(View arg1) {

			item_map_name = (TextView) arg1.findViewById(R.id.item_map_name);
			item_map_state = (TextView) arg1.findViewById(R.id.item_map_state);
			item_map_play = (ToggleButton) arg1
					.findViewById(R.id.item_map_play);
		}

	}

	RemoteMapListener remoteMapListener;
	public interface RemoteMapListener {
		public void onStart(TextView textView, int cityid);
		public void onStop(TextView textView, int cityid);
	}

	public void setRemoteMapListener(RemoteMapListener listener) {
		remoteMapListener = listener;
	}

}
