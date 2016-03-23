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
import com.gps808.app.models.XbRoute;
import com.gps808.app.models.XbVehicle;
import com.gps808.app.view.FancyButton;

public class RoutesListViewAdapter extends BaseAdapter {
	private Context mContext;
	private List<XbRoute> datalist;

	public RoutesListViewAdapter(Context mContext, List<XbRoute> datalist) {
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
					R.layout.item_routes_list, null);
			vh = new ViewHolder(arg1);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}

		// ImageLoader.getInstance().displayImage(datalist.get(arg0).getFace(),
		// vh.item_ranking_headimg);
		vh.item_routes_name.setText(datalist.get(arg0).getRouteName());
		vh.item_routes_time.setText(datalist.get(arg0).getTime());
		vh.item_routes_start.setText(datalist.get(arg0).getStartPlace());
		vh.item_routes_end.setText(datalist.get(arg0).getEndPlace());

		return arg1;
	}

	class ViewHolder {
		FancyButton item_routes_image;
		TextView item_routes_name;
		TextView item_routes_time;
		TextView item_routes_start;
		TextView item_routes_end;

		public ViewHolder(View arg1) {
			item_routes_image = (FancyButton) arg1
					.findViewById(R.id.item_routes_image);
			item_routes_name = (TextView) arg1
					.findViewById(R.id.item_routes_name);
			item_routes_time = (TextView) arg1
					.findViewById(R.id.item_routes_time);
			item_routes_start = (TextView) arg1
					.findViewById(R.id.item_routes_start);
			item_routes_end = (TextView) arg1
					.findViewById(R.id.item_routes_end);
		}

	}

}
