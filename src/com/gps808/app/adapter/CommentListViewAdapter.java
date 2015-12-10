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
import com.gps808.app.bean.BnComment;
import com.gps808.app.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CommentListViewAdapter extends BaseAdapter {
	private Context mContext;
	private List<BnComment> datalist;

	public CommentListViewAdapter(Context mContext, List<BnComment> datalist) {
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
					R.layout.item_comment_list, null);
			vh.item_comment_image = (ImageView) arg1
					.findViewById(R.id.item_comment_image);
			vh.item_comment_content = (TextView) arg1
					.findViewById(R.id.item_comment_content);
			vh.item_comment_name = (TextView) arg1
					.findViewById(R.id.item_comment_name);
			vh.item_comment_time = (TextView) arg1
					.findViewById(R.id.item_comment_time);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}
		vh.item_comment_content.setText(datalist.get(arg0).getContent());
		vh.item_comment_name.setText(datalist.get(arg0).getNickname());
		vh.item_comment_time.setText(StringUtils.toDate(datalist.get(arg0).getAddTime()));
		ImageLoader.getInstance().displayImage(datalist.get(arg0).getFace(), vh.item_comment_image);
	
		return arg1;
	}

	class ViewHolder {
		ImageView item_comment_image;
		TextView item_comment_content;
		TextView item_comment_name;
		TextView item_comment_time;

	}
}
