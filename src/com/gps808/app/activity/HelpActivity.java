package com.gps808.app.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.gps808.app.R;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.view.FancyButton;

/**
 * 帮助界面
 * 
 * @author rhk
 * 
 */
public class HelpActivity extends BaseActivity {

	private HeaderFragment headerFragment;
	private WebView webview;
	private FancyButton feedback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("帮助");
		feedback = (FancyButton) findViewById(R.id.feedback);
		feedback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HelpActivity.this,
						FeedbackActivity.class);
				startActivity(intent);
			}
		});
		webview = (WebView) findViewById(R.id.webview);
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
			
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				dismissProgressDialog();
				super.onPageFinished(view, url);
			}
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				showProgressDialog(HelpActivity.this, "正在加载中");
				super.onPageStarted(view, url, favicon);
			}
		});
		webview.loadUrl("http://app.gps808.com/FAQ.html");
	}
}
