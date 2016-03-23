package com.gps808.app.adapter;

import java.util.List;

import com.gps808.app.R;
import com.gps808.app.models.XbMap;
import com.gps808.app.models.XbMapCityBean;
import com.gps808.app.models.XbMap.Flag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class AllMapExpanListAdapter extends BaseExpandableListAdapter {

	private List<XbMapCityBean> datalist;
	private Context mContext;

	public AllMapExpanListAdapter(Context mContext, List<XbMapCityBean> datalist) {
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
		final ChildViewHolder vh;
		if (arg3 == null) {
			arg3 = LayoutInflater.from(mContext).inflate(
					R.layout.item_map_list, null);
			vh = new ChildViewHolder(arg3);
			arg3.setTag(vh);
		} else {
			vh = (ChildViewHolder) arg3.getTag();
		}
		XbMap bean = datalist.get(arg0).getCity().get(arg1);
		vh.item_map_name.setText(bean.getName());
		vh.item_map_size.setText(bean.getSize());
		int progress = bean.getProgress();
		String progressMsg = "";
		// 根据进度情况，设置显示
		if (progress == 0) {
			progressMsg = "";
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
		vh.item_map_download.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});

		return arg3;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return datalist.get(arg0).getCity().size();
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
		final GroupViewHolder vh;
		if (arg2 == null) {
			arg2 = LayoutInflater.from(mContext).inflate(
					R.layout.item_map_group, null);
			vh = new GroupViewHolder(arg2);
			arg2.setTag(vh);
		} else {
			vh = (GroupViewHolder) arg2.getTag();
		}
		if(arg1){
			vh.item_map_arrow.setImageResource(R.drawable.ssdk_recomm_plats_less);
		}else{
			vh.item_map_arrow.setImageResource(R.drawable.ssdk_recomm_plats_more);
		}
		vh.item_map_group_name.setText(datalist.get(arg0).getName());
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

		TextView item_map_name;
		TextView item_map_state;
		TextView item_map_size;
		ImageView item_map_download;

		public ChildViewHolder(View arg1) {

			item_map_name = (TextView) arg1.findViewById(R.id.item_map_name);
			item_map_state = (TextView) arg1.findViewById(R.id.item_map_state);
			item_map_size = (TextView) arg1.findViewById(R.id.item_map_size);
			item_map_download = (ImageView) arg1
					.findViewById(R.id.item_map_download);
		}

	}

	class GroupViewHolder {
		TextView item_map_group_name;
		ImageView item_map_arrow;

		public GroupViewHolder(View arg1) {
			item_map_group_name = (TextView) arg1
					.findViewById(R.id.item_map_group_name);
			item_map_arrow = (ImageView) arg1.findViewById(R.id.item_map_arrow);
		}
	}
}
