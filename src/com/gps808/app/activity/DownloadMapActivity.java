package com.gps808.app.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.gps808.app.R;
import com.gps808.app.adapter.OfflinePagerAdapter;

import com.gps808.app.utils.BaseActivity;

public class DownloadMapActivity extends BaseActivity implements
		OnPageChangeListener, OnClickListener {
	private RadioGroup map_rg;
	private ViewPager mContentViewPage;
	private PagerAdapter mPageAdapter;
	private TextView mDownloadText;
	private TextView mDownloadedText;
	private ImageView mBackImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offline_map);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
	
		// view pager 用到了所有城市list和已下载城市list所有放在最后初始化
		mContentViewPage = (ViewPager) findViewById(R.id.content_viewpage);

		mPageAdapter = new OfflinePagerAdapter(getSupportFragmentManager());

		mContentViewPage.setAdapter(mPageAdapter);
		mContentViewPage.setCurrentItem(0);
		mContentViewPage.setOnPageChangeListener(this);
		// 顶部
		mDownloadText = (TextView) findViewById(R.id.download_list_text);
		mDownloadedText = (TextView) findViewById(R.id.downloaded_list_text);

		mDownloadText.setOnClickListener(this);
		mDownloadedText.setOnClickListener(this);
		mBackImage = (ImageView) findViewById(R.id.back_image_view);
		mBackImage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.equals(mDownloadText)) {
			int paddingHorizontal = mDownloadText.getPaddingLeft();
			int paddingVertical = mDownloadText.getPaddingTop();
			mContentViewPage.setCurrentItem(0);

			mDownloadText
					.setBackgroundResource(R.drawable.offlinearrow_tab1_pressed);

			mDownloadedText
					.setBackgroundResource(R.drawable.offlinearrow_tab2_normal);

			mDownloadedText.setPadding(paddingHorizontal, paddingVertical,
					paddingHorizontal, paddingVertical);

			mDownloadText.setPadding(paddingHorizontal, paddingVertical,
					paddingHorizontal, paddingVertical);



		} else if (v.equals(mDownloadedText)) {
			int paddingHorizontal = mDownloadedText.getPaddingLeft();
			int paddingVertical = mDownloadedText.getPaddingTop();
			mContentViewPage.setCurrentItem(1);

			mDownloadText
					.setBackgroundResource(R.drawable.offlinearrow_tab1_normal);
			mDownloadedText
					.setBackgroundResource(R.drawable.offlinearrow_tab2_pressed);
			mDownloadedText.setPadding(paddingHorizontal, paddingVertical,
					paddingHorizontal, paddingVertical);
			mDownloadText.setPadding(paddingHorizontal, paddingVertical,
					paddingHorizontal, paddingVertical);

		

		} else if (v.equals(mBackImage)) {
			// 返回
			finish();
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		int paddingHorizontal = mDownloadedText.getPaddingLeft();
		int paddingVertical = mDownloadedText.getPaddingTop();

		switch (arg0) {
		case 0:
			mDownloadText
					.setBackgroundResource(R.drawable.offlinearrow_tab1_pressed);
			mDownloadedText
					.setBackgroundResource(R.drawable.offlinearrow_tab2_normal);
			// mPageAdapter.notifyDataSetChanged();
			break;
		case 1:
			mDownloadText
					.setBackgroundResource(R.drawable.offlinearrow_tab1_normal);

			mDownloadedText
					.setBackgroundResource(R.drawable.offlinearrow_tab2_pressed);
			// mDownloadedAdapter.notifyDataChange();
			break;
		}
		// handler.sendEmptyMessage(UPDATE_LIST);
		mDownloadedText.setPadding(paddingHorizontal, paddingVertical,
				paddingHorizontal, paddingVertical);
		mDownloadText.setPadding(paddingHorizontal, paddingVertical,
				paddingHorizontal, paddingVertical);

	}
}