package com.gps808.app.dialog;



import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.gps808.app.R;


public class AlterSexDialog extends Dialog {

	private Context context;

	public AlterSexDialog(Context context) {
		super(context, R.style.Dialog);
		// TODO Auto-generated constructor stub
		this.context = context;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_alter_password);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
	}
}
