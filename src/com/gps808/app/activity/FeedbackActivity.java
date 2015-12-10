package com.gps808.app.activity;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.StringUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;

public class FeedbackActivity extends BaseActivity {
	private HeaderFragment headerFragment;
	private EditText feedback_edit;
	private Button feedback_ok;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("意见反馈");
		feedback_edit = (EditText) findViewById(R.id.feedback_edit);
		feedback_ok = (Button) findViewById(R.id.feedback_ok);
		feedback_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!StringUtils.isEmpty(feedback_edit.getText().toString())) {
					loadOrder();
				} else {
					Utils.ToastMessage(FeedbackActivity.this, "意见不能为空");
				}
			}
		});
	}

	private void loadOrder() {
//		if (Utils.isNetWorkConnected(FeedbackActivity.this)) {
//			showProgressDialog(FeedbackActivity.this, "加载中，请稍等");
//			String url = UrlConfig.submitSuggest(feedback_edit.getText()
//					.toString());
//			HttpUtil.get(url, new jsonHttpResponseHandler() {
//				@Override
//				public void onSuccess(JSONObject arg0) {
//					// TODO Auto-generated method stub
//					
//					// if (base.getStatusCode().equals("200")) {
//					// Utils.ToastMessage(FeedbackActivity.this,
//					// "意见提交成功，平台会尽快处理,感谢您的支持");
//					// }
//
//					super.onSuccess(arg0);
//				}
//			});
//		} else {
//			Utils.showSuperCardToast(FeedbackActivity.this, getResources()
//					.getString(R.string.network_not_connected));
//		}
//
	};
}
