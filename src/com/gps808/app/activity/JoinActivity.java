package com.gps808.app.activity;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.bean.BnSystem;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.PreferenceUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;

/**
 * 申请加盟的界面
 * 
 * @author rhk
 * 
 */
public class JoinActivity extends BaseActivity {
	private HeaderFragment headerFragment;
	private Button join_button1, join_button2, join_button3, join_button4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join);
		init();
		//getSystem();
	}

	private void init() {
		// TODO Auto-generated method stub
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("申请加盟");
		join_button1 = (Button) findViewById(R.id.join_button1);
		join_button2 = (Button) findViewById(R.id.join_button2);
		join_button3 = (Button) findViewById(R.id.join_button3);
		join_button4 = (Button) findViewById(R.id.join_button4);
		join_button1.setOnClickListener(click);
		join_button2.setOnClickListener(click);
		join_button3.setOnClickListener(click);
		join_button4.setOnClickListener(click);
	}

	private OnClickListener click = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			case R.id.join_button1:
				Utils.ToastMessage(JoinActivity.this, "该功能暂时未开放，尽请期待……");
				//loadData(2);
				break;
			case R.id.join_button2:
				// loadData(1);
				join();
				break;
			case R.id.join_button3:
				Intent intent = new Intent(JoinActivity.this,
						ProcessActivity.class);
				startActivity(intent);
				break;
			case R.id.join_button4:
				Utils.callPhone(JoinActivity.this, join_button4.getText()
						.toString());
				break;
			}
		}

	};
	
	private void getSystem(){
//		HttpUtil.get(UrlConfig.getSystem(), new jsonHttpResponseHandler(){
//			@Override
//			public void onSuccess(JSONObject arg0) {
//				// TODO Auto-generated method stub
//				BnSystem system=JSON.parseObject(Utils.getResult(arg0),BnSystem.class);
//				setValue(system);
//				super.onSuccess(arg0);
//			}
//		});
	}

	private  void setValue(BnSystem system) {
		// TODO Auto-generated method stub
		join_button4.setText(system.getService_tel());
	}

	private void loadData(final int type) {
//		String uid = PreferenceUtils.getInstance(JoinActivity.this)
//				.getStringValue("uid");
//		HttpUtil.get(UrlConfig.getApply(uid, type),
//				new jsonHttpResponseHandler() {
//					@Override
//					public void onSuccess(JSONObject arg0) {
//						// TODO Auto-generated method stub
//						if (Utils.requestOk(arg0)) {
//							Utils.ToastMessage(JoinActivity.this, "申请成功");
//							
//						}
//						super.onSuccess(arg0);
//					}
//				});
	}

	private void join() {
		new AlertDialog.Builder(JoinActivity.this)
				.setMessage("申请正式版分销商需要电话联系平台，确认详细信息！")

				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) { 
						// TODO Auto-generated method stub
						Utils.callPhone(JoinActivity.this, join_button4.getText().toString());

					}
				})
				.setNeutralButton("取消", new DialogInterface.OnClickListener() {

					@Override 
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						arg0.dismiss();
					}
				}).show();
	}
}
