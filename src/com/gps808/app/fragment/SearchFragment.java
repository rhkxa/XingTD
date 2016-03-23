package com.gps808.app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.gps808.app.R;
import com.gps808.app.utils.BaseFragment;
import com.gps808.app.utils.StringUtils;

public class SearchFragment extends Fragment {
	private Button layout_search_bar_button;
	private ImageView layout_search_bar_close;
	private EditText layout_search_bar_edittext;
	private LinearLayout goods_search_box;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.widget_search_layout, null);

		layout_search_bar_button = (Button) view
				.findViewById(R.id.layout_search_bar_button);
		goods_search_box = (LinearLayout) view
				.findViewById(R.id.goods_search_box);
		layout_search_bar_edittext = (EditText) view
				.findViewById(R.id.layout_search_bar_edittext);
		layout_search_bar_close = (ImageView) view
				.findViewById(R.id.layout_search_bar_close);
		goods_search_box = (LinearLayout) view
				.findViewById(R.id.goods_search_box);
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		onListener();
	}

	private void onListener() {
		// TODO Auto-generated method stub
		layout_search_bar_edittext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
		layout_search_bar_edittext
				.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View arg0, boolean arg1) {
						// TODO Auto-generated method stub
						if (arg1) {
							layout_search_bar_close.setVisibility(View.VISIBLE);
							layout_search_bar_button
									.setVisibility(View.VISIBLE);
						} else {

							layout_search_bar_button.setVisibility(View.GONE);
							InputMethodManager imm = (InputMethodManager) getActivity()
									.getSystemService(
											Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									layout_search_bar_edittext.getWindowToken(),
									0);
						}
					}
				});
		goods_search_box.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				layout_search_bar_edittext.setFocusable(true);
			}
		});
		layout_search_bar_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				layout_search_bar_edittext.clearFocus();
				layout_search_bar_close.setVisibility(View.GONE);
				if (!StringUtils.isEmpty(layout_search_bar_edittext.getText()
						.toString())) {
					layout_search_bar_edittext.setText("");
					searchClickListener.onSearchClose();
				}
			}
		});
		layout_search_bar_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				searchClickListener.onSearch(layout_search_bar_edittext
						.getText().toString().trim());
				layout_search_bar_edittext.clearFocus();
			}
		});

	}

	private OnSearchClickListener searchClickListener;

	public void setOnSearchClickListener(OnSearchClickListener l) {
		searchClickListener = l;
	}

	public interface OnSearchClickListener {
		public void onSearch(String key);

		public void onSearchClose();
	}

	public void setHint(String hint) {
		layout_search_bar_edittext.setHint(hint);
	}
}
