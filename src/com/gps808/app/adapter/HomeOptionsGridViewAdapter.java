package com.gps808.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gps808.app.R;

public class HomeOptionsGridViewAdapter extends BaseAdapter {
	private Context context;
	private String[] text = { "活动专区", "最新上架", "限时抢购", "我的订单" };
	private Integer[] icon = { R.drawable.yz_home_option1,
			R.drawable.yz_home_option2, R.drawable.yz_home_option3,
			R.drawable.yz_home_option4 };

	public HomeOptionsGridViewAdapter(Context context) {
		this.context = context;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return text.length;
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
					R.layout.item_home_options_gridview, null);
			vh.item_home_options_gridview_imageview = (ImageView) arg1
					.findViewById(R.id.item_home_options_gridview_imageview);
			vh.item_home_options_gridview_textview = (TextView) arg1
					.findViewById(R.id.item_home_options_gridview_textview);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}
		vh.item_home_options_gridview_imageview.setImageResource(icon[arg0]);
		vh.item_home_options_gridview_textview.setText(text[arg0]);

		return arg1;
	}

	class ViewHolder {
		TextView item_home_options_gridview_textview;
		ImageView item_home_options_gridview_imageview;
	}
}
