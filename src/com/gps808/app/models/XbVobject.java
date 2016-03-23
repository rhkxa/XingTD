package com.gps808.app.models;

import java.util.ArrayList;
import java.util.List;

public class XbVobject {
	private int totalNum;

	private int onlineNum;

	private int offlineNum;

	private ArrayList<XbVehicle> rows;
	



	public ArrayList<XbVehicle> getRows() {
		return rows;
	}

	public void setRows(ArrayList<XbVehicle> rows) {
		this.rows = rows;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getOnlineNum() {
		return onlineNum;
	}

	public void setOnlineNum(int onlineNum) {
		this.onlineNum = onlineNum;
	}

	public int getOfflineNum() {
		return offlineNum;
	}

	public void setOfflineNum(int offlineNum) {
		this.offlineNum = offlineNum;
	}

	

}
