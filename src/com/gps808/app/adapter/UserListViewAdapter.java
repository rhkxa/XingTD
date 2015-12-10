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
import com.gps808.app.bean.BnUser;
import com.gps808.app.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserListViewAdapter extends BaseAdapter {
	private Context mContext;
	private List<BnUser> datalist;

	public UserListViewAdapter(Context mContext, List<BnUser> datalist) {
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
			vh = new ViewHolder();
			arg1 = LayoutInflater.from(mContext).inflate(
					R.layout.item_user_list, null);
			vh.item_user_image = (ImageView) arg1
					.findViewById(R.id.item_user_image);
			vh.item_user_content = (TextView) arg1
					.findViewById(R.id.item_user_content);
			vh.item_user_name = (TextView) arg1
					.findViewById(R.id.item_user_name);
			vh.item_user_check = (ImageView) arg1
					.findViewById(R.id.item_user_check);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}
		vh.item_user_content.setText(datalist.get(arg0).getMobile());
		vh.item_user_name.setText(datalist.get(arg0).getNickname());
		if (true) {
			vh.item_user_check.setVisibility(View.VISIBLE);
		} else {
			vh.item_user_check.setVisibility(View.GONE);
		}

		ImageLoader.getInstance().displayImage(datalist.get(arg0).getFace(),
				vh.item_user_image);
		return arg1;
	}

	class ViewHolder {
		ImageView item_user_image;
		TextView item_user_content;
		TextView item_user_name;
		ImageView item_user_check;

	}
}
