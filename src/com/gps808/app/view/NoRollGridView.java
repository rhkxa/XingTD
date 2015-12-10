package com.gps808.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 解决与 ScrollView滑动冲突的GridView
 * 
 * @author JIA
 * 
 */
public class NoRollGridView extends GridView {

	public NoRollGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoRollGridView(Context context) {
		super(context);
	}

	public NoRollGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
