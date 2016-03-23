package com.gps808.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gps808.app.R;
import com.gps808.app.models.XbPolice;
import com.gps808.app.models.XbVehicle;
import com.gps808.app.view.FancyButton;

public class PoliceListViewAdapter extends BaseAdapter {
	private Context mContext;
	private List<XbPolice> datalist;

	public PoliceListViewAdapter(Context mContext, List<XbPolice> datalist) {
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
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder vh;
		if (arg1 == null) {

			arg1 = LayoutInflater.from(mContext).inflate(
					R.layout.item_police_list, null);
			vh = new ViewHolder(arg1);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}

		// ImageLoader.getInstance().displayImage(datalist.get(arg0).getFace(),
		// vh.item_ranking_headimg);
		vh.item_police_addr.setText(datalist.get(arg0).getPlateNo());
		vh.item_police_name.setText(datalist.get(arg0).getAlarmName());
		vh.item_police_time.setText(datalist.get(arg0).getTime());

		return arg1;
	}

	class ViewHolder {
		FancyButton item_police_image;
		TextView item_police_name;
		TextView item_police_time;
		TextView item_police_addr;

		public ViewHolder(View arg1) {
			item_police_image = (FancyButton) arg1
					.findViewById(R.id.item_police_image);
			item_police_name = (TextView) arg1
					.findViewById(R.id.item_police_name);
			item_police_time = (TextView) arg1
					.findViewById(R.id.item_police_time);
			item_police_addr = (TextView) arg1
					.findViewById(R.id.item_police_addr);
		}

	}

}
