package com.gps808.app.activity;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;
import com.gps808.app.view.AutoAjustSizeTextView;
import com.gps808.app.view.AutoScaleTextView;

public class ArticleDetailActivity extends BaseActivity {
	private AutoScaleTextView title;
	private LinearLayout backlayout;
	private WebView webview;

	private String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article_details);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		id = getIntent().getStringExtra("id");
		title = (AutoScaleTextView) findViewById(R.id.titleText);
		backlayout = (LinearLayout) findViewById(R.id.backlayout);
		backlayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		webview = (WebView) findViewById(R.id.article_details_webview);
		webview.getSettings().setJavaScriptEnabled(true);
		// goods_detail_webview.getSettings().setBuiltInZoomControls(true);//
		webview.getSettings().setUseWideViewPort(true);
		webview.getSettings().setLoadWithOverviewMode(true);
		// 会出现放大缩小的按钮
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}
		});

		loadData();
	}

	private void loadData() {
//		if (Utils.isNetWorkConnected(ArticleDetailActivity.this)) {
//			String url = "";
//			HttpUtil.get(url, new jsonHttpResponseHandler() {
//				@Override
//				public void onSuccess(JSONObject arg0) {
//					// TODO Auto-generated method stub
//					
//					super.onSuccess(arg0);
//				}
//			});
//		} else {
//			Utils.showSuperCardToast(ArticleDetailActivity.this, getResources()
//					.getString(R.string.network_not_connected));
//		}
	}

	private void setValue() {
		title.setText("23432");
		webview.loadDataWithBaseURL(null,""
				, "text/html", "utf-8", null);
	}
}
