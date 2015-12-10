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
import com.gps808.app.bean.BnShopping;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FinishOrderListViewAdapter extends BaseAdapter {
	private Context context;
	private List<BnShopping> data;

	public FinishOrderListViewAdapter(Context context, List<BnShopping> data) {
		this.context = context;
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
		ViewHolder vh;
		if (arg1 == null) {
			vh = new ViewHolder();
			arg1 = LayoutInflater.from(context).inflate(
					R.layout.item_order_list, null);
			vh.item_goods_image = (ImageView) arg1
					.findViewById(R.id.item_goods_image);
			vh.item_goods_introduce = (TextView) arg1
					.findViewById(R.id.item_goods_introduce);
			vh.item_goods_price = (TextView) arg1
					.findViewById(R.id.item_goods_price);
			vh.item_goods_number = (TextView) arg1
					.findViewById(R.id.item_goods_number);
			vh.item_goods_parameters = (TextView) arg1
					.findViewById(R.id.item_goods_parameters);

			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}
		ImageLoader.getInstance().displayImage(data.get(arg0).getImg_url(),
				vh.item_goods_image);
		vh.item_goods_introduce.setText(data.get(arg0).getName());
		vh.item_goods_price.setText("￥" + data.get(arg0).getPrice());
		vh.item_goods_number.setText("×" + data.get(arg0).getQuantity());
		if (data.get(arg0).getSpecs().size() > 0) {
			vh.item_goods_parameters.setText(data.get(arg0).getSpecs().get(0)
					.getSpec_name()
					+ data.get(arg0).getSpecs().get(0).getSpec_value());
		}

		return arg1;
	}

	class ViewHolder {
		ImageView item_goods_image;
		TextView item_goods_introduce;
		TextView item_goods_price;
		TextView item_goods_number;
		TextView item_goods_parameters;
	}
}
