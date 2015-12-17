package com.gps808.app.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.adapter.KeyListViewAdapter;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.StringUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;
import com.gps808.app.view.SingleSelectCheckBoxs;
import com.gps808.app.view.SingleSelectCheckBoxs.OnSelectListener;

public class SearchActivity extends BaseActivity {
	private ImageButton backBtn;
	private Button layout_search_bar_button;
	private EditText layout_search_bar_edittext;
	private int searchtype;
	private ListView keyListView;
	private SingleSelectCheckBoxs key_single;
	private Map<Integer, String> mKeyData;
	private KeyListViewAdapter keyAdapter;
	private List<String> keylist = new ArrayList<String>();
	private Spinner layout_search_bar_spinner;
	private static final String[] searchArray = { "关键字", "品牌" };
	private ArrayAdapter<String> spinnerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		init();
		// searchkey();
	}

	private void init() {
		// TODO Auto-generated method stub

		
		backBtn = (ImageButton) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		layout_search_bar_spinner = (Spinner) findViewById(R.id.layout_search_bar_spinner);
		spinnerAdapter = new ArrayAdapter<String>(SearchActivity.this,
				android.R.layout.simple_spinner_item, searchArray);
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item);

		layout_search_bar_spinner.setAdapter(spinnerAdapter);
		layout_search_bar_spinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View v,
							int arg2, long arg3) {
						TextView textView = (TextView) v;
						textView.setTextColor(getResources().getColor(
								R.color.black));
						if (arg2 == 0) {
							searchtype = 0;
						} else {
							searchtype = 1;
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						searchtype = 0;
					}

				});
		layout_search_bar_edittext = (EditText) findViewById(R.id.layout_search_bar_edittext);

		key_single=(SingleSelectCheckBoxs) findViewById(R.id.key_single);	
		
		key_single.setOnSelectListener(new OnSelectListener() {
			
			@Override
			public void onSelect(int position) {
				// TODO Auto-generated method stub
				layout_search_bar_edittext.setText(keylist.get(position));
				layout_search_bar_button.performClick();
			}
		});
		keyListView = (ListView) findViewById(R.id.key_list);
		keyAdapter = new KeyListViewAdapter(SearchActivity.this, keylist);
		keyListView.setAdapter(keyAdapter);
		keyListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				layout_search_bar_edittext.setText(keylist.get(arg2));
				layout_search_bar_button.performClick();
			}
		});
		layout_search_bar_button = (Button) findViewById(R.id.layout_search_bar_button);
		layout_search_bar_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String key = layout_search_bar_edittext.getText().toString();
				if (StringUtils.isEmpty(key)) {
					Utils.ToastMessage(SearchActivity.this, "请输入搜索关键词");
					return;
				}
				keylist.add(key);
				
			

			}
		});
	}

	private void searchkey() {
//		if (Utils.isNetWorkConnected(SearchActivity.this)) {
//			showProgressDialog(SearchActivity.this, "加载中……");
//			String url = UrlConfig.getKeyList();
//			HttpUtil.get(url, new jsonHttpResponseHandler() {
//				@Override
//				public void onSuccess(JSONObject arg0) {
//					// TODO Auto-generated method stub
//
//					keylist.clear();
//
//					keyAdapter.notifyDataSetChanged();
//
//					super.onSuccess(arg0);
//				}
//
//			});
//		} else {
//			Utils.showSuperCardToast(SearchActivity.this, getResources()
//					.getString(R.string.network_not_connected));
//		}
	}
}
