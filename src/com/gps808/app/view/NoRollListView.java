package com.gps808.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class NoRollListView extends ListView {
	public NoRollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoRollListView(Context context) {
		super(context);
	}

	public NoRollListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
