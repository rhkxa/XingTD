package com.gps808.app.fragment;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.dialog.AlterNameDialog;
import com.gps808.app.dialog.AlterNameDialog.OnAlterClickListener;
import com.gps808.app.models.XbCar;
import com.gps808.app.utils.BaseFragment;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;
import com.gps808.app.view.CircleImageView;

public class CarFragment extends BaseFragment {

	private CircleImageView car_detail_image;
	private TextView car_detail_name, car_detail_num, car_detail_position,
			car_phone_text, car_number_text, car_type_text, car_start_text,
			car_end_text;
	private String vid;
	private ImageView car_detail_edits;

	public static CarFragment newInstance(String id) {
		CarFragment fragment = new CarFragment();
		fragment.vid = id;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_car, null);
		init(view);
		getData();
		return view;
	}

	private void init(View root) {
		// TODO Auto-generated method stub
		car_detail_name = (TextView) root.findViewById(R.id.car_detail_name);
		car_detail_num = (TextView) root.findViewById(R.id.car_detail_num);
		car_detail_position = (TextView) root
				.findViewById(R.id.car_detail_position);
		car_phone_text = (TextView) root.findViewById(R.id.car_phone_text);
		car_number_text = (TextView) root.findViewById(R.id.car_number_text);
		car_type_text = (TextView) root.findViewById(R.id.car_type_text);
		car_start_text = (TextView) root.findViewById(R.id.car_start_text);
		car_end_text = (TextView) root.findViewById(R.id.car_end_text);
		car_detail_edits = (ImageView) root.findViewById(R.id.car_detail_edits);
		car_detail_edits.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showEditDialog();
			}
		});

	}

	private void showEditDialog() {
		AlterNameDialog alter = new AlterNameDialog(getActivity());
		alter.setOnAlterClickListener(new OnAlterClickListener() {

			@Override
			public void onAlterOk(String key) {
				// TODO Auto-generated method stub
				setNick(key);
			}
		});
		alter.show();
	}

	private void setValue(XbCar car) {
		car_detail_name.setText("设备名称:" + car.getTmnlName());
		car_detail_num.setText("车牌号:" + car.getPlateNo());
		car_detail_position.setText(car.getAddr());
		car_phone_text.setText(car.getSimNo());
		car_number_text.setText(car.getTmnlNo());
		car_type_text.setText(car.getTmnlType());
		car_start_text.setText(car.getStart());
		car_end_text.setText(car.getEnd());
	}

	private void getData() {
		String url = UrlConfig.getVehicleVehInfo(vid);
		HttpUtil.get(getActivity(), url, new jsonHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				showProgressDialog(getActivity(), "正在加载，请稍等");
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				XbCar car = JSON.parseObject(response.toString(), XbCar.class);
				LogUtils.DebugLog("result json" + response.toString());
				setValue(car);
				super.onSuccess(statusCode, headers, response);
			}
		});
	}

	private void setNick(final String name) {
		// {"vId":1109,"tmnlName":"张三" }

		String url = UrlConfig.getSetTmnlName();
		JSONObject params = new JSONObject();
		StringEntity entity = null;
		try {
			params.put("vId", vid);
			params.put("tmnlName", name);
			entity = new StringEntity(params.toString(), "UTF-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LogUtils.DebugLog("post json", params.toString());
		HttpUtil.post(getActivity(), url, entity, "application/json",
				new jsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						Utils.ToastMessage(getActivity(), "修改成功");
						car_detail_name.setText("设备名称：" + name);
						super.onSuccess(statusCode, headers, response);
					}
				});

	}
}
