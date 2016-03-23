package com.gps808.app.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.adapter.WeatherAdapter;
import com.gps808.app.models.XbVehicle;
import com.gps808.app.models.XbWeather;
import com.gps808.app.models.XbWeek;
import com.gps808.app.utils.BaseFragment;
import com.gps808.app.utils.FileUtils;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.PreferenceUtils;
import com.gps808.app.utils.StringUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;

public class WeatherFragment extends BaseFragment {
	private TextView weather_city, weather_state, weather_temperature,
			weather_week, weather_today, weather_date, weather_desc;
	private ListView weather_list;
	private WeatherAdapter wadapter;
	private List<XbWeek> xbWeeks = new ArrayList<XbWeek>();
	private XbVehicle xbVehicle;
	private double[] doubleLng;
	private String vid;

	public static WeatherFragment newInstance(XbVehicle xbVehicle) {
		WeatherFragment fragment = new WeatherFragment();
		fragment.xbVehicle = xbVehicle;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View root = inflater.inflate(R.layout.fragment_weather, null);
		init(root);
		return root;
	}

	private void init(View root) {
		// TODO Auto-generated method stub
		doubleLng = Utils.getLng(xbVehicle.getLocation());
		vid = xbVehicle.getVid();
		weather_city = (TextView) root.findViewById(R.id.weather_city);
		weather_state = (TextView) root.findViewById(R.id.weather_state);
		weather_temperature = (TextView) root
				.findViewById(R.id.weather_temperature);
		weather_week = (TextView) root.findViewById(R.id.weather_week);
		weather_today = (TextView) root.findViewById(R.id.weather_today);
		weather_date = (TextView) root.findViewById(R.id.weather_date);
		weather_desc = (TextView) root.findViewById(R.id.weather_desc);
		weather_list = (ListView) root.findViewById(R.id.weather_list);
		wadapter = new WeatherAdapter(getActivity(), xbWeeks);
		weather_list.setAdapter(wadapter);
		if (System.currentTimeMillis()
				- PreferenceUtils.getInstance(getActivity()).getValue(
						"Weather" + vid) > 30 * 60 * 1000) {
			getData();
		} else {
			setValue(FileUtils.read(getActivity(), "FileWeather" + vid));
		}

	}

	private void setValue(String response) {
		XbWeather xbWeather = JSON.parseObject(response, XbWeather.class);
		weather_city.setText(xbWeather.getCityInfo());
		weather_state.setText(xbWeather.getNow().getWeather());
		weather_temperature.setText(xbWeather.getNow().getTemperature());
		weather_week.setText(StringUtils.getWeek(xbWeather.getNow()
				.getWeekday()));
		// weather_today.setText(xbWeather.getNow().get);
		weather_date.setText(StringUtils.toDate(System.currentTimeMillis()));
		weather_desc.setText("今天" + xbWeather.getNow().getDesc());
		xbWeeks.clear();
		xbWeeks.add(xbWeather.getF2());
		xbWeeks.add(xbWeather.getF3());
		xbWeeks.add(xbWeather.getF4());
		xbWeeks.add(xbWeather.getF5());
		xbWeeks.add(xbWeather.getF6());
		xbWeeks.add(xbWeather.getF7());
		wadapter.notifyDataSetChanged();
	}

	private void getData() {

		String url = UrlConfig.getWeather(doubleLng[0], doubleLng[1]);
		HttpUtil.get(getActivity(), url, new jsonHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				showProgressDialog(getActivity(), "正在加载天气信息,请稍等");
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				LogUtils.DebugLog("result json", response.toString());
				setValue(response.toString());
				PreferenceUtils.getInstance(getActivity()).setValue(
						"Weather" + vid, System.currentTimeMillis());
				FileUtils.write(getActivity(), "FileWeather" + vid,
						response.toString());
				super.onSuccess(statusCode, headers, response);
			}
		});
	}
}
