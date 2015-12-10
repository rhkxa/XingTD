package com.gps808.app.activity;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.adapter.AddressListViewAdapter;
import com.gps808.app.bean.AddressBean;
import com.gps808.app.bean.BnAddress;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.PreferenceUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;

/**
 * 我的地址的界面
 * 
 * @author rhk
 * 
 */
public class AddressActivity extends BaseActivity {
	private ListView addressListView;
	private AddressListViewAdapter mAdapter;
	private HeaderFragment headerFragment;
	private List<BnAddress> addressBeans = new ArrayList<BnAddress>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		init();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		loadData();
		super.onResume();
	}

	private void init() {
		// TODO Auto-generated method stub
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("我的地址");
		headerFragment.setRightText("新增地址");
		headerFragment.setRightTextListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AddressActivity.this,
						EditAddressActivity.class);
				startActivity(intent);
			}
		});

		addressListView = (ListView) findViewById(R.id.address_listview);
		mAdapter = new AddressListViewAdapter(AddressActivity.this,
				addressBeans);
		addressListView.setAdapter(mAdapter);
		addressListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (getIntent().getBooleanExtra("get", false)) {
					Intent data = new Intent();
					data.putExtra("toaddress",
							JSON.toJSONString(addressBeans.get(arg2)));
					setResult(RESULT_OK, data);
					finish();
				}
			}
		});
		// mAdapter.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(int id) {
		// // TODO Auto-generated method stub
		// // addressBeans.get(id).setIsDefault("1");
		// // updateAddrObj(addressBeans.get(id));
		// }
		//
		// @Override
		// public void onDelete(int id) {
		// // TODO Auto-generated method stub
		// // addressBeans.remove(id);
		// // mAdapter.notifyDataSetChanged();
		// // deleteAddrObj(addressBeans.get(id).getId());
		// }
		//
		// @Override
		// public void onEdit(int id) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub

		super.onPause();
	}

	private void loadData() {
		
//		HttpUtil.get(UrlConfig.getAddressList(uid),
//				new jsonHttpResponseHandler() {
//					@Override
//					public void onSuccess(JSONObject arg0) {
//						// TODO Auto-generated method stub
//						AddressBean data = JSON.parseObject(
//								Utils.getResult(arg0), AddressBean.class);
//						addressBeans.clear();
//						addressBeans.addAll(data.getDatas());
//						mAdapter.notifyDataSetChanged();
//						super.onSuccess(arg0);
//					}
//				});
	}
	/**
	 * class AddAddressDialog extends Dialog {
	 * 
	 * private TextView dialog_title; private EditText dialog_add_address_name;
	 * private EditText dialog_add_address_phone; private EditText
	 * dialog_add_address_introduce; private Button dialog_alter_password_ok;
	 * private Context context;
	 * 
	 * public AddAddressDialog(Context context) { super(context,
	 * R.style.Dialog); // TODO Auto-generated constructor stub this.context =
	 * context;
	 * 
	 * }
	 * 
	 * @Override protected void onCreate(Bundle savedInstanceState) { // TODO
	 *           Auto-generated method stub super.onCreate(savedInstanceState);
	 *           setContentView(R.layout.dialog_add_address); init(); }
	 * 
	 *           private void init() { // TODO Auto-generated method stub
	 *           dialog_title = (TextView) findViewById(R.id.dialog_title);
	 *           dialog_title.setText("新增地址"); dialog_add_address_name =
	 *           (EditText) findViewById(R.id.dialog_add_address_name);
	 *           dialog_add_address_phone = (EditText)
	 *           findViewById(R.id.dialog_add_address_phone);
	 *           dialog_add_address_introduce = (EditText)
	 *           findViewById(R.id.dialog_add_address_introduce);
	 *           dialog_alter_password_ok = (Button)
	 *           findViewById(R.id.dialog_alter_password_ok);
	 *           dialog_alter_password_ok .setOnClickListener(new
	 *           View.OnClickListener() {
	 * @Override public void onClick(View arg0) { // TODO Auto-generated method
	 *           stub
	 * 
	 *           if (!StringUtils.isEmpty(dialog_add_address_name
	 *           .getText().toString())) { if (!StringUtils
	 *           .isEmpty(dialog_add_address_phone .getText().toString())) { if
	 *           (!StringUtils .isEmpty(dialog_add_address_introduce
	 *           .getText().toString())) { // addAddrObj(dialog_add_address_name
	 *           // .getText().toString(), // dialog_add_address_phone //
	 *           .getText().toString(), // dialog_add_address_introduce //
	 *           .getText().toString()); dismiss();
	 * 
	 *           } else { Utils.ToastMessage(context, "姓名不能为空"); } } else {
	 *           Utils.ToastMessage(context, "电话不能为空"); } } else {
	 *           Utils.ToastMessage(context, "地址不能为空"); } } });
	 * 
	 *           } }
	 * 
	 *           class EditAddressDialog extends Dialog {
	 * 
	 *           private TextView dialog_title; private EditText
	 *           dialog_add_address_name; private EditText
	 *           dialog_add_address_phone; private EditText
	 *           dialog_add_address_introduce; private Button
	 *           dialog_alter_password_ok; private Context context; private int
	 *           id;
	 * 
	 *           public EditAddressDialog(Context context, int id) {
	 *           super(context, R.style.Dialog); // TODO Auto-generated
	 *           constructor stub this.context = context; this.id = id;
	 * 
	 *           }
	 * @Override protected void onCreate(Bundle savedInstanceState) { // TODO
	 *           Auto-generated method stub super.onCreate(savedInstanceState);
	 *           setContentView(R.layout.dialog_add_address); init(); }
	 * 
	 *           private void init() { // TODO Auto-generated method stub
	 *           dialog_title = (TextView) findViewById(R.id.dialog_title);
	 *           dialog_title.setText("修改地址"); dialog_add_address_name =
	 *           (EditText) findViewById(R.id.dialog_add_address_name);
	 *           dialog_add_address_phone = (EditText)
	 *           findViewById(R.id.dialog_add_address_phone);
	 *           dialog_add_address_introduce = (EditText)
	 *           findViewById(R.id.dialog_add_address_introduce);
	 *           dialog_alter_password_ok = (Button)
	 *           findViewById(R.id.dialog_alter_password_ok);
	 *           dialog_add_address_introduce.setText(addressBeans.get(id)
	 *           .getFull_address());
	 *           dialog_add_address_phone.setText(addressBeans.get(id).get);
	 *           dialog_add_address_name
	 *           .setText(addressBeans.get(id).getName()); //
	 *           dialog_alter_password_ok // .setOnClickListener(new
	 *           View.OnClickListener() { // // @Override // public void
	 *           onClick(View arg0) { // // TODO Auto-generated method stub //
	 *           if (!StringUtils.isEmpty(dialog_add_address_name //
	 *           .getText().toString())) { // if (!StringUtils //
	 *           .isEmpty(dialog_add_address_phone // .getText().toString())) {
	 *           // if (!StringUtils // .isEmpty(dialog_add_address_introduce //
	 *           .getText().toString())) { // addressBeans.get(id).setAddress(
	 *           // dialog_add_address_introduce // .getText().toString()); //
	 *           addressBeans.get(id).setName( // dialog_add_address_name //
	 *           .getText().toString()); // addressBeans.get(id).setTel( //
	 *           dialog_add_address_phone // .getText().toString()); //
	 *           updateAddrObj(addressBeans.get(id)); // dismiss(); // // } else
	 *           { // Utils.ToastMessage(context, "姓名不能为空"); // } // } else { //
	 *           Utils.ToastMessage(context, "电话不能为空"); // } // } else { //
	 *           Utils.ToastMessage(context, "地址不能为空"); // } // } // });
	 * 
	 *           } }
	 * 
	 *           /*private void addAddrObj(String name, String tel, String
	 *           address) { if (Utils.isNetWorkConnected(AddressActivity.this))
	 *           { showProgressDialog(AddressActivity.this,"正在新增，等稍等"); String
	 *           url = UrlConfig.addAddrObj(AddressActivity.this); RequestParams
	 *           params = new RequestParams(); params.put("addrObj.name", name);
	 *           params.put("addrObj.tel", tel); params.put("addrObj.address",
	 *           address); params.put("addrObj.reserved", "");
	 *           params.put("addrObj.isDefault", "0"); HttpUtil.post(url,
	 *           params, new jsonHttpResponseHandler() {
	 * @Override public void onSuccess(JSONObject arg0) { // TODO Auto-generated
	 *           method stub BaseBean base = JSON.parseObject(arg0.toString(),
	 *           BaseBean.class); if (base.getStatusCode().equals("200")) {
	 *           Utils.ToastMessage(AddressActivity.this, "地址添加成功");
	 *           listAddrObj(); mAdapter.notifyDataSetChanged();
	 * 
	 *           } super.onSuccess(arg0); } }); } else {
	 *           Utils.showSuperCardToast(AddressActivity.this, getResources()
	 *           .getString(R.string.network_not_connected)); } }
	 * 
	 *           private void listAddrObj() { if
	 *           (Utils.isNetWorkConnected(AddressActivity.this)) {
	 *           showProgressDialog(AddressActivity.this,"加载中……"); String url =
	 *           UrlConfig.listAddrObj(AddressActivity.this); HttpUtil.get(url,
	 *           new jsonHttpResponseHandler() {
	 * @Override public void onSuccess(JSONObject arg0) { // TODO Auto-generated
	 *           method stub AddressListBean addressListBean = JSON.parseObject(
	 *           arg0.toString(), AddressListBean.class); if
	 *           (addressListBean.getStatusCode().equals("200")) {
	 *           addressBeans.clear();
	 *           addressBeans.addAll(addressListBean.getAddressList());
	 *           mAdapter.notifyDataSetChanged(); } super.onSuccess(arg0); } });
	 *           } else { Utils.showSuperCardToast(AddressActivity.this,
	 *           getResources() .getString(R.string.network_not_connected)); } }
	 * 
	 *           private void updateAddrObj(AddressBean addressBean) { if
	 *           (Utils.isNetWorkConnected(AddressActivity.this)) {
	 *           showProgressDialog(AddressActivity.this,"更新中……"); String url =
	 *           UrlConfig.updateAddress(AddressActivity.this); RequestParams
	 *           params = new RequestParams(); params.put("addrObj.id",
	 *           addressBean.getId()); params.put("addrObj.name",
	 *           addressBean.getName()); params.put("addrObj.tel",
	 *           addressBean.getTel()); params.put("addrObj.address",
	 *           addressBean.getAddress()); params.put("addrObj.reserved",
	 *           addressBean.getReserved()); params.put("addrObj.isDefault",
	 *           addressBean.getIsDefault());
	 *           System.out.println("jia"+params.toString()); HttpUtil.post(url,
	 *           params, new jsonHttpResponseHandler() {
	 * @Override public void onSuccess(JSONObject arg0) { // TODO Auto-generated
	 *           method stub BaseBean base = JSON.parseObject(arg0.toString(),
	 *           BaseBean.class); if (base.getStatusCode().equals("200")) {
	 *           Utils.ToastMessage(AddressActivity.this, "修改成功");
	 *           listAddrObj(); } super.onSuccess(arg0); } }); } else {
	 *           Utils.showSuperCardToast(AddressActivity.this, getResources()
	 *           .getString(R.string.network_not_connected)); } }
	 * 
	 *           private void deleteAddrObj(String aid) { if
	 *           (Utils.isNetWorkConnected(AddressActivity.this)) { String url =
	 *           UrlConfig.deleteAddrObj(AddressActivity.this, aid);
	 *           HttpUtil.get(url, new jsonHttpResponseHandler() {
	 * @Override public void onSuccess(JSONObject arg0) { // TODO Auto-generated
	 *           method stub BaseBean base = JSON.parseObject(arg0.toString(),
	 *           BaseBean.class); if (base.getStatusCode().equals("200")) {
	 *           Utils.ToastMessage(AddressActivity.this, "删除成功");
	 *           listAddrObj(); } super.onSuccess(arg0); } }); } else {
	 *           Utils.showSuperCardToast(AddressActivity.this, getResources()
	 *           .getString(R.string.network_not_connected));
	 * 
	 *           } }
	 */
}
