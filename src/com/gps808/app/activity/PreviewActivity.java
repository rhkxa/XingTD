/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gps808.app.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;

import com.gps808.app.R;

import com.gps808.app.utils.BaseActivity;
import com.gps808.app.view.Photoview.PhotoView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 放大图片，并支持下载
 * 
 * @author JIA
 * 
 */
public class PreviewActivity extends BaseActivity {

	private PhotoView image;
	// flag to indicate if need to delete image on server after download
	private ProgressBar loadLocalPb;
	private DisplayImageOptions options;
	private Bitmap mBitmap;
	private final String SavePath = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/HuaShi/Image/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_show_big_image);
		super.onCreate(savedInstanceState);

		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_action_picture)
				.showImageOnFail(R.drawable.ic_action_picture)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		init();
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // TODO Auto-generated method stub
	// menu.add("下载").setIcon(R.drawable.ic_action_download)
	// .setOnMenuItemClickListener(new OnMenuItemClickListener() {
	//
	// @Override
	// public boolean onMenuItemClick(MenuItem arg0) {
	// // TODO Auto-generated method stub
	// try {
	// String path = SavePath
	// + ImageUtils.getTempFileName() + ".jpg";
	// ImageUtils.saveImageToSD(ShowBigImage.this, path,
	// mBitmap, 100);
	// Util.ToastMessage(ShowBigImage.this, "图片保存成功，位置"
	// + path);
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return false;
	// }
	// }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	// return super.onCreateOptionsMenu(menu);
	// }

	private void init() {
		// TODO Auto-generated method stub
		image = (PhotoView) findViewById(R.id.image);
		loadLocalPb = (ProgressBar) findViewById(R.id.pb_load_local);
		ImageLoader
				.getInstance()
				.displayImage(
						"https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg",
						image, options, new SimpleImageLoadingListener() {
							@Override
							public void onLoadingStarted(String imageUri,
									View view) {
								loadLocalPb.setProgress(0);
								loadLocalPb.setVisibility(View.VISIBLE);
							}

							@Override
							public void onLoadingFailed(String imageUri,
									View view, FailReason failReason) {
								// TODO Auto-generated method stub
								loadLocalPb.setVisibility(View.GONE);
							}

							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
								loadLocalPb.setVisibility(View.GONE);
								mBitmap = loadedImage;
							}
						}, new ImageLoadingProgressListener() {
							@Override
							public void onProgressUpdate(String imageUri,
									View view, int current, int total) {
								loadLocalPb.setProgress(Math.round(100.0f
										* current / total));
							}
						});

	}
}