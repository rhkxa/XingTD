package com.gps808.app.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.adapter.UserListViewAdapter;
import com.gps808.app.bean.BnUser;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;

public class UserManagerActivity extends BaseActivity {

	private TextView user_exit;
	private LinearLayout user_add;
	private ListView user_list;
	private UserListViewAdapter mAdapter;
	private HeaderFragment headerFragment;
	private List<BnUser> datalist = new ArrayList<BnUser>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_manager);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("帐户管理");
		mAdapter = new UserListViewAdapter(UserManagerActivity.this, datalist);
		user_list = (ListView) findViewById(R.id.user_list);
		user_list.setAdapter(mAdapter);
		user_add = (LinearLayout) findViewById(R.id.user_add);
		user_exit = (TextView) findViewById(R.id.user_exit);
		user_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Utils.toLogin(UserManagerActivity.this);
			}
		});
		user_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				getLogin(datalist.get(arg2).getMobile(),datalist.get(arg2).getPass());
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		datalist.clear();
		
		mAdapter.notifyDataSetChanged();
		super.onResume();

	}

	// 登录
	private void getLogin(String user, String pass) {
//		if (Utils.isNetWorkConnected(UserManagerActivity.this)) {
//			showProgressDialog(UserManagerActivity.this, "正在切换用户，请稍等");
////			String url = UrlConfig.getLogin(user, pass);
//			String url="";
//			HttpUtil.get(url, new jsonHttpResponseHandler() {
//				@Override
//				public void onSuccess(JSONObject arg0) {
//					// TODO Auto-generated method stub
//					if (Utils.requestOk(arg0)) {
//						Utils.ToastMessage(UserManagerActivity.this, "切换成功");
//						BnUser data = JSON.parseObject(Utils.getResult(arg0),
//								BnUser.class);
//						Utils.setUid(UserManagerActivity.this, data.getUid());
//						Utils.setUser(UserManagerActivity.this,
//								Utils.getResult(arg0));
//						mAdapter.notifyDataSetChanged();
//					} else {
//						Utils.ToastMessage(UserManagerActivity.this,
//								Utils.getKey(arg0, "msg"));
//					}
//
//					super.onSuccess(arg0);
//				}
//			});
//		} else {
//			Utils.showSuperCardToast(UserManagerActivity.this, getResources()
//					.getString(R.string.network_not_connected));
//		}
	}

}
