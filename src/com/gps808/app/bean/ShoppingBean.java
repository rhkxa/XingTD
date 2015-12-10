package com.gps808.app.bean;

import java.util.List;

public class ShoppingBean {
	private int count;

	private double amount;

	private List<BnShopping> datas;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public List<BnShopping> getDatas() {
		return datas;
	}

	public void setDatas(List<BnShopping> datas) {
		this.datas = datas;
	}

	// private List<pmt_alert> pmt_alerts ;

}
