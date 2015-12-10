package com.gps808.app.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public class SearchResultActivity extends BaseActivity {
	private ImageButton backBtn;
	private TextView titleText;
	private GoodsGridViewAdapter mAdapter;
	private GridView mGridView;
	private PullToRefreshGridView search_result_gridview;
	private String keyword;
	private int pno = 0;
	private final int psize = 6;
	private List<BnGoods> data = new ArrayList<BnGoods>();
	private ImageView search_result_toshop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);
		init();
		loadData();
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
		keyword = getIntent().getStringExtra("keyword");
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText(keyword + "   ×");
		titleText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		search_result_toshop=(ImageView) findViewById(R.id.search_result_toshop);
		search_result_toshop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Utils.toShop(SearchResultActivity.this);
			}
		});
		search_result_gridview = (PullToRefreshGridView) findViewById(R.id.search_result_gridview);
		mGridView = search_result_gridview.getRefreshableView();
		mAdapter = new GoodsGridViewAdapter(SearchResultActivity.this, data);
		mGridView.setAdapter(mAdapter);
		search_result_gridview.setMode(Mode.PULL_FROM_END);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Utils.toGoodDetail(SearchResultActivity.this, data.get(arg2)
						.getId());
			}
		});
		search_result_gridview
				.setOnRefreshListener(new OnRefreshListener<GridView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<GridView> refreshView) {
						// TODO Auto-generated method stub
						loadData();
					}
				});
	}

	private void loadData() {
//		if (Utils.isNetWorkConnected(SearchResultActivity.this)) {
//			String url = UrlConfig.getSearch(keyword, psize, pno);
//			HttpUtil.get(url, new jsonHttpResponseHandler() {
//				@Override
//				public void onSuccess(JSONObject arg0) {
//					// TODO Auto-generated method stub
//					ProductBean product = JSON.parseObject(
//							Utils.getResult(arg0), ProductBean.class);
//					data.addAll(product.getDatas());
//					mAdapter.notifyDataSetChanged();
//					search_result_gridview.onRefreshComplete();
//					if (product.getDatas().size() < psize) {
//						search_result_gridview.setMode(Mode.DISABLED);
//					} else {
//						pno++;
//					}
//					super.onSuccess(arg0);
//				}
//
//				@Override
//				public void onFailure(Throwable arg0, JSONObject arg1) {
//					// TODO Auto-generated method stub
//					search_result_gridview.onRefreshComplete();
//					super.onFailure(arg0, arg1);
//				}
//			});
//		} else {
//			Utils.showSuperCardToast(SearchResultActivity.this, getResources()
//					.getString(R.string.network_not_connected));
//		}
	}
}
