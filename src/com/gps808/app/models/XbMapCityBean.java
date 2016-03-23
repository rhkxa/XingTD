package com.gps808.app.models;

import java.util.List;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;

public class XbMapCityBean {

	private int id;
	private String name;
	private String size;

	private List<XbMap> city;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public List<XbMap> getCity() {
		return city;
	}

	public void setCity(List<XbMap> city) {
		this.city = city;
	}

}
