package com.gps808.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gps808.app.R;
import com.gps808.app.adapter.ShoppingListViewAdapter.OnItemClickListener;
import com.gps808.app.bean.BnOrder;
import com.gps808.app.bean.BnShopping;
import com.gps808.app.view.FancyButton;

public class OrderGroupListViewAdapter extends BaseAdapter {
	private Context context;
	private List<BnOrder> data;
	private OrderChildListViewAdapter mAdapter;

	public OrderGroupListViewAdapter(Context context, List<BnOrder> data) {
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
					R.layout.item_order_group_list, null);
			vh.childListView = (ListView) arg1
					.findViewById(R.id.item_order_child_list);

			vh.item_order_group_ok = (FancyButton) arg1
					.findViewById(R.id.item_order_group_ok);
			vh.item_order_group_del = (FancyButton) arg1
					.findViewById(R.id.item_order_group_del);
			vh.item_order_group_comment = (FancyButton) arg1
					.findViewById(R.id.item_order_group_comment);
			vh.order_id = (TextView) arg1.findViewById(R.id.order_id);
			vh.order_time = (TextView) arg1.findViewById(R.id.order_time);
			vh.order_total = (TextView) arg1.findViewById(R.id.order_total);
			vh.order_state = (TextView) arg1.findViewById(R.id.order_state);
			vh.order_total_money = (TextView) arg1
					.findViewById(R.id.order_total_money);
			vh.childListView = (ListView) arg1
					.findViewById(R.id.item_order_child_list);

			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}
		mAdapter = new OrderChildListViewAdapter(context, data.get(arg0)
				.getItems());
		vh.childListView.setAdapter(mAdapter);
		vh.order_total_money.setText("￥" + data.get(arg0).getAmount());
		vh.order_total.setText("共：" + data.get(arg0).getCount() + "件 实付：");
		if (data.get(arg0).getOrder_status().equals("1")) {
			vh.item_order_group_ok.setVisibility(View.VISIBLE);
			vh.item_order_group_del.setVisibility(View.GONE);
		} else {
			vh.item_order_group_ok.setVisibility(View.GONE);
			vh.item_order_group_del.setVisibility(View.VISIBLE);
		}
		vh.item_order_group_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				mOnItemClickListener.onReceive(arg0);
			}
		});
		vh.item_order_group_del.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				mOnItemClickListener.onDelete(arg0);
			}
		});

		// "order_status": 1, 订单状态0未确认,1确认,2已取消,3无效,4退货 
		// "shipping_status": 0, 商品配送情况;0未发货,1已发货,2已收货,3没有4退货
		// "pay_status": 0, 支付状态;0未付款;1付款中;2已付款
		String oState = "";
//		switch (data.get(arg0).getShipping_status()) {
//		case "0":
//			oState = "商品未发货";
//			break;
//		case "1":
//			oState = "商品已发货";
//			break;
//		case "2":
//			oState = "商品已收货";
//			break;
//		case "4":
//			oState = "商品已退货";
//			break;
//		}
		vh.order_state.setText(oState);
		return arg1;
	}

	class ViewHolder {
		ListView childListView;
		FancyButton item_order_group_ok;
		FancyButton item_order_group_comment;
		FancyButton item_order_group_del;
		ImageView item_order_group_phone;
		LinearLayout item_order_group_call;
		TextView order_id;
		TextView order_time;
		TextView order_total;
		TextView order_total_money;
		TextView order_state;

	}
	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}
	public interface OnItemClickListener {
		public void onDelete(int position);

		public void onReceive(int position);
	}


}
