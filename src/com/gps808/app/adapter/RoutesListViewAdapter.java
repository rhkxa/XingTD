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
import com.gps808.app.bean.XbVehicle;

public class RoutesListViewAdapter extends BaseAdapter {
	private Context mContext;
	private List<XbVehicle> datalist;

	public RoutesListViewAdapter(Context mContext, List<XbVehicle> datalist) {
		this.mContext = mContext;
		this.datalist = datalist;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
//		return datalist.size();
		return 5;
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
					R.layout.item_routes_list, null);
			vh = new ViewHolder(arg1);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}

		// ImageLoader.getInstance().displayImage(datalist.get(arg0).getFace(),
		// vh.item_ranking_headimg);
		// vh.item_vehicle_name.setText(datalist.get(arg0).getPlateNo());
		// String status = "";
		// if (datalist.get(arg0).isOnline()) {
		// status = "在线";
		//
		// } else {
		// status = "离线";
		// }
		// vh.item_vehicle_status.setText(status);

		return arg1;
	}

	class ViewHolder {
		// ImageView item_vehicle_image;
		// TextView item_vehicle_name;
		// TextView item_vehicle_status;
		//
		public ViewHolder(View arg1) {
			// item_vehicle_image = (ImageView) arg1
			// .findViewById(R.id.item_vehicle_image);
			// // item_ranking_name = (TextView) arg1
			// // .findViewById(R.id.item_ranking_name);
			// item_vehicle_name = (TextView) arg1
			// .findViewById(R.id.item_vehicle_name);
			// item_vehicle_status = (TextView) arg1
			// .findViewById(R.id.item_vehicle_status);
		}

	}

}
