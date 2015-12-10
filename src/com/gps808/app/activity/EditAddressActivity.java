package com.gps808.app.activity;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.bean.BnAddress;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.PreferenceUtils;
import com.gps808.app.utils.StringUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;

public class EditAddressActivity extends BaseActivity {
	private HeaderFragment headerFragment;
	private BnAddress bnAddress = new BnAddress();
	private boolean flag = true;
	private TextView address_default, address_delete;
	private TextView address_addr, address_mobi, address_name;
	private RadioGroup address_sex;
	private String uid;
	private int sex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alter_address);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setRightText("保存");
		if (StringUtils.isEmpty(getIntent().getStringExtra("address"))) {
			flag = false;
		} else {
			bnAddress = JSON.parseObject(getIntent().getStringExtra("address"),
					BnAddress.class);
		}

		address_default = (TextView) findViewById(R.id.address_default);
		address_delete = (TextView) findViewById(R.id.address_delete);

		address_addr = (TextView) findViewById(R.id.address_addr);
		address_mobi = (TextView) findViewById(R.id.address_mobi);
		address_name = (TextView) findViewById(R.id.address_name);
		address_sex = (RadioGroup) findViewById(R.id.address_sex);
		String title = "";
		if (flag) {
			title = "修改地址";

			address_default.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					loadData(
							edit(bnAddress.getId(), getText(address_name), sex,
									getText(address_mobi),
									getText(address_addr), "1"), "设置默认地址");
				}
			});
			address_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					loadData(del(bnAddress.getId()), "删除");
				}
			});
			headerFragment.setRightTextListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					loadData(
							edit(bnAddress.getId(), getText(address_name), sex,
									getText(address_mobi),
									getText(address_addr), bnAddress.getIs_default()), "修改");
				}
			});
			address_addr.setText(bnAddress.getAddr());
			address_name.setText(bnAddress.getName());
			address_mobi.setText(bnAddress.getMobile());
			if (bnAddress.getSex().equals("0")) {
				address_sex.check(R.id.sex_man);
			} else {
				address_sex.check(R.id.sex_woman);
			}
		} else {
			title = "添加地址";
			address_default.setVisibility(View.GONE);
			address_delete.setVisibility(View.GONE);

			headerFragment.setRightTextListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (check()) {
						loadData(
								add(getText(address_name), sex,
										getText(address_mobi),
										getText(address_addr), "0"), "添加");
					}

				}
			});
		}
		headerFragment.setTitleText(title);
		address_sex.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch (arg1) {
				case R.id.sex_woman:
					sex = 0;
					break;

				case R.id.sex_man:
					sex = 1;
					break;
				}
			}
		});

	}

	private String add(String name, int sex, String mobile, String addr,
			String is_default) {
		String url = UrlConfig.getAddressAdd(uid, name, sex, mobile, 35, 36, 0,
				addr, is_default);
		return url;

	}

	private String del(String addr_id) {
		String url = UrlConfig.getAddressDel(uid, addr_id);
		return url;

	}

	private String edit(String addr_id, String name, int sex, String mobile,
			String addr, String is_default) {
		String url = UrlConfig.getAddressEdit(uid, addr_id, name, sex, mobile,
				35, 36, 0, addr, is_default);
		return url;

	}

	private void loadData(String url, final String sign) {
//		HttpUtil.get(url, new jsonHttpResponseHandler() {
//			@Override
//			public void onSuccess(JSONObject arg0) {
//				// TODO Auto-generated method stub
//				if (Utils.requestOk(arg0)) {
//					Utils.ToastMessage(EditAddressActivity.this, sign + "成功");
//					finish();
//				} else {
//					Utils.ToastMessage(EditAddressActivity.this, sign + "失败");
//				}
//				super.onSuccess(arg0);
//			}
//		});
	}

	private String getText(TextView tv) {
		return tv.getText().toString();
	}

	private boolean check() {
		if (StringUtils.isEmpty(getText(address_addr))) {
			Utils.ToastMessage(EditAddressActivity.this, "请填写地址");
			return false;
		}
		if (StringUtils.isEmpty(getText(address_mobi))) {
			Utils.ToastMessage(EditAddressActivity.this, "请填写电话");
			return false;
		}
		if (!StringUtils.isPhone(getText(address_mobi))) {
			Utils.ToastMessage(EditAddressActivity.this, "电话不正确");
			return false;
		}
		if (StringUtils.isEmpty(getText(address_name))) {
			Utils.ToastMessage(EditAddressActivity.this, "请填写姓名");
			return false;
		}
		return true;

	}
}
