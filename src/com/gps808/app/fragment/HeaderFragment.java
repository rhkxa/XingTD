package com.gps808.app.fragment;


import com.gps808.app.R;
import com.gps808.app.utils.BaseFragment;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HeaderFragment extends Fragment {

	private ImageView backBtn;
	private ImageView commentBtn;
	private TextView titleText;
	private TextView rightText;
	private LinearLayout backLayout;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.back_header, null);
		backBtn = (ImageView) view.findViewById(R.id.backBtn);
		titleText = (TextView) view.findViewById(R.id.titleText);
		commentBtn = (ImageView) view.findViewById(R.id.all_comments);
		rightText = (TextView) view.findViewById(R.id.right_text);
		backLayout = (LinearLayout) view.findViewById(R.id.backlayout);
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		backListener();
	}

	private void backListener() {
		// commentBtn.setOnTouchListener(listener);
		backLayout.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
//				Intent mainIntent = new Intent(getActivity(),
//						MainActivity.class);
//				ActivityManager am = (ActivityManager) getActivity()
//						.getSystemService(Context.ACTIVITY_SERVICE);
//				List<RunningTaskInfo> appTask = am.getRunningTasks(1);
//				if (appTask.size() > 0
//						&& appTask.get(0).baseActivity.equals(mainIntent
//								.getComponent())) {
//					getActivity().onBackPressed();
//				} else {
//					startActivity(new Intent(getActivity(), LoginActiivty.class));
////							.putExtra(Contracts.KEY_NOTIFY_LAUNCHER, true));			
//					getActivity().finish();
//				}
				getActivity().onBackPressed();

			}
		});

	}

	public void setTitleText(String title) {
		titleText.setText(title);
	}

	private void setRightTextVisible() {
		rightText.setVisibility(View.VISIBLE);
	}

	public void setRightText(String text) {
		setRightTextVisible();
		rightText.setText(text);
	}

	public void setRightTextListener(OnClickListener listener) {
		setRightTextVisible();
		rightText.setOnClickListener(listener);
	}

	private void setCommentBtnVisible() {
		commentBtn.setVisibility(View.VISIBLE);
	}
	public void setCommentBtnGone() {
		commentBtn.setVisibility(View.GONE);
	}

	public void setCommentBtnListener(OnClickListener listener) {
		setCommentBtnVisible();
		commentBtn.setOnClickListener(listener);
	}

	public void setImageButtonResource(int resId) {
		setCommentBtnVisible();
		commentBtn.setImageResource(resId);
	}

	public int getHeight() {
		// TODO Auto-generated method stub
		return backBtn.getHeight();
	}
	public ImageView getCommentBtn(){
		return commentBtn;
	}

}
