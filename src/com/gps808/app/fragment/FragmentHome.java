package com.gps808.app.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.activity.SearchActivity;
import com.gps808.app.adapter.HomeActiveListViewAdapter;
import com.gps808.app.adapter.HomeCateGridViewAdapter;
import com.gps808.app.adapter.HomeGameListViewAdapter;
import com.gps808.app.adapter.HomeGoodsListViewAdapter;
import com.gps808.app.adapter.HomeOptionsGridViewAdapter;
import com.gps808.app.adapter.MainFragmentAdapter;
import com.gps808.app.bean.BnHActive;
import com.gps808.app.bean.BnHCate;
import com.gps808.app.bean.BnHGame;
import com.gps808.app.bean.BnHGoods;
import com.gps808.app.bean.BnHome;
import com.gps808.app.bean.BnSlider;
import com.gps808.app.utils.BaseFragment;
import com.gps808.app.utils.FileUtils;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;
import com.gps808.app.view.Indicator.UnderlinePageIndicator;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

/**
 * 主页面
 * 
 * @author JIA
 * 
 */

public class FragmentHome extends BaseFragment {

	private List<BnSlider> slider = new ArrayList<BnSlider>();

	private View view;
	private ViewPager mPager;
	private UnderlinePageIndicator mIndicator;
	private MainFragmentAdapter fAdapter;
	private PullToRefreshScrollView mPullRefreshScrollView;
	private ScrollView mScrollView;
	private ListView goodsListView;
	private ListView gameListView;
	private GridView cateGridView;
	private ListView activeListView;
	private ImageView main_head_search;
	private GridView home_options_gridview;
	private HomeOptionsGridViewAdapter homeOptionsGridViewAdapter;
	private HomeActiveListViewAdapter activeListViewAdapter;
	private HomeGameListViewAdapter gameListViewAdapter;
	private HomeCateGridViewAdapter cateGridViewAdapter;
	private HomeGoodsListViewAdapter goodsListViewAdapter;
	private List<BnHGame> game = new ArrayList<BnHGame>();
	private List<BnHActive> active = new ArrayList<BnHActive>();
	private List<BnHCate> cate = new ArrayList<BnHCate>();
	private List<List<BnHGoods>> goods = new ArrayList<List<BnHGoods>>();
	private BnHome home;
	private int currentItem;
	private ScheduledExecutorService scheduledExecutorService;

	public static FragmentHome newInstance() {
		FragmentHome fragment = new FragmentHome();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_home, null);
		init();
		loadData();
		return view;
	}

	private void init() {
		// TODO Auto-generated method stub

		main_head_search = (ImageView) view.findViewById(R.id.main_head_search);
		main_head_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), SearchActivity.class);
				startActivity(intent);
			}
		});
		/** 轮播图 **/

		mPager = (ViewPager) view.findViewById(R.id.pager);
		fAdapter = new MainFragmentAdapter(getChildFragmentManager(), slider);
		mPager.setAdapter(fAdapter);
//		mIndicator = (UnderlinePageIndicator) view.findViewById(R.id.indicator);
//		mIndicator.setViewPager(mPager);
//
//		/** 四个按钮 **/
//		home_options_gridview = (GridView) view
//				.findViewById(R.id.home_options_gridview);
		homeOptionsGridViewAdapter = new HomeOptionsGridViewAdapter(
				getActivity());
		home_options_gridview.setAdapter(homeOptionsGridViewAdapter);
		home_options_gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (arg2 == 3) {
					Utils.toOrder(getActivity(), "");
				} else {
					// Utils.ToastMessage(getActivity(), "敬请期待……");
					Utils.toArea(getActivity(), arg2 + 1);
				}
			}
		});
		/**** game ******************/
		gameListView = (ListView) view.findViewById(R.id.home_game_listview);
		gameListViewAdapter = new HomeGameListViewAdapter(getActivity(), game);
		gameListView.setAdapter(gameListViewAdapter);
		gameListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
			}
		});
		/****** activity ************/
		activeListView = (ListView) view
				.findViewById(R.id.home_active_listview);
		activeListViewAdapter = new HomeActiveListViewAdapter(getActivity(),
				active);
		activeListView.setAdapter(activeListViewAdapter);

		/****** goods ***********/
		goodsListView = (ListView) view.findViewById(R.id.home_goods_listview);
		goodsListViewAdapter = new HomeGoodsListViewAdapter(getActivity(),
				goods);
		goodsListView.setAdapter(goodsListViewAdapter);

		/****** cate ***********/
		cateGridView = (GridView) view.findViewById(R.id.home_cate_gridview);
		cateGridViewAdapter = new HomeCateGridViewAdapter(getActivity(), cate);
		cateGridView.setAdapter(cateGridViewAdapter);
		cateGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Utils.toGoodsList(getActivity(), cate.get(arg2).getCate_id(),
						cate.get(arg2).getTitle());
			}
		});

		/**** ScrollView下拉 *******/
		mPullRefreshScrollView = (PullToRefreshScrollView) view
				.findViewById(R.id.pull_refresh_scrollview);
		mPullRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						mPullRefreshScrollView.setRefreshing(false);
						loadData();
					}
				});

		mScrollView = mPullRefreshScrollView.getRefreshableView();
		mScrollView.smoothScrollTo(0, 0);
		if (!FileUtils.read(getActivity(), "firstPageCache").isEmpty()) {
			home = JSON.parseObject(
					FileUtils.read(getActivity(), "firstPageCache"),
					BnHome.class);
			refresh();
		}
	}

	private void loadData() {
		if (!Utils.isNetWorkConnected(getActivity())) {
			Utils.ToastMessage(getActivity(), "网络状态不可用，请检查网络");
			mPullRefreshScrollView.onRefreshComplete();
			return;
		}
//		HttpUtil.get(UrlConfig.getFirstPage(), new jsonHttpResponseHandler() {
//
//			@Override
//			public void onSuccess(JSONObject arg1) {
//				// TODO Auto-generated method stub
//				home = JSON.parseObject(Utils.getResult(arg1), BnHome.class);
//				FileUtils.write(getActivity(), "firstPageCache",
//						Utils.getResult(arg1));
//				refresh();
//				super.onSuccess(arg1);
//			}
//
//			@Override
//			public void onFinish() {
//				// TODO Auto-generated method stub
//				mPullRefreshScrollView.onRefreshComplete();
//				super.onFinish();
//			}
//
//		});

	}

	private void refresh() {
		slider.clear();
		slider.addAll(home.getSlider());
		active.clear();
		active.addAll(home.getActivity());
		game.clear();
		game.addAll(home.getGame());
		cate.clear();
		cate.addAll(home.getCate());
		goods.clear();
		goods.addAll(home.getGoods_banner());
		fAdapter.notifyDataSetChanged();
		gameListViewAdapter.notifyDataSetChanged();
		activeListViewAdapter.notifyDataSetChanged();
		cateGridViewAdapter.notifyDataSetChanged();
		goodsListViewAdapter.notifyDataSetChanged();
	}

	private class SlideShowTask implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			currentItem = (currentItem + 1) % slider.size();
			handler.obtainMessage().sendToTarget();

		}

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 设置当前页面
			mPager.setCurrentItem(currentItem);
		}
	};

	public void onResume() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4,
				TimeUnit.SECONDS);
		super.onResume();
	};

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		scheduledExecutorService.shutdown();
		super.onStop();
	}

}
