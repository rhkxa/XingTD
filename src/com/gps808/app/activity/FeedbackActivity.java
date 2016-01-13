package com.gps808.app.activity;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.gps808.app.R;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.StringUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;
import com.gps808.app.view.FancyButton;

public class FeedbackActivity extends BaseActivity {
	private HeaderFragment headerFragment;
	private EditText feedback_edit;
	private FancyButton feedback_ok;

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
		feedback_ok = (FancyButton) findViewById(R.id.feedback_ok);
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

		String url = UrlConfig.getUserQuestion();
		JSONObject params = new JSONObject();
		StringEntity entity = null;
		try {
			params.put("content", feedback_edit.getText().toString());
			entity = new StringEntity(params.toString(), "UTF-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LogUtils.DebugLog("post json", params.toString());
		HttpUtil.post(FeedbackActivity.this, url, entity, "application/json",
				new jsonHttpResponseHandler() {
					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						showProgressDialog(FeedbackActivity.this, "提交中，请稍等");
						super.onStart();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						if (Utils.requestOk(response)) {
							Utils.ToastMessage(FeedbackActivity.this,
									"意见提交成功，平台会尽快处理,感谢您的支持");
						}
						super.onSuccess(statusCode, headers, response);
					}
				});

	}
}
