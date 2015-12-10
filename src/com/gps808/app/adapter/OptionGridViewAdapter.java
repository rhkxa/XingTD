package com.gps808.app.adapter;

import java.util.ArrayList;

import com.gps808.app.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OptionGridViewAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<String> dataList;
	private int SelectPosition=-1;

	public OptionGridViewAdapter(Context context, ArrayList<String> dataList) {
		this.context = context;

		this.dataList = dataList;
	}
	public void setSelectPosition(int position){
		SelectPosition=position;
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
					R.layout.item_option_gridview, null);
			vh.item_options_gridview_textview = (TextView) arg1
					.findViewById(R.id.item_options_gridview_textview);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}

		if(arg0==SelectPosition){
			vh.item_options_gridview_textview.setSelected(true);
		}else{
			vh.item_options_gridview_textview.setSelected(false);
		}
		return arg1;
	}

	class ViewHolder {
		TextView item_options_gridview_textview;

	}

}
