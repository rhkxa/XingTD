package com.gps808.app.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.adapter.GoodFragmentAdapter;
import com.gps808.app.bean.BnGallery;
import com.gps808.app.bean.BnProduct;
import com.gps808.app.bean.PDetailsBean;
import com.gps808.app.bean.Products;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.PreferenceUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;
import com.gps808.app.view.FancyButton;
import com.gps808.app.view.SingleSelectCheckBoxs;
import com.gps808.app.view.Indicator.CirclePageIndicator;
import com.gps808.app.view.SingleSelectCheckBoxs.OnSelectListener;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 商品详情的界面
 * 
 * @author rhk
 * 
 */
public class GoodsDetailActivity extends BaseActivity {
	private PDetailsBean data;

	private Map<Integer, String> mSpecData;
	private SingleSelectCheckBoxs goods_details_spec;

	private ImageView item_comment_image;
	private TextView item_comment_content;
	private TextView item_comment_name;
	private TextView item_comment_time;
	private TextView goods_detail_spec_name;
	private TextView goods_details_income;
	private TextView goods_details_rate_sales, goods_details_rate_comment;

	private ImageView goods_detail_toshare;
	private ImageView goods_detail_tocollect;
	private ImageView goods_detail_toshop;
	private ImageButton backBtn;

	private ImageButton currency_add;
	private ImageButton currency_subtract;
	private TextView show_text;

	private TextView goods_details_look_more;
	private FancyButton goods_details_join_shopping, goods_details_my_shopping;
	private FancyButton goods_details_comment;
	private TextView goods_details_comment_totals;
	private TextView goods_details_old_price;
	private TextView goods_details_new_price;

	private TextView goods_details_title;
	private ScrollView goods_details_scrollview;
	private WebView goods_detail_webview;
	private int index = 0;
	private String goods_id = null;
	private LinearLayout comment_layout;
	private ViewPager mPager;
	private CirclePageIndicator mIndicator;
	private GoodFragmentAdapter fAdapter;
	private List<BnGallery> gallery = new ArrayList<BnGallery>();
	private List<Products> products = new ArrayList<Products>();
	private LinearLayout incomeLayout;
	private FrameLayout pager_layout;
	private String product_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_details);
		init();
		loadData();
	}

	private void init() {
		// TODO Auto-generated method stub
		goods_id = getIntent().getStringExtra("goods_id");
	
		incomeLayout = (LinearLayout) findViewById(R.id.income_layout);
		
		pager_layout = (FrameLayout) findViewById(R.id.pager_layout);
		int screenWidth = Utils.getScreenWidth(GoodsDetailActivity.this);
		LayoutParams lp = pager_layout.getLayoutParams();
		lp.width = screenWidth;
		lp.height = screenWidth;
		pager_layout.setLayoutParams(lp);
		// pager_layout.setLayoutParams(new
		// FrameLayout.LayoutParams(screenWidth, screenWidth));
		goods_detail_spec_name = (TextView) findViewById(R.id.goods_detail_spec_name);
		goods_detail_toshare = (ImageView) findViewById(R.id.goods_detail_toshare);
		goods_detail_tocollect = (ImageView) findViewById(R.id.goods_detail_tocollect);
		goods_detail_toshop = (ImageView) findViewById(R.id.goods_detail_toshop);
		backBtn = (ImageButton) findViewById(R.id.backBtn);
		goods_details_rate_sales = (TextView) findViewById(R.id.goods_details_rate_sales);
		goods_details_rate_comment = (TextView) findViewById(R.id.goods_details_rate_comment);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		goods_detail_toshare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
					Utils.Share(GoodsDetailActivity.this, "冀牛网", data
							.getName(),"", ImageLoader.getInstance()
							.getDiskCache().get(data.getImg_url()).getPath());
			

			}
		});
		goods_detail_toshop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Utils.toShop(GoodsDetailActivity.this);
			}
		});

		goods_detail_tocollect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Utils.joinCollect(GoodsDetailActivity.this, goods_id);
			}
		});

		/** 轮播图 **/

		mPager = (ViewPager) findViewById(R.id.pager);
		fAdapter = new GoodFragmentAdapter(getSupportFragmentManager(), gallery);
		mPager.setAdapter(fAdapter);
//		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
//		mIndicator.setViewPager(mPager);
		goods_details_look_more = (TextView) findViewById(R.id.goods_details_look_more);
		goods_details_look_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GoodsDetailActivity.this,
						CommentActivity.class);
				intent.putExtra("goods_id", goods_id);
				startActivity(intent);
			}
		});
		goods_details_comment = (FancyButton) findViewById(R.id.goods_details_comment);
		comment_layout = (LinearLayout) findViewById(R.id.comment_layout);
		item_comment_name = (TextView) findViewById(R.id.item_comment_name);
		item_comment_time = (TextView) findViewById(R.id.item_comment_time);
		item_comment_content = (TextView) findViewById(R.id.item_comment_content);
		goods_details_income = (TextView) findViewById(R.id.goods_details_income);
		item_comment_image = (ImageView) findViewById(R.id.item_comment_image);
		show_text = (TextView) findViewById(R.id.show_text);
		currency_add = (ImageButton) findViewById(R.id.currency_add);
		currency_subtract = (ImageButton) findViewById(R.id.currency_subtract);
		goods_details_comment_totals = (TextView) findViewById(R.id.goods_details_comment_totals);
		goods_details_join_shopping = (FancyButton) findViewById(R.id.goods_details_join_shopping);
		goods_details_my_shopping = (FancyButton) findViewById(R.id.goods_details_my_shopping);
		goods_details_old_price = (TextView) findViewById(R.id.goods_details_old_price);
		goods_details_new_price = (TextView) findViewById(R.id.goods_details_new_price);
		goods_details_title = (TextView) findViewById(R.id.goods_details_title);
		goods_details_spec = (SingleSelectCheckBoxs) findViewById(R.id.goods_details_spec);
		goods_details_scrollview = (ScrollView) findViewById(R.id.goods_details_scrollview);
		goods_detail_webview = (WebView) findViewById(R.id.goods_detail_webview);
		// goods_detail_webview.getSettings().setJavaScriptEnabled(true);
		goods_detail_webview.getSettings().setLayoutAlgorithm(
				LayoutAlgorithm.NARROW_COLUMNS);

		// goods_detail_webview.getSettings().setBuiltInZoomControls(false);
		// goods_detail_webview.getSettings().setUseWideViewPort(true);
		// goods_detail_webview.getSettings().setLoadWithOverviewMode(true);
		// 会出现放大缩小的按钮
		// goods_detail_webview.setWebViewClient(new WebViewClient() {
		// @Override
		// public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// // TODO Auto-generated method stub
		// view.loadUrl(url);
		// return super.shouldOverrideUrlLoading(view, url);
		// }
		// });
		goods_details_spec.setOnSelectListener(new OnSelectListener() {

			@Override
			public void onSelect(int position) {
				// TODO Auto-generated method stub
				if (position >= 0) {
					refresh(data.getProducts().get(position));
				}

			}
		});

		goods_details_my_shopping.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
					products.clear();
					Products ps = new Products();
					ps.setProduct_id(product_id);
					ps.setQuantity(Integer.parseInt(show_text.getText()
							.toString()));
					products.add(ps);
					Intent intent = new Intent(GoodsDetailActivity.this,
							FinishOrderActivity.class);
					intent.putExtra("products", JSON.toJSONString(products));
					startActivity(intent);
				
			}
		});

		goods_details_join_shopping.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				int quantity = Integer.parseInt(show_text.getText().toString());
				Utils.joinShop(GoodsDetailActivity.this, product_id, quantity);
				// Utils.ToastMessage(GoodsDetailActivity.this, "请选择商品数量");

			}
		});
		currency_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				show_text.setText(String.valueOf(Integer.parseInt(show_text
						.getText().toString()) + 1));
			}
		});
		currency_subtract.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (Integer.parseInt(show_text.getText().toString()) > 1) {
					show_text.setText(String.valueOf(Integer.parseInt(show_text
							.getText().toString()) - 1));
				}
			}
		});
		goods_details_comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// commentDialog = new CommentDialog(GoodsDetailActivity.this,
				// bean.getProduction().getId());
				// // 显示窗口
				// commentDialog.show();
				
					Intent intent = new Intent(GoodsDetailActivity.this,
							SubmitComActivity.class);
					intent.putExtra("goods_img", data.getImg_url());
					intent.putExtra("goods_id", data.getId());
					intent.putExtra("goods_name", data.getName());
					intent.putExtra("goods_price", data.getPrice());
					startActivity(intent);
				

			}
		});

	}

	// 获取网络数据
	private void loadData() {
		if (Utils.isNetWorkConnected(GoodsDetailActivity.this)) {
			showProgressDialog(GoodsDetailActivity.this, "");
			String url = UrlConfig.getGoodsInfo(goods_id);

			HttpUtil.get(url, new jsonHttpResponseHandler() {
//				@Override
//				public void onSuccess(JSONObject arg0) {
//					// TODO Auto-generated method stub
//					if (Utils.requestOk(arg0)) {
//						data = JSON.parseObject(Utils.getResult(arg0),
//								PDetailsBean.class);
//						setValue();
//					} else {
//						Utils.ToastMessage(GoodsDetailActivity.this,
//								Utils.getKey(arg0, "msg"));
//					}
//					super.onSuccess(arg0);
//				}

				

			});
		} else {
			Utils.showSuperCardToast(GoodsDetailActivity.this, getResources()
					.getString(R.string.network_not_connected));
		}
	}

	private void setValue() {
		// TODO Auto-generated method stub
		product_id = data.getProducts().get(0).getId();
		mSpecData = new HashMap<Integer, String>();
		int size = data.getProducts().size();
		for (int i = 0; i < size; i++) {
			if (data.getProducts().get(i).getSpecs().size()>0) {
				mSpecData.put(i, data.getProducts().get(i).getSpecs().get(0)
						.getSpec_value());
				goods_detail_spec_name.setText(data.getProducts().get(0)
						.getSpecs().get(0).getSpec_name());
			}

		}
		goods_details_spec.setData(mSpecData);

		goods_details_comment_totals.setText("产品评价  ("
				+ data.getComment_count() + ")");

		goods_details_rate_sales.setText(data.getBuy_count() + "组");
		goods_details_rate_comment.setText(data.getComment_count() + "%");
		goods_details_old_price.getPaint()
				.setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

		goods_details_old_price.setText("市场价￥" + data.getProducts().get(0).getMktprice());
		goods_details_income.setText(data.getProducts().get(0).getAgent_income());
		goods_details_new_price.setText("￥" + data.getProducts().get(0).getPrice());
		goods_details_title.setText(data.getName());
		goods_detail_webview.loadData(data.getIntro(),
				"text/html; charset=UTF-8", "UTF-8");
		gallery.clear();
		gallery.addAll(data.getGallery());
		fAdapter.notifyDataSetChanged();
		if (data.getComment_count() > 0) {
			comment_layout.setVisibility(View.GONE);
			// ImageLoader.getInstance().displayImage(
			// data.getCommentList().get(0).getUserimg(),
			// item_comment_image);
			// item_comment_content.setText(data.getCommentList().get(0)
			// .getContent());
			// item_comment_name.setText(data.getCommentList().get(0)
			// .getUsername());
			// item_comment_time.setText(data.getCommentList().get(0)
			// .getInputTime());
		} else {
			comment_layout.setVisibility(View.GONE);
		}
	}

	private void refresh(BnProduct product) {
		product_id = product.getId();
		goods_details_old_price.setText("市场价￥" + product.getMktprice());
		goods_details_income.setText(product.getAgent_income());
		goods_details_new_price.setText("￥" + product.getPrice());
	}

	// class CommentDialog extends Dialog {
	//
	// private Button btn_ok, btn_cancel;
	// private EditText comment;
	// private Context context;
	// private String id;
	//
	// public CommentDialog(Context context, String id) {
	// super(context, R.style.Dialog);
	// // TODO Auto-generated constructor stub
	// this.context = context;
	// this.id = id;
	//
	// }
	//
	// @Override
	// protected void onCreate(Bundle savedInstanceState) {
	// // TODO Auto-generated method stub
	// super.onCreate(savedInstanceState);
	// setContentView(R.layout.dialog_comment);
	// init();
	// }
	//
	// private void init() {
	// btn_ok = (Button) findViewById(R.id.btn_ok);
	// comment = (EditText) findViewById(R.id.comment_et);
	// btn_ok.setOnClickListener(new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// // TODO Auto-generated method stub
	// if (!StringUtils.isEmpty(comment.getText().toString())) {
	//
	// String url = UrlConfig.submitComment(context, id,
	// comment.getText().toString());
	// HttpUtil.get(url, new JsonHttpResponseHandler() {
	// @Override
	// public void onSuccess(JSONObject arg0) {
	// // TODO Auto-generated method stub
	// BaseBean bean = JSON.parseObject(
	// arg0.toString(), BaseBean.class);
	// if (bean.getStatusCode().equals("200")) {
	// Utils.ToastMessage(context, "评论成功");
	// loadData();
	// dismiss();
	// } else {
	// Utils.ToastMessage(context, "评论失败");
	// }
	// super.onSuccess(arg0);
	// }
	// });
	// } else {
	// Utils.ToastMessage(context, "请输入评论内容");
	// }
	// }
	// });
	// }
	// }
}
