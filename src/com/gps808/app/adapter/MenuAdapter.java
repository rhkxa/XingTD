package com.gps808.app.adapter;

import java.util.List;

import com.gps808.app.R;
import com.gps808.app.bean.BnMCate;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class MenuAdapter extends BaseAdapter {

	private Context context;
	private List<BnMCate> data;

	public MenuAdapter(Context context, List<BnMCate> data) {
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
					R.layout.item_grid_menu, null);
			vh.img = (ImageView) arg1.findViewById(R.id.image_view);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}

		ImageLoader.getInstance().displayImage(data.get(arg0).getImg_url(),
				vh.img);

		return arg1;
	}

	class ViewHolder {
		ImageView img;

	}

}
