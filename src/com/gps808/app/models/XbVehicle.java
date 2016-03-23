package com.gps808.app.models;

public class XbVehicle {
//	1.vId：车辆唯一标识；
//	2.online：车辆在线状态， true:在线；false:离线；
//	3.direction：车辆方向，范围0~359；0为正北方向，顺时针；
//	4.speed:车速，单位km/h；
//	5.location：车辆坐标信息，由经度、纬度组成，由“:”分隔，已经偏转为百度坐标；
//	6.address:位置信息
	private String vid;

	private String plateNo;

	private boolean online;

	private String time;

	private int direction;

	private double speed;

	private String location;

	private String addr;

	



	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public String getPlateNo() {
		return plateNo;
	}

	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}
}
