package com.gps808.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.gps808.app.R;
import com.gps808.app.models.XbWeek;
import com.gps808.app.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class WeatherAdapter extends BaseAdapter {
	private Context mContext;
	private List<XbWeek> datalist;

	public WeatherAdapter(Context mContext, List<XbWeek> datalist) {
		this.mContext = mContext;
		this.datalist = datalist;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datalist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder vh;
		if (arg1 == null) {

			arg1 = LayoutInflater.from(mContext).inflate(
					R.layout.item_weather_list, null);
			vh = new ViewHolder(arg1);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}

		ImageLoader.getInstance().displayImage(
				datalist.get(arg0).getDay_weather_pic(), vh.item_weather_image);
		vh.item_weather_cool.setText(datalist.get(arg0)
				.getNight_air_temperature());
		vh.item_weather_heat.setText(datalist.get(arg0)
				.getDay_air_temperature());
		vh.item_weather_week.setText(StringUtils.getWeek(datalist.get(arg0)
				.getWeekday()));

		return arg1;
	}

	class ViewHolder {
		ImageView item_weather_image;
		TextView item_weather_cool;
		TextView item_weather_heat;
		TextView item_weather_week;

		public ViewHolder(View arg1) {
			item_weather_image = (ImageView) arg1
					.findViewById(R.id.item_weather_image);
			item_weather_week = (TextView) arg1
					.findViewById(R.id.item_weather_week);
			item_weather_cool = (TextView) arg1
					.findViewById(R.id.item_weather_cool);
			item_weather_heat = (TextView) arg1
					.findViewById(R.id.item_weather_heat);
		}

	}

}
