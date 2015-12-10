package com.gps808.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gps808.app.R;
import com.gps808.app.adapter.HomeActiveListViewAdapter.ViewHolder;
import com.gps808.app.bean.BnHActive;
import com.gps808.app.bean.BnHGoods;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HomeGoodsListViewAdapter extends BaseAdapter {
	private Context context;
	private List<List<BnHGoods>> data;

	public HomeGoodsListViewAdapter(Context context, List<List<BnHGoods>> data) {
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
		final ViewHolder vh;
		if (arg1 == null) {
			vh = new ViewHolder();
			arg1 = LayoutInflater.from(context).inflate(
					R.layout.item_goods_image, null);
			vh.item_goods_image1 = (ImageView) arg1
					.findViewById(R.id.item_goods_image1);
			vh.item_goods_image2 = (ImageView) arg1
					.findViewById(R.id.item_goods_image2);
			vh.item_goods_image3 = (ImageView) arg1
					.findViewById(R.id.item_goods_image3);
			vh.item_goods_image4 = (ImageView) arg1
					.findViewById(R.id.item_goods_image4);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}

		ImageLoader.getInstance().displayImage(
				data.get(arg0).get(0).getImg_url(), vh.item_goods_image2);
		ImageLoader.getInstance().displayImage(
				data.get(arg0).get(1).getImg_url(), vh.item_goods_image3);
		ImageLoader.getInstance().displayImage(
				data.get(arg0).get(2).getImg_url(), vh.item_goods_image4);
		ImageLoader.getInstance().displayImage(
				data.get(arg0).get(3).getImg_url(), vh.item_goods_image1);
		
		return arg1;
	}

	class ViewHolder {
		ImageView item_goods_image1;
		ImageView item_goods_image2;
		ImageView item_goods_image3;
		ImageView item_goods_image4;
	}

}