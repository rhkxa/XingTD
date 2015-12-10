package com.gps808.app.fragment;



import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.activity.AddressActivity;
import com.gps808.app.activity.BillActivity;
import com.gps808.app.activity.BonusActivity;
import com.gps808.app.activity.CollectActivity;
import com.gps808.app.activity.HelpActivity;
import com.gps808.app.activity.JoinActivity;
import com.gps808.app.activity.OrderActivity;
import com.gps808.app.activity.PersonActivity;
import com.gps808.app.activity.ScoreActivity;
import com.gps808.app.activity.SetupActivity;
import com.gps808.app.activity.VoucherActivity;
import com.gps808.app.activity.WalletActivity;
import com.gps808.app.bean.BnUser;
import com.gps808.app.utils.BaseFragment;
import com.gps808.app.utils.PreferenceUtils;
import com.gps808.app.utils.Utils;
import com.gps808.app.view.CircleImageView;
import com.gps808.app.view.PengButton;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 我的页面
 * 
 * @author rhk
 * 
 */
public class FragmentMyself extends BaseFragment {

	private View view;
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_my, null);
		init();

		return view;
	}

	private void init() {
		// TODO Auto-generated method stub

		alter = (ImageView) view.findViewById(R.id.alter_person);
		alter.setOnClickListener(click);
		// button
		my_address = (PengButton) view.findViewById(R.id.my_address);
		my_address.setOnClickListener(click);

//		my_join = (PengButton) view.findViewById(R.id.my_join);
//		 my_join.setOnClickListener(click);

//		my_collet = (PengButton) view.findViewById(R.id.my_collet);
//		my_collet.setOnClickListener(click);
//
//		my_wallet = (PengButton) view.findViewById(R.id.my_wallet);
//		my_wallet.setOnClickListener(click);

		// linear
		my_center = (LinearLayout) view.findViewById(R.id.my_center);
		my_center.setOnClickListener(click);
		my_order = (LinearLayout) view.findViewById(R.id.my_order);
		my_order.setOnClickListener(click);
		my_voucher = (LinearLayout) view.findViewById(R.id.my_voucher);
		my_voucher.setOnClickListener(click);
		my_score = (LinearLayout) view.findViewById(R.id.my_score);
		my_score.setOnClickListener(click);
		my_share = (LinearLayout) view.findViewById(R.id.my_share);
		my_share.setOnClickListener(click);
		my_help = (LinearLayout) view.findViewById(R.id.my_help);
	
		my_help.setOnClickListener(click);
//		mynickname = (TextView) view.findViewById(R.id.my_nickname);
//
//		my_headimage = (CircleImageView) view.findViewById(R.id.my_headimage);

		alter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), SetupActivity.class);
				startActivity(intent);
			}
		});
		myself_call = (LinearLayout) view.findViewById(R.id.myself_call);
		myself_call.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Utils.callPhone(getActivity(), getResources().getString(R.string.server_call));
			}
		});
		myself_edit = (LinearLayout) view.findViewById(R.id.myself_edit);
		// my_headimage.setOnClickListener(click);

	}

	private OnClickListener click = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
				Intent intent = new Intent();
				Class cls = null;
				switch (arg0.getId()) {
				case R.id.my_center:
					cls = PersonActivity.class;

					break;
				case R.id.my_order:
					cls = OrderActivity.class;
					break;
				case R.id.my_voucher:
					cls = VoucherActivity.class;
					break;
				case R.id.my_score:
					cls = ScoreActivity.class;
					break;
				case R.id.my_share:
					cls = BonusActivity.class;
					break;
				case R.id.my_help:
					cls = HelpActivity.class;
					break;

				case R.id.my_address:
					cls = AddressActivity.class;
					break;
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
				}
				intent.setClass(getActivity(), cls);
				startActivity(intent);
			
		}
	};

	private void setValue() {
		my_wallet.setText("余额：" + user.getAdvance());
		ImageLoader.getInstance().displayImage(user.getFace(), my_headimage);
		mynickname.setText(user.getNickname());
	

	}

	
}
