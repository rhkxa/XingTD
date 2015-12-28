package com.gps808.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.gps808.app.R;
import com.gps808.app.fragment.SearchFragment.OnSearchClickListener;
import com.gps808.app.view.wheelview.AbstractWheelPicker;
import com.gps808.app.view.wheelview.IWheelPicker;

/**
 * @author AigeStudio 2015-12-08
 */
public class WheelDialog extends Dialog implements View.OnClickListener {
	private View root;
	private ViewGroup container;
	private Button btnOK, btnCancel;
	private IWheelPicker picker;
	private String data;
	private int index;
	private android.view.View.OnClickListener onclick;

	public WheelDialog(Context context) {
		super(context, R.style.Dialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		getWindow().setGravity(Gravity.BOTTOM);
		getWindow().getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.WRAP_CONTENT;
		setCanceledOnTouchOutside(true);
		setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				container.removeAllViews();
			}
		});
		root = getLayoutInflater().inflate(R.layout.widget_dialog_wheel, null);
		container = (ViewGroup) root.findViewById(R.id.main_dialog_container);

		btnOK = (Button) root.findViewById(R.id.btn_ok);
		btnCancel = (Button) root.findViewById(R.id.btn_cancel);
		btnOK.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
	}

	@Override
	public void setContentView(View view) {
		if (view instanceof IWheelPicker) {
			picker = (IWheelPicker) view;
			picker.setOnWheelChangeListener(new AbstractWheelPicker.SimpleWheelChangeListener() {
				@Override
				public void onWheelScrollStateChanged(int state) {
					if (state != AbstractWheelPicker.SCROLL_STATE_IDLE) {
						btnOK.setEnabled(false);
					} else {
						btnOK.setEnabled(true);
					}
				}

				@Override
				public void onWheelSelected(int index, String data) {
					WheelDialog.this.data = data;
					WheelDialog.this.index=index;
				}
			});
		}
		container.addView(view);
		super.setContentView(root);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			wheelClickListener.onWheelOk(index,data);

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
		public void onWheelOk(int index,String key);
	}
}