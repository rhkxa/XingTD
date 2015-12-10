package com.gps808.app.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.adapter.RankListViewAdapter;
import com.gps808.app.bean.BnRank;
import com.gps808.app.bean.RankBean;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;

/**
 * 加盟商排名
 * 
 * @author yourname
 * 
 */
public class FranchiseeActivity extends BaseActivity {

	private ListView franchisee_list;
	private RankListViewAdapter mAdapter;
	private List<BnRank> data = new ArrayList<BnRank>();
	private HeaderFragment headerFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_franchisee);
		init();
		loadData();
	}

	private void init() {
		// TODO Auto-generated method stub
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("加盟商排名");
		franchisee_list = (ListView) findViewById(R.id.franchisee_list);
		mAdapter = new RankListViewAdapter(FranchiseeActivity.this, data);
		franchisee_list.setAdapter(mAdapter);

	}

	private void loadData() {

//		HttpUtil.get(UrlConfig.getAgentRank(), new jsonHttpResponseHandler() {
//			@Override
//			public void onSuccess(JSONObject arg0) {
//				// TODO Auto-generated method stub
//				RankBean bill = JSON.parseObject(Utils.getResult(arg0),
//						RankBean.class);
//				setValue(bill);
//				super.onSuccess(arg0);
//			}
//
//		});
	}

	private void setValue(RankBean bill) {
		// TODO Auto-generated method stub
		data.addAll(bill.getDatas());
		mAdapter.notifyDataSetChanged();
	}
}
