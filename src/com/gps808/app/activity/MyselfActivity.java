package com.gps808.app.activity;

import android.content.Intent;
import android.os.Bundle;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gps808.app.R;
import com.gps808.app.bean.BnUser;
import com.gps808.app.fragment.FragmentMyself;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.Utils;
import com.gps808.app.view.CircleImageView;
import com.gps808.app.view.PengButton;


public class MyselfActivity extends BaseActivity{
	private ImageView alter;
	private TextView mynickname;
	private CircleImageView my_headimage;
	private LinearLayout my_center, my_order, my_voucher, my_score, my_share,
			my_help;
	private PengButton my_address, my_join, my_collet, my_wallet;
	private BnUser user;
	private LinearLayout myself_call,myself_edit;

	public static FragmentMyself newInstance() {
		FragmentMyself fragment = new FragmentMyself();
		return fragment;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself);
		init();
	}



	private void init() {
		// TODO Auto-generated method stub

//		alter = (ImageView)  findViewById(R.id.alter_person);
//		alter.setOnClickListener(click);
//
//		my_address = (PengButton)  findViewById(R.id.my_address);
//		my_address.setOnClickListener(click);

//		my_join = (PengButton)  findViewById(R.id.my_join);
//		 my_join.setOnClickListener(click);

//		my_collet = (PengButton)  findViewById(R.id.my_collet);
//		my_collet.setOnClickListener(click);
//
//		my_wallet = (PengButton)  findViewById(R.id.my_wallet);
//		my_wallet.setOnClickListener(click);

		// linear
//		my_center = (LinearLayout)  findViewById(R.id.my_center);
//		my_center.setOnClickListener(click);
//		my_order = (LinearLayout)  findViewById(R.id.my_order);
//		my_order.setOnClickListener(click);
//		my_voucher = (LinearLayout)  findViewById(R.id.my_voucher);
//		my_voucher.setOnClickListener(click);
//		my_score = (LinearLayout)  findViewById(R.id.my_score);
//		my_score.setOnClickListener(click);
//		my_share = (LinearLayout)  findViewById(R.id.my_share);
//		my_share.setOnClickListener(click);
//		my_help = (LinearLayout)  findViewById(R.id.my_help);
	
//		my_help.setOnClickListener(click);
//		mynickname = (TextView)  findViewById(R.id.my_nickname);
//
//		my_headimage = (CircleImageView)  findViewById(R.id.my_headimage);

//		alter.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(getActivity(), SetupActivity.class);
//				startActivity(intent);
//			}
//		});
//		myself_call = (LinearLayout)  findViewById(R.id.myself_call);
//		myself_call.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Utils.callPhone(getActivity(), getResources().getString(R.string.server_call));
//			}
//		});
//		myself_edit = (LinearLayout)  findViewById(R.id.myself_edit);
		// my_headimage.setOnClickListener(click);

//	}

//	private OnClickListener click = new OnClickListener() {
//
//		@Override
//		public void onClick(View arg0) {
//			// TODO Auto-generated method stub
//			
//				Intent intent = new Intent();
//				Class cls = null;
//				switch (arg0.getId()) {
//				case R.id.my_center:
//					cls = PersonActivity.class;
//
//					break;
//				case R.id.my_order:
//					cls = OrderActivity.class;
//					break;
//				case R.id.my_voucher:
//					cls = VoucherActivity.class;
//					break;
//				case R.id.my_score:
//					cls = ScoreActivity.class;
//					break;
//				case R.id.my_share:
//					cls = BonusActivity.class;
//					break;
//				case R.id.my_help:
//					cls = HelpActivity.class;
//					break;
//
//				case R.id.my_address:
//					cls = AddressActivity.class;
//					break;
//				case R.id.my_collet:
//					cls = CollectActivity.class;
//					break;
//
//				case R.id.my_wallet:
//					cls = WalletActivity.class;
//					break;
//				case R.id.my_join:
//					if (user.getIs_agent().equals("1")) {
//						cls = BillActivity.class;
//					} else {
//						cls = JoinActivity.class;
//					}
//					break;
//				}
//				intent.setClass(MyselfActivity.this, cls);
//				startActivity(intent);
//			
//		}
//	};
}
}
