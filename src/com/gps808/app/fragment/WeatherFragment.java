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
import com.gps808.app.bean.XbWeather;
import com.gps808.app.bean.XbWeek;
import com.gps808.app.utils.BaseFragment;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.LogUtils;
import com.gps808.app.utils.StringUtils;
import com.gps808.app.utils.UrlConfig;

public class WeatherFragment extends BaseFragment {
	private TextView weather_city, weather_state, weather_temperature,
			weather_week, weather_today, weather_date, weather_desc;
	private ListView weather_list;
	private WeatherAdapter wadapter;
	private List<XbWeek> xbWeeks=new ArrayList<XbWeek>();

	private double lat, lng;

	public static WeatherFragment newInstance(double lat, double lng) {
		WeatherFragment fragment = new WeatherFragment();
		fragment.lat = lat;
		fragment.lng = lng;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View root = inflater.inflate(R.layout.fragment_weather, null);
		init(root);
		getData();
		return root;
	}

	private void init(View root) {
		// TODO Auto-generated method stub
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
	}

	private void setValue(XbWeather xbWeather) {
		weather_city.setText(xbWeather.getCityInfo());
		weather_state.setText(xbWeather.getNow().getWeather());
		weather_temperature.setText(xbWeather.getNow().getTemperature());
		weather_week.setText(StringUtils.getWeek(xbWeather.getNow()
				.getWeekday()));
		// weather_today.setText(xbWeather.getNow().get);
		weather_date.setText(StringUtils.toDate(System.currentTimeMillis()));
		weather_desc.setText("今天"+xbWeather.getNow().getDesc());
		xbWeeks.add(xbWeather.getF2());
		xbWeeks.add(xbWeather.getF3());
		xbWeeks.add(xbWeather.getF4());
		xbWeeks.add(xbWeather.getF5());
		xbWeeks.add(xbWeather.getF6());
		xbWeeks.add(xbWeather.getF7());
		wadapter.notifyDataSetChanged();
	}

	private void getData() {
		String url = UrlConfig.getWeather(lng, lat);
		HttpUtil.get(getActivity(), url, new jsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub

				LogUtils.DebugLog("result json", response.toString());
				XbWeather xbWeather = JSON.parseObject(response.toString(),
						XbWeather.class);
				setValue(xbWeather);
				super.onSuccess(statusCode, headers, response);
			}
		});
	}
}
