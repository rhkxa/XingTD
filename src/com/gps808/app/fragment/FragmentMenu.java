package com.gps808.app.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.activity.SearchActivity;
import com.gps808.app.adapter.MenuAdapter;
import com.gps808.app.bean.BnMCate;
import com.gps808.app.bean.BnMenu;
import com.gps808.app.utils.BaseFragment;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;

/**
 * 分类的页面
 * 
 * @author rhk
 * 
 */
public class FragmentMenu extends BaseFragment {

	private List<BnMCate> data = new ArrayList<BnMCate>();
	private View view;
	private GridView menu_grid;
	private MenuAdapter mAdapter;
	private LinearLayout search_layout;

	public static FragmentMenu newInstance() {
		FragmentMenu fragment = new FragmentMenu();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_menu, null);
		init();
		loadData();
		return view;
	}

	private void init() {
		// TODO Auto-generated method stub
		menu_grid = (GridView) view.findViewById(R.id.menu_grid);
		mAdapter = new MenuAdapter(getActivity(), data);
		menu_grid.setAdapter(mAdapter);
		menu_grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Utils.toGoodsList(getActivity(),data.get(arg2).getCate_id(), data.get(arg2).getCate_name());
			}
		});
		search_layout = (LinearLayout) view.findViewById(R.id.search_layout);
		search_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), SearchActivity.class);
				startActivity(intent);
			}
		});
	}

	private void loadData() {
//		HttpUtil.get(UrlConfig.getClassList(), new jsonHttpResponseHandler() {
//			@Override
//			public void onSuccess(JSONObject arg0) {
//				// TODO Auto-generated method stub
//				BnMenu bm = JSON.parseObject(Utils.getResult(arg0),
//						BnMenu.class);
//				data.clear();
//				// data.addAll(menuBean.getResult().getDatas());
//				data.addAll(bm.getDatas());
//				mAdapter.notifyDataSetChanged();
//				super.onSuccess(arg0);
//			}
//		});
	}

}
