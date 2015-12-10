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
import com.gps808.app.adapter.CommentListViewAdapter.ViewHolder;
import com.gps808.app.bean.BnBill;

public class BillListViewAdapter extends BaseAdapter {
	private Context mContext;
	private List<BnBill> datalist;

	public BillListViewAdapter(Context mContext, List<BnBill> datalist) {
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
					R.layout.item_bill_list, null);
			vh = new ViewHolder(arg1);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}
		vh.item_bill_name.setText(datalist.get(arg0).getNickname());
		vh.item_bill_num.setText(datalist.get(arg0).getIncome());
		vh.item_bill_state.setText(datalist.get(arg0).getOperation());

		return arg1;
	}

	class ViewHolder {

		TextView item_bill_name;
		TextView item_bill_state;
		TextView item_bill_num;

		public ViewHolder(View arg1) {

			item_bill_name = (TextView) arg1.findViewById(R.id.item_bill_name);
			item_bill_state = (TextView) arg1
					.findViewById(R.id.item_bill_state);
			item_bill_num = (TextView) arg1.findViewById(R.id.item_bill_num);
		}
	}

}
