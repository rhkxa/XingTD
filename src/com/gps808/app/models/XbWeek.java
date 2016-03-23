package com.gps808.app.models;

public class XbWeek {
	private String day_air_temperature;

	private String night_air_temperature;

	private int weekday;

	private String day_weather_pic;

	public String getDay_air_temperature() {
		return day_air_temperature;
	}

	public void setDay_air_temperature(String day_air_temperature) {
		this.day_air_temperature = day_air_temperature;
	}

	public String getNight_air_temperature() {
		return night_air_temperature;
	}

	public void setNight_air_temperature(String night_air_temperature) {
		this.night_air_temperature = night_air_temperature;
	}

	public int getWeekday() {
		return weekday;
	}

	public void setWeekday(int weekday) {
		this.weekday = weekday;
	}

	public String getDay_weather_pic() {
		return day_weather_pic;
	}

	public void setDay_weather_pic(String day_weather_pic) {
		this.day_weather_pic = day_weather_pic;
	}

}
