package com.gps808.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gps808.app.R;
import com.gps808.app.bean.BnGoods;
import com.gps808.app.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;


public class GoodsGridViewAdapter extends BaseAdapter {
	private Context context;
	private List<BnGoods> data;

	public GoodsGridViewAdapter(Context context, List<BnGoods> data) {
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
			
			arg1 = LayoutInflater.from(context).inflate(
					R.layout.item_goods_grid, null);
			vh = new ViewHolder(arg1);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}
		ImageLoader.getInstance().displayImage(data.get(arg0).getImg_url(),vh.item_goods_image);
		vh.item_goods_introduce.setText(data.get(arg0).getName());
		vh.item_goods_parameters.setText(data.get(arg0).getSpec());
		vh.item_goods_price.setText("￥"+data.get(arg0).getPrice()+"元");
		vh.item_goods_join.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Utils.joinShop(context, data.get(arg0).getProduct_id(), 1);
			}
		});
		return arg1;
	}

	class ViewHolder {
		ImageView item_goods_image;
		TextView item_goods_introduce;
		TextView item_goods_parameters;
		TextView item_goods_price;
		ImageView item_goods_join;
		public ViewHolder(View view){
			item_goods_image=(ImageView) view.findViewById(R.id.item_goods_image);
			item_goods_introduce=(TextView) view.findViewById(R.id.item_goods_introduce);
			item_goods_parameters=(TextView) view.findViewById(R.id.item_goods_parameters);
			item_goods_price=(TextView) view.findViewById(R.id.item_goods_price);
			item_goods_join=(ImageView) view.findViewById(R.id.item_goods_join);
					
		}

	}

}
