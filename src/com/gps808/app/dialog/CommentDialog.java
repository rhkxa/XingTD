package com.gps808.app.dialog;

import com.gps808.app.R;

import com.gps808.app.utils.StringUtils;
import com.gps808.app.utils.UrlConfig;

import android.app.Dialog;
import android.content.Context;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CommentDialog extends Dialog {

	private Button btn_ok, btn_cancel;
	private EditText comment;
	private Context context;
	private String id;

	public CommentDialog(Context context, String id) {
		super(context, R.style.Dialog);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.id = id;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_comment);
		init();
	}

	private void init() {
		btn_ok = (Button) findViewById(R.id.btn_ok);
		comment = (EditText) findViewById(R.id.comment_et);
		btn_ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!StringUtils.isEmpty(comment.getText().toString())) {

				}
			}
		});
	}
}
