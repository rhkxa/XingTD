package com.gps808.app.activity;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gps808.app.R;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.PreferenceUtils;
import com.gps808.app.utils.StringUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;
import com.gps808.app.view.FancyButton;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SubmitComActivity extends BaseActivity {

	private HeaderFragment headerFragment;
	private ImageView item_goods_image;
	private TextView item_goods_introduce;
	private TextView item_goods_number;
	private TextView item_goods_price;
	private EditText comment_content;
	private FancyButton submit_ok;
	private String goods_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sumbit_comment);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("评价 ");
		item_goods_image = (ImageView) findViewById(R.id.item_goods_image);
		item_goods_introduce = (TextView) findViewById(R.id.item_goods_introduce);
		item_goods_number = (TextView) findViewById(R.id.item_goods_number);
		item_goods_price = (TextView) findViewById(R.id.item_goods_price);
		comment_content = (EditText) findViewById(R.id.comment_content);
		goods_id = getIntent().getStringExtra("goods_id");
		ImageLoader.getInstance().displayImage(
				getIntent().getStringExtra("goods_img"), item_goods_image);
		item_goods_introduce.setText(getIntent().getStringExtra("goods_name"));
		item_goods_price.setText(getIntent().getStringExtra("goods_price"));
		submit_ok = (FancyButton) findViewById(R.id.submit_ok);

		submit_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (StringUtils.isEmpty(comment_content.getText().toString())) {
					Utils.ToastMessage(SubmitComActivity.this, "请填写评价内容");
					return;
				}
				submit(comment_content.getText().toString());
			}
		});

	}

	private void submit(String content) {
		String url = UrlConfig.getUserQuestion();
		JSONObject params = new JSONObject();
		StringEntity entity = null;
		try {
			params.put("content", content);
			entity = new StringEntity(params.toString(),"UTF-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpUtil.post(SubmitComActivity.this, url, entity, "application/json",
				new jsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						super.onSuccess(statusCode, headers, response);
					}
				});
	}
}
