package com.gps808.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gps808.app.R;
import com.gps808.app.utils.Utils;
import com.gps808.app.view.FancyButton;
import com.gps808.app.view.LinkView;
import com.gps808.app.view.LinkView.OnLinkClickListener;

public class CustomOkDialog extends Dialog {

	private Context context;
	private View.OnClickListener okClickListener;
	private String title, content;
	private TextView dialog_title;
	private LinkView dialog_content;
	private FancyButton dialog_ok;

	public CustomOkDialog(Context context, String title, String content,
			View.OnClickListener okClickListener) {
		super(context, R.style.Dialog);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.okClickListener = okClickListener;
		this.title = title;
		this.content = content;
		setCanceledOnTouchOutside(false);

	}

	public CustomOkDialog(Context context) {
		super(context, R.style.Dialog);
		// TODO Auto-generated constructor stub
		this.context = context;
		setCanceledOnTouchOutside(false);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_ok);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		dialog_content = (LinkView) findViewById(R.id.dialog_content);
		dialog_title = (TextView) findViewById(R.id.dialog_title);
		dialog_ok = (FancyButton) findViewById(R.id.dialog_ok);
		dialog_title.setText(title);
		dialog_content.setLinkText(content);
		if (okClickListener == null) {
			okClickListener = close;
		}
		dialog_ok.setOnClickListener(okClickListener);
		dialog_content.setLinkClickListener(new OnLinkClickListener() {
			
			@Override
			public void onLinkClick() {
				// TODO Auto-generated method stub
				Utils.callPhone(context, "0317-4227916");
			}
		});

	}

	public void setTitle(String title) {
		dialog_title.setText(title);
	}

	public void setContent(String content) {
		dialog_content.setLinkText(content);
	}

	public void setOkClick(android.view.View.OnClickListener listener) {
		dialog_ok.setOnClickListener(listener);
	}
	
	

	private android.view.View.OnClickListener close = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			dismiss();
		}
	};
}
