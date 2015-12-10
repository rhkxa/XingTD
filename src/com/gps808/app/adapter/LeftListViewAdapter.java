package com.gps808.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gps808.app.R;


public class LeftListViewAdapter extends BaseAdapter {
	private Context context;
	private List<String> dataList;
	private int SelectPosition = 0;

	public LeftListViewAdapter(Context context, List<String> dataList) {
		this.context = context;

		this.dataList = dataList;
	}

	public void setSelectPosition(int position) {
		SelectPosition = position;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
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
			vh = new ViewHolder();
			arg1 = LayoutInflater.from(context).inflate(
					R.layout.item_left_list, null);
			vh.left_name = (TextView) arg1
					.findViewById(R.id.item_left_textview);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}

		if (arg0 == SelectPosition) {
			vh.left_name.setTextColor(context.getResources().getColor(
					R.color.tag_blue));
		} else {
			vh.left_name.setTextColor(context.getResources().getColor(
					R.color.black));
		}
		

		return arg1;
	}

	class ViewHolder {
		TextView left_name;

	}

}
