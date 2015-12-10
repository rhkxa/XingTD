package com.gps808.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gps808.app.R;
import com.gps808.app.bean.BnHGoods;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AreaGoodsBannerAdapter extends BaseAdapter {
	private Context context;
	private List<BnHGoods> data;

	public AreaGoodsBannerAdapter(Context context, List<BnHGoods> data) {
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
					R.layout.item_active_image, null);
			vh.item_active_img1 = (ImageView) arg1
					.findViewById(R.id.item_active_img1);

			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}
		ImageLoader.getInstance().displayImage(data.get(arg0).getImg_url(),
				vh.item_active_img1);
		return arg1;
	}

	class ViewHolder {
		ImageView item_active_img1;

	}

}
