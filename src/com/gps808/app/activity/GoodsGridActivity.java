package com.gps808.app.activity;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.adapter.GoodsGridViewAdapter;
import com.gps808.app.bean.BnGoods;
import com.gps808.app.bean.ProductBean;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;

public class GoodsGridActivity extends BaseActivity {

	private ImageButton backBtn;
	private TextView goods_search_titleText;
	private GoodsGridViewAdapter mAdapter;
	private GridView mGridView;
	private PullToRefreshGridView active_gridview;
	private String cate_id;
	private String cate_name = "苹果";
	private int pno = 0;
	private final int psize = 10;
	private List<BnGoods> data = new ArrayList<BnGoods>();
	private ImageView goods_toshop;
	private String sales = "", price = "";
	private RadioButton item_sales, item_price;
	String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_grid);
		init();

	}

	private void init() {
		// TODO Auto-generated method stub
		backBtn = (ImageButton) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		cate_id = getIntent().getStringExtra("cate_id");
		cate_name = getIntent().getStringExtra("cate_name");
		goods_search_titleText = (TextView) findViewById(R.id.goods_search_titleText);
		goods_search_titleText.setText(cate_name + "   ×");
		goods_toshop = (ImageView) findViewById(R.id.goods_toshop);
		goods_toshop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Utils.toShop(GoodsGridActivity.this);
			}
		});
		active_gridview = (PullToRefreshGridView) findViewById(R.id.goods_gridview);
		mGridView = active_gridview.getRefreshableView();
		mAdapter = new GoodsGridViewAdapter(GoodsGridActivity.this, data);
		mGridView.setAdapter(mAdapter);
		active_gridview.setMode(Mode.PULL_FROM_END);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GoodsGridActivity.this,
						GoodsDetailActivity.class);
				intent.putExtra("goods_id", data.get(arg2).getId());
				startActivity(intent);
			}
		});
		active_gridview.setOnRefreshListener(new OnRefreshListener<GridView>() {

			@Override
			public void onRefresh(PullToRefreshBase<GridView> refreshView) {
				// TODO Auto-generated method stub
				loadData(url);
		
			}
		});// 销量▽价格△

		item_sales = (RadioButton) findViewById(R.id.item_sales);
		item_price = (RadioButton) findViewById(R.id.item_price);
		item_sales.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				price = "";
				item_price.setText("价格△");
				active_gridview.setMode(Mode.PULL_FROM_END);
				pno=0;
				data.clear();
				if (sales.equals("desc")) {
					sales = "asc";
					item_sales.setText("销量△");
				} else {
					sales = "desc";
					item_sales.setText("销量▽");
				}
				url = UrlConfig
						.getGoodsList(cate_id, "", psize, pno, sales, "");
				loadData(url);
			}
		});
		item_price.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sales="";
				item_sales.setText("销量▽");
				active_gridview.setMode(Mode.PULL_FROM_END);
				pno=0;
				data.clear();
				if (price.equals("asc")) {
					price = "desc";
					item_price.setText("价格▽");
				} else {
					price = "asc";
					item_price.setText("价格△");
				}
				url = UrlConfig
						.getGoodsList(cate_id, "", psize, pno, sales, price);
				loadData(url);
			}
		});
		url = UrlConfig.getGoodsList(cate_id, "", psize, pno, sales, price);
		loadData(url);
	}

	private void loadData(String turl) {
		
//		if (Utils.isNetWorkConnected(GoodsGridActivity.this)) {
//			String url=UrlConfig.getGoodsList(cate_id, "", psize, pno, sales, price);
//			HttpUtil.get(url, new jsonHttpResponseHandler() {
//				@Override
//				public void onSuccess(JSONObject arg0) {
//					// TODO Auto-generated method stub
//					ProductBean product = JSON.parseObject(
//							Utils.getResult(arg0), ProductBean.class);					
//					data.addAll(product.getDatas());
//					mAdapter.notifyDataSetChanged();
//					active_gridview.onRefreshComplete();
//					if (product.getDatas().size() < psize) {
//						active_gridview.setMode(Mode.DISABLED);
//						Utils.ToastMessage(GoodsGridActivity.this, "已加载全部");
//					} else {
//						pno++;
//					}
//					super.onSuccess(arg0);
//				}
//
//				@Override
//				public void onFailure(Throwable arg0, JSONObject arg1) {
//					// TODO Auto-generated method stub
//					active_gridview.onRefreshComplete();
//					super.onFailure(arg0, arg1);
//				}
//			});
//		} else {
//			Utils.showSuperCardToast(GoodsGridActivity.this, getResources()
//					.getString(R.string.network_not_connected));
//		}
	}
}
