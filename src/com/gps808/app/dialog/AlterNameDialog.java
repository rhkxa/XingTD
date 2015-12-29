package com.gps808.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.gps808.app.R;
import com.gps808.app.utils.StringUtils;
import com.gps808.app.utils.Utils;
import com.gps808.app.view.FancyButton;

public class AlterNameDialog extends Dialog implements View.OnClickListener {
	private FancyButton dialog_ok, dialog_no;
	private EditText dialog_content;
	private ImageView dialog_clear;

	public AlterNameDialog(Context context) {
		super(context, R.style.Dialog);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_alter_name);
		dialog_ok = (FancyButton) findViewById(R.id.dialog_ok);
		dialog_no = (FancyButton) findViewById(R.id.dialog_no);
		dialog_content = (EditText) findViewById(R.id.dialog_content);
		dialog_clear = (ImageView) findViewById(R.id.dialog_clear);
		dialog_content.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {
					dialog_clear.setVisibility(View.VISIBLE);
				} else {
					dialog_clear.setVisibility(View.GONE);
				}
			}
		});
		dialog_clear.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog_content.clearFocus();
				dialog_content.setText("");
			}
		});
		dialog_ok.setOnClickListener(this);
		dialog_no.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_ok:
			if (StringUtils.isEmpty(dialog_content.getText().toString())) {
				Utils.ToastMessage(getContext(), "请填写新的名称");
				return;
			}
			alterClickListener.onAlterOk(dialog_content.getText().toString());
			break;
		case R.id.dialog_no:

			break;

		}
		dismiss();
	}

	private OnAlterClickListener alterClickListener;

	public void setOnAlterClickListener(OnAlterClickListener l) {
		alterClickListener = l;
	}

	public interface OnAlterClickListener {
		public void onAlterOk(String key);
	}

}
