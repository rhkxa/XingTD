package com.gps808.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.gps808.app.R;
import com.gps808.app.adapter.ShoppingListViewAdapter.OnItemClickListener;
import com.gps808.app.bean.BnGoods;
import com.gps808.app.bean.BnShopping;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CollectListViewAdapter extends BaseAdapter {
	private Context context;
	private List<BnGoods> data;
	private List<BnGoods> selectdata;
	private boolean isEdit;

	public CollectListViewAdapter(Context context, List<BnGoods> data,List<BnGoods> selectdata) {
		this.context = context;
		this.data = data;
		this.selectdata = selectdata;
       
	}
	public void setEdit(boolean isEdit){
		 this.isEdit=isEdit;
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
					R.layout.item_collet_list, null);
			vh.item_collect_checkbox=(CheckBox) arg1.findViewById(R.id.item_collect_checkbox);
			vh.item_collect_list_introduce = (TextView) arg1
					.findViewById(R.id.item_collect_introduce);
			vh.item_collect_list_image = (ImageView) arg1
					.findViewById(R.id.item_collect_image);
			vh.item_collect_sales = (TextView) arg1
					.findViewById(R.id.item_collect_sales);
			vh.item_collect_list_new_price = (TextView) arg1
					.findViewById(R.id.item_collect_new_price);
			vh.item_collect_parameters = (TextView) arg1
					.findViewById(R.id.item_collect_parameters);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}
		if(isEdit){
			vh.item_collect_checkbox.setVisibility(View.VISIBLE);
			vh.item_collect_checkbox.setChecked(isInSelectedDataList(data.get(
					arg0).getProduct_id()));
		}else{
			vh.item_collect_checkbox.setVisibility(View.GONE);
		}
		vh.item_collect_checkbox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (data != null && mOnItemClickListener != null
						&& arg0 < data.size()) {
					mOnItemClickListener.onItemClick(vh.item_collect_checkbox,
							data.get(arg0),
							vh.item_collect_checkbox.isChecked());

				}
			}
		});
		
		vh.item_collect_list_introduce.setText(data.get(arg0).getName());
		vh.item_collect_parameters.setText(data.get(arg0).getSpec());
		vh.item_collect_sales.setText("销量：" + data.get(arg0).getBuy_count());
		vh.item_collect_list_new_price.setText("特惠价："
				+ data.get(arg0).getPrice());
		ImageLoader.getInstance().displayImage(data.get(arg0).getImg_url(),
				vh.item_collect_list_image);

		return arg1;
	}
	private boolean isInSelectedDataList(String selectedString) {
		for (int i = 0; i < selectdata.size(); i++) {
			if (selectdata.get(i).getProduct_id().equals(selectedString)) {
				return true;
			}
		}
		return false;
	}

	class ViewHolder {
		CheckBox item_collect_checkbox;
		ImageView item_collect_list_image;
		TextView item_collect_list_introduce;
		TextView item_collect_sign;
		TextView item_collect_parameters;
		TextView item_collect_sales;
		TextView item_collect_list_new_price;
	}
	
	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	public interface OnItemClickListener {
		public void onItemClick(CheckBox view, BnGoods goods,
				boolean isChecked);
	}

}
