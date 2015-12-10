package com.gps808.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.gps808.app.R;
import com.gps808.app.adapter.OrderChildListViewAdapter;
import com.gps808.app.bean.BnShopping;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;

public class OrderDetailActivity extends BaseActivity {

	private HeaderFragment headerFragment;
	private ListView order_details_list;
	private OrderChildListViewAdapter mAdapter;
	private TextView order_details_number;
	private TextView order_details_time;
	private TextView order_details_address_name;
	private TextView order_details_address_phone;
	private TextView order_details_address_details;
	private TextView order_details_total;
	private TextView order_details_total_money;
	private TextView order_details_phone;
	private TextView order_details_place_time;
	private TextView order_details_handle_time;
	private List<BnShopping> data = new ArrayList<BnShopping>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_details);
		init();
		setValue();
	}

	private void init() {
		// TODO Auto-generated method stub
	
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("订单详情");
		order_details_list = (ListView) findViewById(R.id.order_details_list);
		mAdapter = new OrderChildListViewAdapter(OrderDetailActivity.this, data);
		order_details_list.setAdapter(mAdapter);
		order_details_number = (TextView) findViewById(R.id.order_details_number);
		order_details_time = (TextView) findViewById(R.id.order_details_time);
		order_details_address_name = (TextView) findViewById(R.id.order_details_address_name);
		order_details_address_phone = (TextView) findViewById(R.id.order_details_address_phone);
		order_details_address_details = (TextView) findViewById(R.id.order_details_address_details);
		order_details_total = (TextView) findViewById(R.id.order_details_total);
		order_details_total_money = (TextView) findViewById(R.id.order_details_total_money);
		order_details_phone = (TextView) findViewById(R.id.order_details_phone);
		order_details_place_time = (TextView) findViewById(R.id.order_details_place_time);
		order_details_handle_time = (TextView) findViewById(R.id.order_details_handle_time);

	}

	private void setValue() {
//		order_details_time.setText("下单时间：" + groupOrder.getSubmitTime());
//		order_details_address_name.setText(groupOrder.getHyname());
//		order_details_address_phone.setText(groupOrder.getHytel());
//		order_details_address_details.setText(groupOrder.getHyaddress());
//		data.clear();
//		data.addAll(groupOrder.getOdList());
//		int size = data.size();
//		int total = 0;
//		double totalmoney = 0;
//		for (int i = 0; i < size; i++) {
//			totalmoney = totalmoney
//					+ Double.parseDouble(data.get(i).getOdActualAmount());
//			total = total + Integer.parseInt(data.get(i).getOdNumber());
//		}
//		order_details_total.setText("共" + total + "件");
//		order_details_total_money.setText("￥" + groupOrder.getActualAmount());
//		order_details_phone.setText(groupOrder.getKftel());
//		order_details_number.setText("货运单号：" + groupOrder.getId());
//		order_details_handle_time
//				.setText("平台确认时间：" + groupOrder.getInputTime());
//		order_details_place_time
//				.setText("用户下单时间：" + groupOrder.getSubmitTime());
	}

}
