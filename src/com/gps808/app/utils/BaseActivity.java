package com.gps808.app.utils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.Header;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * 
 * @author JIA
 *
 */
public class BaseActivity extends FragmentActivity {
	public XtdApplication huaShiApplication;

	private ProgressDialog progressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		huaShiApplication = (XtdApplication) getApplication();
		XtdApplication.getInstance().addActivity(this);
		// getMenuTextColor();
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		// getOverFlowMenu();
	}

	public XtdApplication getAppContext() {
		return huaShiApplication;

	}

	public void showProgressDialog(Activity activity, CharSequence message) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(activity);
			progressDialog.setIndeterminate(true);
		}
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage(message);
		progressDialog.show();
	}

	public void dismissProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dismissProgressDialog();
	}

	public class jsonHttpResponseHandler extends JsonHttpResponseHandler {

		@Override
		public void onFailure(int statusCode, Header[] headers,
				String responseString, Throwable throwable) {
			// TODO Auto-generated method stub
			super.onFailure(statusCode, headers, responseString, throwable);
		}

		@Override
		public void onFinish() {
			super.onFinish();
			dismissProgressDialog();
		}
	}

	public static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

}
