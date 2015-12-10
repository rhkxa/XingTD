package com.gps808.app.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.adapter.CommentListViewAdapter;
import com.gps808.app.bean.BnComment;
import com.gps808.app.bean.CommentBean;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class CommentActivity extends BaseActivity {
	private HeaderFragment headerFragment;
	private CommentListViewAdapter commentListViewAdapter;
	private String goods_id;
	private int pagenum = 0;
	private final int pageSize = 10;
	private List<BnComment> commentlist = new ArrayList<BnComment>();
	private PullToRefreshListView comment_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		init();
		loadData();
	}

	private void init() {
		// TODO Auto-generated method stub
		goods_id = getIntent().getStringExtra("goods_id");
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("所有评论");
		commentListViewAdapter = new CommentListViewAdapter(
				CommentActivity.this, commentlist);
		comment_list = (PullToRefreshListView) findViewById(R.id.comment_list);
		comment_list.setMode(Mode.PULL_FROM_END);
		comment_list.setAdapter(commentListViewAdapter);
		// Set a listener to be invoked when the list should be refreshed.
		comment_list.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(
						getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				// pull的时候显示的文字
				refreshView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
				// 正在加载时候显示的文字
				refreshView.getLoadingLayoutProxy().setRefreshingLabel("正在加载中");
				// 取消的时候 的文字
				refreshView.getLoadingLayoutProxy().setReleaseLabel("放开以加载");

				// Do work to refresh the list here.
				loadData();
			}
		});

	}

	// 获取网络数据
	private void loadData() {
//		if (Utils.isNetWorkConnected(CommentActivity.this)) {
//
//			String url = UrlConfig.getCommentList(goods_id, pagenum, pageSize);
//			HttpUtil.get(url, new jsonHttpResponseHandler() {
//				@Override
//				public void onSuccess(JSONObject arg0) {
//					// TODO Auto-generated method stub
//
//					CommentBean commentBean = JSON.parseObject(
//							Utils.getResult(arg0), CommentBean.class);
//					commentlist.addAll(commentBean.getDatas());
//					if (commentBean.getDatas().size() < pageSize) {
//						comment_list.setMode(Mode.DISABLED);
//						Utils.ToastMessage(CommentActivity.this, "暂无更多评论");
//					} else {
//						pagenum++;
//					}
//					commentListViewAdapter.notifyDataSetChanged();
//
//					super.onSuccess(arg0);
//				}
//			});
//		} else {
//			Utils.showSuperCardToast(CommentActivity.this, getResources()
//					.getString(R.string.network_not_connected));
//		}
	}
}
