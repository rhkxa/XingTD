package com.gps808.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gps808.app.R;
import com.gps808.app.bean.BnCharge;

public class GitftListViewAdapter extends BaseAdapter {
	private Context context;
	private List<BnCharge> dataList;
	private int position = -1;

	public GitftListViewAdapter(Context context, List<BnCharge> dataList) {
		this.context = context;
		this.dataList = dataList;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getMoney() {
		// return dataList.get(position);
		if (position > -1) {
			return String.valueOf(dataList.get(position).getAmount());
		} else {
			return "";
		}

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		// return dataList.size();
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
		ViewHolder vh;
		if (arg1 == null) {
			vh = new ViewHolder();
			arg1 = LayoutInflater.from(context).inflate(
					R.layout.item_gift_list, null);

			vh.item_gift_check = (CheckBox) arg1
					.findViewById(R.id.item_gift_check);
			vh.item_gift_content = (TextView) arg1
					.findViewById(R.id.item_gift_content);

			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}

		vh.item_gift_content.setText(dataList.get(arg0).getName());
		vh.item_gift_check.setChecked(false);
		if (position == arg0) {
			vh.item_gift_check.setChecked(true);
		}
		vh.item_gift_check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				setPosition(arg0);
				notifyDataSetChanged();
			}
		});
		return arg1;
	}

	class ViewHolder {

		CheckBox item_gift_check;
		TextView item_gift_content;
	}

}
