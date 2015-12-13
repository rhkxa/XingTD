package com.gps808.app.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gps808.app.R;
import com.gps808.app.bean.BnRank;
import com.gps808.app.bean.XbVehicle;
import com.nostra13.universalimageloader.core.ImageLoader;



import java.util.List;



public class VehicleListAdapter extends BaseAdapter {
	private Context mContext;
	private List<XbVehicle> datalist;

	public VehicleListAdapter(Context mContext, List<XbVehicle> datalist) {
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
					R.layout.item_ranking_list, null);
			vh = new ViewHolder(arg1);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}

//		ImageLoader.getInstance().displayImage(datalist.get(arg0).getFace(),
//				vh.item_ranking_headimg);
//		vh.item_ranking_money.setText("月销售额："+datalist.get(arg0).getMonthly_icome()+"万");
//		vh.item_ranking_name.setText(datalist.get(arg0).getNickname());
//		vh.item_ranking_num.setText("NO."+datalist.get(arg0).getRank());
		

		return arg1;
	}

	class ViewHolder {
		ImageView item_ranking_headimg;
		TextView item_ranking_name;
		TextView item_ranking_money;
		TextView item_ranking_num;

		public ViewHolder(View arg1) {
//			item_ranking_headimg = (ImageView) arg1
//					.findViewById(R.id.item_ranking_headimg);
//			item_ranking_name = (TextView) arg1
//					.findViewById(R.id.item_ranking_name);
			item_ranking_money = (TextView) arg1
					.findViewById(R.id.item_ranking_money);
			item_ranking_num = (TextView) arg1
					.findViewById(R.id.item_ranking_num);
		}

	}

}

