package com.gps808.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import com.gps808.app.R;

import com.gps808.app.view.wheelview.AbstractWheelPicker.OnWheelChangeListener;
import com.gps808.app.view.wheelview.WheelDatePicker;
import com.gps808.app.view.wheelview.WheelTimePicker;

public class DateTimeDialog extends Dialog implements View.OnClickListener {
	private Button btnOK, btnCancel;
	private String data, time;
	private int index;
	private WheelDatePicker dataPick;
	private WheelTimePicker timePick;
	private Context mContext
	;

	public DateTimeDialog(Context context) {
		super(context, R.style.Dialog);
		mContext=context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		getWindow().setGravity(Gravity.BOTTOM);
		getWindow().getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.WRAP_CONTENT;
		setCanceledOnTouchOutside(true);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widget_wheel_datatime);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		btnOK = (Button) findViewById(R.id.btn_ok);
		btnCancel = (Button) findViewById(R.id.btn_cancel);
		btnOK.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		dataPick = (WheelDatePicker) findViewById(R.id.datapick);
		timePick = (WheelTimePicker) findViewById(R.id.timepick);
		dataPick.setCurrentTextColor(mContext.getResources().getColor(R.color.app_blue));
		timePick.setCurrentTextColor(mContext.getResources().getColor(R.color.app_blue));
		dataPick.setOnWheelChangeListener(new OnWheelChangeListener() {

			@Override
			public void onWheelSelected(int index, String data) {
				// TODO Auto-generated method stub
				DateTimeDialog.this.data = data;
			}

			@Override
			public void onWheelScrolling(float deltaX, float deltaY) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onWheelScrollStateChanged(int state) {
				// TODO Auto-generated method stub

			}
		});
		timePick.setOnWheelChangeListener(new OnWheelChangeListener() {

			@Override
			public void onWheelSelected(int index, String data) {
				// TODO Auto-generated method stub
				DateTimeDialog.this.time = data;
			}

			@Override
			public void onWheelScrolling(float deltaX, float deltaY) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onWheelScrollStateChanged(int state) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			wheelClickListener.onWheelOk(index, data + " " + time + ":00");

			break;
		case R.id.btn_cancel:

			break;
		}
		dismiss();
	}

	private OnWheelClickListener wheelClickListener;

	public void setOnWheelClickListener(OnWheelClickListener l) {
		wheelClickListener = l;
	}

	public interface OnWheelClickListener {
		public void onWheelOk(int index, String key);
	}

}
