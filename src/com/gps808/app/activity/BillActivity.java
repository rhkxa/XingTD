package com.gps808.app.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.adapter.BillListViewAdapter;
import com.gps808.app.bean.BillBean;
import com.gps808.app.bean.BnBill;
import com.gps808.app.dialog.TextDialog;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.PreferenceUtils;
import com.gps808.app.utils.StringUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;

public class BillActivity extends BaseActivity {
	private HeaderFragment headerFragment;
	private ListView bill_list;
	private BillListViewAdapter mAdapter;
	private List<BnBill> data = new ArrayList<BnBill>();

	private TextView bill_my_rank, bill_count, bill_amount;
	private LinearLayout bill_ranking;
	private Button bill_getmoney, bill_setmoney;
	private ImageView bill_share_pengyou, bill_share_wechat, bill_share_weibo,
			bill_share_qq;

	private static final String FILE_NAME = "/share_pic.jpg";
	public static String TEST_IMAGE;
	private String path, title, sharetext;
	private TextDialog textDialog;
	private String uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bill);
		init();
		loadData();
	}

	private void init() {
		// TODO Auto-generated method stub
		
		title = getResources().getString(R.string.app_name);
		sharetext = "下载加入冀牛时尚超市，天天享受水果盛宴";
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("我的合伙人");
		bill_share_qq = (ImageView) findViewById(R.id.bill_share_qq);
		bill_share_qq.setOnClickListener(shareClickListener);
		bill_share_pengyou = (ImageView) findViewById(R.id.bill_share_pengyou);
		bill_share_pengyou.setOnClickListener(shareClickListener);
		bill_share_wechat = (ImageView) findViewById(R.id.bill_share_wechat);
		bill_share_wechat.setOnClickListener(shareClickListener);
		bill_share_weibo = (ImageView) findViewById(R.id.bill_share_weibo);
		bill_share_weibo.setOnClickListener(shareClickListener);
		bill_list = (ListView) findViewById(R.id.bill_list);
		mAdapter = new BillListViewAdapter(BillActivity.this, data);
		bill_list.setAdapter(mAdapter);
		bill_my_rank = (TextView) findViewById(R.id.bill_my_rank);
		bill_count = (TextView) findViewById(R.id.bill_count);
		bill_amount = (TextView) findViewById(R.id.bill_amount);
		bill_ranking = (LinearLayout) findViewById(R.id.bill_ranking);
		bill_getmoney = (Button) findViewById(R.id.bill_getmoney);
		bill_setmoney = (Button) findViewById(R.id.bill_setmoney);
		bill_share_pengyou = (ImageView) findViewById(R.id.bill_share_pengyou);
		bill_share_wechat = (ImageView) findViewById(R.id.bill_share_wechat);
		bill_share_weibo = (ImageView) findViewById(R.id.bill_share_weibo);
		bill_share_qq = (ImageView) findViewById(R.id.bill_share_qq);

		bill_ranking.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(BillActivity.this,
						FranchiseeActivity.class);
				startActivity(intent);
			}
		});
		bill_getmoney.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				textDialog = new TextDialog(BillActivity.this, "提现");
				textDialog.setOnClickListener(new TextDialog.OnClickListener() {

					@Override
					public void onItemClick(int money) {
						// TODO Auto-generated method stub
						getAgent(UrlConfig.getAgentOut(uid, money), "");
					}
				});
				textDialog.show();
			}
		});
		bill_setmoney.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				textDialog = new TextDialog(BillActivity.this, "转入");
				textDialog.setOnClickListener(new TextDialog.OnClickListener() {

					@Override
					public void onItemClick(int money) {
						// TODO Auto-generated method stub
						getAgent(UrlConfig.getAgentIn(uid, money), "提现");
					}
				});
				textDialog.show();
			}
		});

	}

	private OnClickListener shareClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			initImagePath();
			switch (arg0.getId()) {
			case R.id.bill_share_pengyou:

				Utils.shareOneWechatMoments(BillActivity.this, title,
						sharetext, path, TEST_IMAGE);
				break;
			case R.id.bill_share_wechat:

				Utils.shareOneWechat(BillActivity.this, title, sharetext, path,
						TEST_IMAGE);
				break;
			case R.id.bill_share_weibo:
				Utils.shareOneSina(BillActivity.this, sharetext, path,
						TEST_IMAGE);
				break;
			case R.id.bill_share_qq:
				Utils.shareOneQQ(BillActivity.this, title, sharetext, path,
						TEST_IMAGE);
				break;

			}
		}
	};

	private void loadData() {
//		String uid = PreferenceUtils.getInstance(BillActivity.this)
//				.getStringValue("uid");
//		HttpUtil.get(UrlConfig.getAgent(uid), new jsonHttpResponseHandler() {
//			@Override
//			public void onStart() {
//				// TODO Auto-generated method stub
//				showProgressDialog(BillActivity.this, "正在加载中……");
//				super.onStart();
//			}
//
//			@Override
//			public void onSuccess(JSONObject arg0) {
//				// TODO Auto-generated method stub
//				BillBean bill = JSON.parseObject(Utils.getResult(arg0),
//						BillBean.class);
//				setValue(bill);
//				super.onSuccess(arg0);
//			}
//
//		});
	}

	private void getAgent(String url, final String tag) {
//		HttpUtil.get(url, new jsonHttpResponseHandler() {
//			@Override
//			public void onStart() {
//				// TODO Auto-generated method stub
//				showProgressDialog(BillActivity.this, "正在申请中……");
//				super.onStart();
//			}
//
//			@Override
//			public void onSuccess(JSONObject arg0) {
//				// TODO Auto-generated method stub
//				if (Utils.requestOk(arg0)) {
//					Utils.ToastMessage(BillActivity.this, tag
//							+ "申请成功，客服会24小时之内联系您");
//				} else {
//					Utils.ToastMessage(BillActivity.this,
//							Utils.getKey(arg0, "msg"));
//				}
//				super.onSuccess(arg0);
//			}
//		});
	}

	private void setValue(BillBean bill) {
		// TODO Auto-generated method stub
		data.addAll(bill.getBills());
		mAdapter.notifyDataSetChanged();
		bill_my_rank.setText("我的排名：" + bill.getRank());
		bill_count.setText("收益余额：" + bill.getBalance());
		bill_amount.setText("总收益：" + bill.getRevenue());
	}

	// 把图片从drawable复制到sdcard中
	// copy the picture from the drawable to sdcard
	private void initImagePath() {
		try {
			if (Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())
					&& Environment.getExternalStorageDirectory().exists()) {
				TEST_IMAGE = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + FILE_NAME;
			} else {
				TEST_IMAGE = getApplication().getFilesDir().getAbsolutePath()
						+ FILE_NAME;
			}
			File file = new File(TEST_IMAGE);
			if (!file.exists()) {
				file.createNewFile();
				Bitmap pic = BitmapFactory.decodeResource(getResources(),
						R.drawable.ic_launcher);
				FileOutputStream fos = new FileOutputStream(file);
				pic.compress(CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
			}
		} catch (Throwable t) {
			t.printStackTrace();
			TEST_IMAGE = null;
		}
	}
}
