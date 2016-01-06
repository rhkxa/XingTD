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
import com.gps808.app.view.FancyButton;


public class DriverListAdapter extends BaseAdapter {
	private Context mContext;
	private List<XbDriver> datalist;

	public DriverListAdapter(Context mContext, List<XbDriver> datalist) {
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
					R.layout.item_driver_list, null);
			vh = new ViewHolder(arg1);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}

		// ImageLoader.getInstance().displayImage(datalist.get(arg0).getFace(),
		// vh.item_ranking_headimg);
		vh.item_driver_name.setText(datalist.get(arg0).getDriverName());
		vh.item_driver_phone.setText(datalist.get(arg0).getPhone());

		return arg1;
	}

	class ViewHolder {
		FancyButton item_driver_image;
		TextView item_driver_name;
		TextView item_driver_phone;

		public ViewHolder(View arg1) {
			item_driver_image = (FancyButton) arg1
					.findViewById(R.id.item_driver_image);
			// item_ranking_name = (TextView) arg1
			// .findViewById(R.id.item_ranking_name);
			item_driver_name = (TextView) arg1
					.findViewById(R.id.item_driver_name);
			item_driver_phone = (TextView) arg1
					.findViewById(R.id.item_driver_phone);
		}

	}


}
