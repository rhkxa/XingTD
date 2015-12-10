package com.gps808.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class NoRollWebview extends WebView{
	public NoRollWebview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoRollWebview(Context context) {
		super(context);
	}

	public NoRollWebview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
