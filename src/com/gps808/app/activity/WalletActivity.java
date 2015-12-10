package com.gps808.app.activity;



import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gps808.app.R;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.UrlConfig;
/**
 * 钱包的界面
 * @author rhk
 *
 */
public class WalletActivity extends BaseActivity {
	private HeaderFragment headerFragment;
	private WebView webview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wallet);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("我的钱包");
		webview = (WebView) findViewById(R.id.userpoint);
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
		webview.loadUrl(UrlConfig.User_score());
	}
	
	

}
