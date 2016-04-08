package com.gps808.app.adapter;

import java.util.List;

import com.gps808.app.R;
import com.gps808.app.models.RoutesInfo;
import com.gps808.app.view.FancyButton;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RoutesExpandableListAdapter extends BaseExpandableListAdapter {
	private Context mContext;
	private List<RoutesInfo> datalist;

	public RoutesExpandableListAdapter(Context mContext,
			List<RoutesInfo> datalist) {
		this.mContext = mContext;
		this.datalist = datalist;
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {
		// TODO Auto-generated method stub
		ChildViewHolder vh;
		if (arg3 == null) {

			arg3 = LayoutInflater.from(mContext).inflate(
					R.layout.item_routes_list, null);
			vh = new ChildViewHolder(arg3);
			arg3.setTag(vh);
		} else {
			vh = (ChildViewHolder) arg3.getTag();
		}

		// ImageLoader.getInstance().displayImage(datalist.get(arg0).getFace(),
		// vh.item_ranking_headimg);
		vh.item_routes_name.setText(datalist.get(arg0).getSub().get(arg1)
				.getRouteName());
		vh.item_routes_time.setText(datalist.get(arg0).getSub().get(arg1)
				.getTime());
		vh.item_routes_start.setText(datalist.get(arg0).getSub().get(arg1)
				.getStartPlace());
		vh.item_routes_end.setText(datalist.get(arg0).getSub().get(arg1)
				.getEndPlace());

		return arg3;

	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return datalist.get(arg0).getSub().size();
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return datalist.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		// TODO Auto-generated method stub
		GroupViewHolder vh;
		if (arg2 == null) {

			arg2 = LayoutInflater.from(mContext).inflate(
					R.layout.item_routes_group, null);
			vh = new GroupViewHolder(arg2);
			arg2.setTag(vh);
		} else {
			vh = (GroupViewHolder) arg2.getTag();
		}
		if (arg1) {
			vh.item_routes_group_ivExpande
					.setImageResource(R.drawable.ssdk_recomm_plats_less);
		} else {
			vh.item_routes_group_ivExpande
					.setImageResource(R.drawable.ssdk_recomm_plats_more);
		}

		vh.item_routes_group_name.setText(datalist.get(arg0).getGroupName());

		return arg2;

	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}
	

	class ChildViewHolder {
		FancyButton item_routes_image;
		TextView item_routes_name;
		TextView item_routes_time;
		TextView item_routes_start;
		TextView item_routes_end;

		public ChildViewHolder(View arg1) {
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

	class GroupViewHolder {

		TextView item_routes_group_name;
		ImageView item_routes_group_ivExpande;

		public GroupViewHolder(View arg1) {
			item_routes_group_name = (TextView) arg1
					.findViewById(R.id.item_routes_group_name);
			item_routes_group_ivExpande = (ImageView) arg1
					.findViewById(R.id.item_routes_group_ivExpande);
		}

	}

}
