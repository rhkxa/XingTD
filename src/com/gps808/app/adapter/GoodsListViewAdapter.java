package com.gps808.app.adapter;

import java.util.List;

import com.gps808.app.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GoodsListViewAdapter extends BaseAdapter {
	private Context context;
	private List<String> dataList;

	public GoodsListViewAdapter(Context context, List<String> dataList) {
		this.context = context;
		this.dataList = dataList;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList == null ? 0 : dataList.size();
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
					R.layout.item_goods_list, null);
			vh.item_goods_list_introduce = (TextView) arg1
					.findViewById(R.id.item_goods_introduce);
			vh.item_goods_list_image = (ImageView) arg1
					.findViewById(R.id.item_goods_image);
			vh.item_goods_sign = (TextView) arg1
					.findViewById(R.id.item_goods_sign);
			vh.item_goods_list_old_price = (TextView) arg1
					.findViewById(R.id.item_goods_old_price);
			vh.item_goods_list_new_price = (TextView) arg1
					.findViewById(R.id.item_goods_new_price);
			vh.item_goods_parameters = (TextView) arg1
					.findViewById(R.id.item_goods_parameters);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}

		return arg1;
	}

	class ViewHolder {
		ImageView item_goods_list_image;
		TextView item_goods_list_introduce;
		TextView item_goods_sign;
		TextView item_goods_parameters;
		TextView item_goods_list_old_price;
		TextView item_goods_list_new_price;
	}

}
