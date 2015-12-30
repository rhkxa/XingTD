package com.gps808.app.dialog;



import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.gps808.app.R;


import com.gps808.app.utils.StringUtils;

import com.gps808.app.utils.Utils;
import com.gps808.app.view.FancyButton;

public class AlterPassWordDialog extends Dialog {
	private EditText dialog_old_password;
	private EditText dialog_new_password;
	private EditText dialog_again_password;

	private FancyButton dialog_ok, dialog_no;
	private Context context;

	public AlterPassWordDialog(Context context) {
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
		dialog_old_password = (EditText) findViewById(R.id.dialog_old_password);
		dialog_new_password = (EditText) findViewById(R.id.dialog_new_password);
		dialog_again_password = (EditText) findViewById(R.id.dialog_again_password);
		dialog_ok = (FancyButton) findViewById(R.id.dialog_ok);
		dialog_no = (FancyButton) findViewById(R.id.dialog_no);
		dialog_no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		dialog_ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (StringUtils.isEmpty(dialog_old_password.getText()
						.toString())) {
					Utils.ToastMessage(context, "输入旧密码不能为空");
					return;
				}
				if (StringUtils.isEmpty(dialog_new_password.getText()
						.toString())) {
					Utils.ToastMessage(context, "输入新密码不能为空");
					return;
				}
//				if (StringUtils.isEmpty(dialog_again_password.getText()
//						.toString())) {
//					Utils.ToastMessage(context, "再次输入密码不能为空");
//					return;
//				}
//				if ((dialog_again_password.getText().toString())
//						.equals(dialog_again_password.getText().toString())) {
//					Utils.ToastMessage(context, "两次密码输入不一致");
//					return;
//				}
				alterClickListener.onAlterOk(dialog_old_password.getText()
						.toString(), dialog_new_password.getText().toString());
			}

		});

	}

	private OnAlterClickListener alterClickListener;

	public void setOnAlterClickListener(OnAlterClickListener l) {
		alterClickListener = l;
	}

	public interface OnAlterClickListener {
		public void onAlterOk(String oldPw, String newPw);
	}

	
}
