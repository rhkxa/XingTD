package com.gps808.app.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.adapter.FinishOrderListViewAdapter;
import com.gps808.app.bean.BnAddress;
import com.gps808.app.bean.BnShopping;
import com.gps808.app.bean.InitOrderBean;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.PreferenceUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;
import com.gps808.app.view.FancyButton;
import com.loopj.android.http.RequestParams;

public class FinishOrderActivity extends BaseActivity {

	private HeaderFragment headerFragment;
	private ListView order_details_list;
	private FinishOrderListViewAdapter mAdapter;
	private TextView order_details_address_name;
	private TextView order_details_address_phone;
	private TextView order_details_address_details;
	private TextView order_details_shop_fare;
	private TextView order_details_shop_price;
	private TextView order_details_shop_price_desc;
	private TextView shopping_all_money;
	
	private FancyButton shopping_ok;
	private LinearLayout address_layout;

	private List<BnShopping> dataList = new ArrayList<BnShopping>();
	private String products;
	private BnAddress address;
	private String uid;
	private InitOrderBean bean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finish_order);
		init();
		loadOrder();
	}

	private void init() {
		// TODO Auto-generated method stub
		
		products = getIntent().getStringExtra("products");
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("订单确定");
		order_details_list = (ListView) findViewById(R.id.order_details_list);
		mAdapter = new FinishOrderListViewAdapter(FinishOrderActivity.this,
				dataList);
		order_details_list.setAdapter(mAdapter);

		order_details_address_name = (TextView) findViewById(R.id.order_details_address_name);
		order_details_address_phone = (TextView) findViewById(R.id.order_details_address_phone);
		order_details_address_details = (TextView) findViewById(R.id.order_details_address_details);
		order_details_shop_price = (TextView) findViewById(R.id.order_details_shop_price);
		order_details_shop_fare = (TextView) findViewById(R.id.order_details_shop_fare);
		order_details_shop_price_desc = (TextView) findViewById(R.id.order_details_shop_price_desc);
		shopping_all_money = (TextView) findViewById(R.id.shopping_all_money);
		shopping_ok = (FancyButton) findViewById(R.id.shopping_ok);
		shopping_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (address == null) {
					Utils.ToastMessage(FinishOrderActivity.this, "请选择地址");
					return;
				}
				submitOrder();
			}
		});
		address_layout = (LinearLayout) findViewById(R.id.address_layout);
		address_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FinishOrderActivity.this,
						AddressActivity.class);
				intent.putExtra("get", true);
				startActivityForResult(intent, 0);
			}
		});
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		switch (arg1) {
		case RESULT_OK:
			address = JSON.parseObject(arg2.getStringExtra("toaddress"),
					BnAddress.class);
			order_details_address_name.setText(address.getName());
			order_details_address_phone.setText(address.getMobile());
			order_details_address_details.setText(address.getFull_address());
			break;

		}
		super.onActivityResult(arg0, arg1, arg2);
	}

	private void setValue() {
		dataList.addAll(bean.getItems());
		mAdapter.notifyDataSetChanged();
		if (bean.getAddress() != null) {
			address = bean.getAddress();
			order_details_address_name.setText(bean.getAddress().getName());
			order_details_address_phone.setText(bean.getAddress().getMobile());
			order_details_address_details.setText(bean.getAddress()
					.getFull_address());
		}
		order_details_shop_price_desc.setText(bean.getShipping().getDesc());
		order_details_shop_fare.setText("￥" + bean.getFreight());
		order_details_shop_price.setText("￥" + bean.getBill_amount());
		shopping_all_money.setText("￥" + bean.getAmount());
	}

	// 提交订单
	private void submitOrder() {
//		HttpUtil.get(UrlConfig.submitOrder(uid, bean.getSn(), address.getId()),
//				new jsonHttpResponseHandler() {
//					@Override
//					public void onSuccess(JSONObject arg0) {
//						// TODO Auto-generated method stub
//						if (Utils.requestOk(arg0)) {
//							Utils.toPay(FinishOrderActivity.this,bean.getSn(),"购物",
//									String.valueOf(bean.getAmount()));
//						}
//						super.onSuccess(arg0);
//					}
//				});

	}

	private void loadOrder() {
//		RequestParams params = new RequestParams();
//		params.put("uid", uid);
//		params.put("products", products);
//		LogUtils.d("iezone", products);
//		HttpUtil.post(UrlConfig.loadOrder(), params,
//				new jsonHttpResponseHandler() {
//					@Override
//					public void onSuccess(JSONObject arg0) {
//						// TODO Auto-generated method stub
//						bean = JSON.parseObject(Utils.getResult(arg0),
//								InitOrderBean.class);
//						setValue();
//						super.onSuccess(arg0);
//					}
//				});
	}

}
