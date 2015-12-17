package com.gps808.app.dialog;

import com.gps808.app.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class DateDialog extends Dialog {

	public DateDialog(Context context) {
		super(context,R.style.Dialog);
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
	}

}
