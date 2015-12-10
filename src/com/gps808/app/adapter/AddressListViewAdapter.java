package com.gps808.app.adapter;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.activity.EditAddressActivity;
import com.gps808.app.bean.BnAddress;
import com.gps808.app.view.FancyButton;
import com.gps808.app.view.PengButton;

import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddressListViewAdapter extends BaseAdapter {
	private Context mContext;
	private List<BnAddress> data;

	public AddressListViewAdapter(Context mContext, List<BnAddress> data) {
		this.mContext = mContext;
		this.data = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
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
			arg1 = LayoutInflater.from(mContext).inflate(
					R.layout.item_address_list, null);
//			vh.item_address_default = (PengButton) arg1
//					.findViewById(R.id.item_address_default);
			vh.item_address_name = (TextView) arg1
					.findViewById(R.id.item_address_name);
			vh.item_address_phone = (TextView) arg1
					.findViewById(R.id.item_address_phone);
			vh.item_address_details = (TextView) arg1
					.findViewById(R.id.item_address_details);
			vh.item_address_details = (TextView) arg1
					.findViewById(R.id.item_address_details);
			vh.item_address_edit = (ImageView) arg1
					.findViewById(R.id.item_address_edit);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}
		vh.item_address_name.setText(data.get(arg0).getName());
		vh.item_address_phone.setText(data.get(arg0).getMobile());
		vh.item_address_details.setText(data.get(arg0).getFull_address());

		if (data.get(arg0).getIs_default().equals("1")) {
			vh.item_address_default.setVisibility(View.VISIBLE);
		} else {
			vh.item_address_default.setVisibility(View.GONE);
		}

		// vh.item_address_default.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View view) {
		// // TODO Auto-generated method stub
		// if (vh.item_address_default.isChecked()) {
		// mOnItemClickListener.onItemClick(arg0);
		// }
		// }
		// });

		// vh.item_address_delete.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View view) {
		// // TODO Auto-generated method stub
		// new AlertDialog.Builder(mContext)
		// .setTitle("删除地址")
		// .setMessage("确定要删除吗？")
		// .setPositiveButton("确定",
		// new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(
		// DialogInterface dialogInterface,
		// int arg1) {
		// // TODO Auto-generated method stub
		// mOnItemClickListener.onDelete(arg0);
		// }
		// })
		// .setNegativeButton("取消",
		// new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(
		// DialogInterface dialogInterface,
		// int arg1) {
		// // TODO Auto-generated method stub
		// dialogInterface.dismiss();
		// }
		// }).show();
		//
		// }
		// });
		vh.item_address_edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, EditAddressActivity.class);
				intent.putExtra("address", JSON.toJSONString(data.get(arg0)));
				mContext.startActivity(intent);
			}
		});
		return arg1;
	}

	class ViewHolder {
		PengButton item_address_default;
		TextView item_address_name;
		TextView item_address_phone;
		TextView item_address_details;
		LinearLayout item_address_delete;
		ImageView item_address_edit;

	}

	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	public interface OnItemClickListener {
		public void onItemClick(int position);

		public void onDelete(int position);

		public void onEdit(int position);
	}

}
