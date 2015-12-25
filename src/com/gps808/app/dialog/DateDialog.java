package com.gps808.app.dialog;

import java.util.ArrayList;
import java.util.List;

import com.gps808.app.R;
import com.gps808.app.view.FancyButton;
import com.gps808.app.view.wheelview.WheelDatePicker;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class DateDialog extends Dialog {

	private CheckBox chose_today, chose_yesterday, chose_beforeday,
			chose_onehour, chose_custom;
	private List<CheckBox> checkboxList = new ArrayList<CheckBox>();
	private FancyButton dialog_no, dialog_ok;
	private TextView custom_end_time, custom_start_time;
	private Context context;

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
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			WheelDialog wheelDialog = new WheelDialog(context);
			WheelDatePicker wheelDatePicker = new WheelDatePicker(context);
			wheelDatePicker.setPadding(0, 0, 0, 0);
			wheelDatePicker.setTextColor(0xFF7787C5);
			wheelDatePicker.setCurrentTextColor(0xFF7774B7);
			wheelDatePicker.setLabelColor(0xFF7774B7);
			// wheelDatePicker.setTextSize(textSize);
			// wheelDatePicker.setItemSpace(itemSpace);
			wheelDatePicker.setCurrentDate(2015, 12, 20);
			wheelDialog.setContentView(wheelDatePicker);
			wheelDialog.show();
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
				break;
			case R.id.chose_custom:
				chose_custom.setChecked(arg1);
				break;
			case R.id.chose_onehour:
				chose_onehour.setChecked(arg1);
				break;
			case R.id.chose_today:
				chose_today.setChecked(arg1);
				break;
			case R.id.chose_yesterday:
				chose_yesterday.setChecked(arg1);
				break;

			}
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

}
