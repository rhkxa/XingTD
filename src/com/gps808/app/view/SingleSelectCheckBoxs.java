package com.gps808.app.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gps808.app.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;

public class SingleSelectCheckBoxs extends LinearLayout {
	@SuppressLint("UseSparseArrays")
	private Map<Integer, String> mData = new HashMap<Integer, String>();
	private int mSelectPosition = 0;
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private int mSrcW;
	private WindowManager wm;
	private int maxItemW;
	private List<View> mItemViews = new ArrayList<View>();
	private LinearLayout mLlytRoot;
	private int maxOneRowItemCount = -1;
	public OnSelectListener mOnSelectListener;

	public interface OnSelectListener {
		void onSelect(int position);
	}

	@SuppressWarnings("deprecation")
	public SingleSelectCheckBoxs(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		this.mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		this.mSrcW = wm.getDefaultDisplay().getWidth();
	}

	public SingleSelectCheckBoxs(Context context) {
		super(context);
	}

	/**
	 * 初始化数据
	 */
	private void initView() {
		View view = mLayoutInflater.inflate(R.layout.widget_single_select_chk,
				this);
		// 用来放很多checkbox的布局
		mLlytRoot = (LinearLayout) view.findViewById(R.id.single_select_root);
		if (null == mData || 0 == mData.size()) {
			return;
		}

		// 根据数据集合，实例化出对应的条目View，存入list， 每个checkbox设置tag为对应的数据位置
		for (int i = 0; i < mData.size(); i++) {
			View itemView = mLayoutInflater.inflate(
					R.layout.widget_item_single_select, null);
			CheckBox chk = (CheckBox) itemView
					.findViewById(R.id.single_select_chk);
			chk.setOnClickListener(new OnChkClickEvent());
			chk.setText(mData.get(i));
			chk.setTag(i);
			mItemViews.add(itemView);

			// 测量最长的一个条目的位置
			measureView(itemView);
			int width = itemView.getMeasuredWidth();
			// 获取最长的条目长度
			if (width > maxItemW) {
				maxItemW = width;
			}
		}

		// 每行最大条目数目（屏幕宽度-左右两个页边距个8dip，再除以最长的条目的宽度）
		maxOneRowItemCount = (mSrcW - dip2px(mContext, 16)) / maxItemW;

		// 如果每行最大条目数为0，说明长度最大的条目太宽了，连一行一个都显示不完全，就让他一行显示一条。
		if (maxOneRowItemCount == 0) {
			maxOneRowItemCount = 1;
		}
		// 开始添加条目
		addItem();
		notifyAllItemView(0);

	}

	/**
	 * 添加条目
	 */
	private void addItem() {
		// 计算出一共有多少行，
		int rowCount = mItemViews.size() / maxOneRowItemCount;

		// 总条目个数除以每行的条目数，判断是否有余数。
		int lastRowItemCount = mItemViews.size() % maxOneRowItemCount;

		// 重新绘制每个条目的宽度（屏幕宽度-左右两边页边距各8dip，再除以每行最大的条目数）
		int itemW = (mSrcW - dip2px(mContext, 16)) / maxOneRowItemCount;
		for (int i = 0; i < rowCount; i++) {

			// new一个新的行的线性布局，用于添加每行的条目
			LinearLayout llytRow = getNewRow();
			for (int j = 0; j < maxOneRowItemCount; j++) {
				View itemView = mItemViews.get(i * maxOneRowItemCount + j);
				// 重新绘制条目的宽度
				resetItemWidth(itemView, itemW);
				llytRow.addView(itemView);
			}
			mLlytRoot.addView(llytRow);
		}

		// 如果有还剩下不够一行的条目，重新开一行，添加余下的条目view
		if (lastRowItemCount != 0 && rowCount > 0) {
			LinearLayout llytRow = getNewRow();
			for (int k = 0; k < lastRowItemCount; k++) {
				View itemView = mItemViews.get(maxOneRowItemCount * rowCount
						+ k);
				resetItemWidth(itemView, itemW);
				llytRow.addView(itemView);
			}
			mLlytRoot.addView(llytRow);
			// 如果总条目数连一行都填不满
		} else if (rowCount == 0) {
			LinearLayout llytRow = getNewRow();
			for (int j = 0; j < mData.size(); j++) {
				View itemView = mItemViews.get(j);
				itemW = (mSrcW - dip2px(mContext, 16)) / mData.size();
				resetItemWidth(itemView, itemW);
				llytRow.addView(itemView);
			}
			mLlytRoot.addView(llytRow);
		}
	}

	/**
	 * 重新设置条目宽度
	 */
	private void resetItemWidth(View itemView, int itemW) {
		LinearLayout itemViewRoot = (LinearLayout) itemView
				.findViewById(R.id.item_single_select_root);
		// LinearLayout.LayoutParams lp = new LayoutParams(itemW,
		// LayoutParams.MATCH_PARENT);
		LinearLayout.LayoutParams lp = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		itemViewRoot.setLayoutParams(lp);
	}

	/**
	 * 设置一个新行
	 * 
	 * @return
	 */
	private LinearLayout getNewRow() {
		LinearLayout llytRow = new LinearLayout(mContext);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llytRow.setOrientation(LinearLayout.HORIZONTAL);
		llytRow.setLayoutParams(lp);
		return llytRow;
	}

	/**
	 * 设置数据
	 * 
	 * @param data
	 */
	public void setData(Map<Integer, String> data) {
		this.mData = data;
		initView();
	}

	/**
	 * 刷新数据
	 */
	private void notifyAllItemView(int selectPosition) {
		for (View itemView : mItemViews) {
			CheckBox chk = (CheckBox) itemView
					.findViewById(R.id.single_select_chk);
			if ((Integer) chk.getTag() != selectPosition) {
				chk.setChecked(false);
			} else {
				chk.setChecked(true);
			}
		}
	}

	/**
	 * 条目checkbox的点击事件
	 */
	private class OnChkClickEvent implements OnClickListener {
		@Override
		public void onClick(View v) {
			CheckBox chk = (CheckBox) v;
			if (chk.isChecked()) {
				mSelectPosition = (Integer) chk.getTag();

			} else {
				mSelectPosition = 0;
			}
			notifyAllItemView(mSelectPosition);
			mOnSelectListener.onSelect(mSelectPosition);
		}
	}

	public void setOnSelectListener(OnSelectListener l) {
		this.mOnSelectListener = l;
	}

	public void measureView(View v) {
		if (v == null) {
			return;
		}
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		v.measure(w, h);
	}

	/**
	 * 
	 * 描述：dip转换为px
	 * 
	 * @param context
	 * @param dipValue
	 * @return
	 * @throws
	 */
	public int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}
}
