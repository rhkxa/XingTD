package com.gps808.app.bean;

import java.util.List;

public class BillBean {
	private double revenue;
	private double balance;
	private int rank;
	private List<BnBill> bills;

	public double getRevenue() {
		return revenue;
	}

	public void setRevenue(double revenue) {
		this.revenue = revenue;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public List<BnBill> getBills() {
		return bills;
	}

	public void setBills(List<BnBill> bills) {
		this.bills = bills;
	}

}
