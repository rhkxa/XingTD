package com.gps808.app.dialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.gps808.app.R;
import com.gps808.app.dialog.AlterNameDialog.OnAlterClickListener;
import com.gps808.app.dialog.DateTimeDialog.OnWheelClickListener;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.view.FancyButton;
import com.gps808.app.view.wheelview.WheelDatePicker;
import com.mob.tools.utils.Data;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class DateDialog extends Dialog {

	private CheckBox chose_today, chose_yesterday, chose_beforeday,
			chose_onehour, chose_custom;
	private List<CheckBox> checkboxList = new ArrayList<CheckBox>();
	private FancyButton dialog_no, dialog_ok;
	private TextView custom_end_time, custom_start_time;
	private Context context;
	private String start, end;
	private SimpleDateFormat sdf;

	public DateDialog(Context context) {
		super(context, R.style.Dialog);
		this.context = context;
		setCanceledOnTouchOutside(false);
	}

	public DateDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_date);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		custom_end_time = (TextView) findViewById(R.id.custom_end_time);
		custom_start_time = (TextView) findViewById(R.id.custom_start_time);
		chose_today = (CheckBox) findViewById(R.id.chose_today);
		chose_yesterday = (CheckBox) findViewById(R.id.chose_yesterday);
		chose_beforeday = (CheckBox) findViewById(R.id.chose_beforeday);
		chose_onehour = (CheckBox) findViewById(R.id.chose_onehour);
		chose_custom = (CheckBox) findViewById(R.id.chose_custom);
		dialog_no = (FancyButton) findViewById(R.id.dialog_no);
		dialog_ok = (FancyButton) findViewById(R.id.dialog_ok);
		dialog_no.setOnClickListener(click);
		dialog_ok.setOnClickListener(click);
		checkboxList.add(chose_beforeday);
		checkboxList.add(chose_custom);
		checkboxList.add(chose_onehour);
		checkboxList.add(chose_today);
		checkboxList.add(chose_yesterday);
		for (CheckBox item : checkboxList) {
			item.setOnCheckedChangeListener(check);
		}
		custom_end_time.setOnClickListener(choseDate);
		custom_start_time.setOnClickListener(choseDate);

	}

	private void setCheckable() {
		for (CheckBox item : checkboxList) {
			item.setChecked(false);
		}
	}

	private android.view.View.OnClickListener choseDate = new View.OnClickListener() {

		@Override
		public void onClick(final View arg0) {
			// TODO Auto-generated method stub
			DateTimeDialog datatime = new DateTimeDialog(getContext());
			datatime.setOnWheelClickListener(new OnWheelClickListener() {

				@Override
				public void onWheelOk(int index, String key) {
					// TODO Auto-generated method stub
					switch (arg0.getId()) {
					case R.id.custom_start_time:
						custom_start_time.setText(key);
						start = key;
						break;
					case R.id.custom_end_time:
						custom_end_time.setText(key);
						end = key;
						break;

					}
				}
			});
			datatime.show();
		}
	};
	private OnCheckedChangeListener check = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			setCheckable();
			switch (arg0.getId()) {
			case R.id.chose_beforeday:
				chose_beforeday.setChecked(arg1);
				getDate(3);
				break;
			case R.id.chose_today:
				chose_today.setChecked(arg1);
				getDate(1);
				break;
			case R.id.chose_yesterday:
				chose_yesterday.setChecked(arg1);
				getDate(1);
				break;
			case R.id.chose_onehour:
				chose_onehour.setChecked(arg1);
				getOneHour();
				break;
			case R.id.chose_custom:
				chose_custom.setChecked(arg1);
				if (arg1) {
					custom_end_time.setEnabled(true);
					custom_start_time.setEnabled(true);
				} else {
					custom_end_time.setEnabled(true);
					custom_start_time.setEnabled(true);
				}
				break;

			}
			LogUtils.DebugLog("开始:"+start+"结束"+end);
		}
	};

	private android.view.View.OnClickListener click = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			case R.id.dialog_no:

				break;

			case R.id.dialog_ok:
				break;
			}
			dismiss();
		}
	};
	private OnTimeClickListener timeClickListener;

	public void setOnTimeClickListener(OnTimeClickListener l) {
		timeClickListener = l;
	}

	public interface OnTimeClickListener {
		public void onTimeOk(String start, String end);
	}

	private void getDate(int flag) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		switch (flag) {
		case 1:
			break;
		case 2:
			calendar.add(Calendar.DATE, -1);
			break;
		case 3:
			calendar.add(Calendar.DATE, -2);
			break;
		}
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		start = sdf.format(calendar.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		end = sdf.format(calendar.getTime());
		
	}

	private void getOneHour() {
		Calendar calendar = Calendar.getInstance();
		end = sdf.format(calendar.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, -1);
		start = sdf.format(calendar.getTime());
	}

}
