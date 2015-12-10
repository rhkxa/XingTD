package com.gps808.app.activity;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.adapter.CollectListViewAdapter;
import com.gps808.app.adapter.CollectListViewAdapter.OnItemClickListener;
import com.gps808.app.bean.BnGoods;
import com.gps808.app.bean.CollectBean;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.PreferenceUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;
import com.gps808.app.view.FancyButton;

/**
 * 我的收藏的界面
 * 
 * @author rhk
 * 
 */
public class CollectActivity extends BaseActivity {

	private HeaderFragment headerFragment;
	private ListView collect_list;
	private CollectListViewAdapter mAdapter;
	private List<BnGoods> data = new ArrayList<BnGoods>();
	private List<BnGoods> selectdata = new ArrayList<BnGoods>();
	private boolean isEdit = false;
	private LinearLayout collect_edit_layout;
	private FancyButton collect_joinshop, collect_del;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collect);
		init();
		loadData();
	}

	private void init() {
		// TODO Auto-generated method stub
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("我的收藏");
		headerFragment.setRightText("编辑");

		collect_list = (ListView) findViewById(R.id.collect_list);
		mAdapter = new CollectListViewAdapter(CollectActivity.this, data,
				selectdata);
		collect_list.setAdapter(mAdapter);
		mAdapter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(CheckBox view, BnGoods goods,
					boolean isChecked) {
				// TODO Auto-generated method stub
				removePath(goods);
				if (isChecked) {
					selectdata.add(goods);
				}
			}
		});
		collect_edit_layout = (LinearLayout) findViewById(R.id.collect_edit_layout);
		headerFragment.setRightTextListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (isEdit) {
					isEdit = false;
					collect_edit_layout.setVisibility(View.GONE);
					headerFragment.setRightText("编辑 ");
					selectdata.clear();
				} else {
					isEdit = true;
					headerFragment.setRightText("完成");
					collect_edit_layout.setVisibility(View.VISIBLE);

				}
				mAdapter.setEdit(isEdit);
				mAdapter.notifyDataSetChanged();
			}
		});
		collect_joinshop = (FancyButton) findViewById(R.id.collect_joinshop);
		collect_del = (FancyButton) findViewById(R.id.collect_del);
		collect_joinshop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(selectdata.size()==0){
					Utils.ToastMessage(CollectActivity.this, "请至少选择一项");
					return;
				}
				Utils.joinShop(CollectActivity.this, getId(), 1);
			}
		});
		collect_del.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(selectdata.size()==0){
					Utils.ToastMessage(CollectActivity.this, "请至少选择一项");
					return;
				}
				delCollect();
			}
		});
	}

	private void removePath(BnGoods position) {
		int size = selectdata.size();
		for (int i = 0; i < size; i++) {
			if (selectdata.get(i).getProduct_id()
					.equals(position.getProduct_id())) {
				selectdata.remove(i);
				return;
			}
		}

	}

	private void loadData() {
//		String uid = PreferenceUtils.getInstance(CollectActivity.this)
//				.getStringValue("uid");
//		HttpUtil.get(UrlConfig.getCollectList(uid),
//				new jsonHttpResponseHandler() {
//					@Override
//					public void onSuccess(JSONObject arg0) {
//						// TODO Auto-generated method stub
//						CollectBean collect = JSON.parseObject(
//								Utils.getResult(arg0), CollectBean.class);
//						data.clear();
//						data.addAll(collect.getDatas());
//						mAdapter.notifyDataSetChanged();
//						super.onSuccess(arg0);
//					}
//				});
	}
	private void delCollect(){
//		String uid = PreferenceUtils.getInstance(CollectActivity.this)
//				.getStringValue("uid");
//		HttpUtil.get(UrlConfig.getCollectDel(uid, getId()), new jsonHttpResponseHandler(){
//			@Override
//			public void onSuccess(JSONObject arg0) {
//				// TODO Auto-generated method stub
//				if(Utils.requestOk(arg0)){
//					Utils.ToastMessage(CollectActivity.this, "删除收藏成功");
//					selectdata.clear();
//					loadData();
//					
//				}
//				super.onSuccess(arg0);
//			}
//		});
	}

	private String getId() {
		String ids = selectdata.get(0).getId();
		for (int i = 1; i < selectdata.size(); i++) {
			ids = ids + "," + selectdata.get(i).getId();
		}
		return ids;
	}
}
