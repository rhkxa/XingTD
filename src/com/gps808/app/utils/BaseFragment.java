package com.gps808.app.utils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tendcloud.tenddata.TCAgent;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class BaseFragment extends Fragment {
	private ProgressDialog progressDialog = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public void showProgressDialog(final Context context, CharSequence message) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(context);
			progressDialog.setIndeterminate(true);
		}
		progressDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface arg0) {
				// TODO Auto-generated method stub
				HttpUtil.cancelRequest(context);
			}
		});
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage(message);
		progressDialog.show();
	}

	public void dismissProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	public class jsonHttpResponseHandler extends JsonHttpResponseHandler {
		@Override
		public void onFailure(int statusCode, org.apache.http.Header[] headers,
				String responseString, Throwable throwable) {
		};

		@Override
		public void onFinish() {
			super.onFinish();
			dismissProgressDialog();
		}
	}

	static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

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
	
	
	
	
	@Override
	public void onResume() {
		super.onResume();
		TCAgent.onPageStart(getActivity(), getClass().getName());
	}

	@Override
	public void onPause() {
		super.onPause();
		TCAgent.onPageEnd(getActivity(), getClass().getName());
	}

}
