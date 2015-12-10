package com.gps808.app.bean;

import java.util.List;

public class InitOrderBean {

	private String sn;
	private BnAddress address;
	private List<BnShopping> items;
	private Bnfare shipping;
	private int count;
	private double amount;
	private double bill_amount;
	private double freight;
	
	public Bnfare getShipping() {
		return shipping;
	}
	public void setShipping(Bnfare shipping) {
		this.shipping = shipping;
	}
	public double getBill_amount() {
		return bill_amount;
	}
	public void setBill_amount(double bill_amount) {
		this.bill_amount = bill_amount;
	}
	public double getFreight() {
		return freight;
	}
	public void setFreight(double freight) {
		this.freight = freight;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public BnAddress getAddress() {
		return address;
	}
	public void setAddress(BnAddress address) {
		this.address = address;
	}

	public List<BnShopping> getItems() {
		return items;
	}
	public void setItems(List<BnShopping> items) {
		this.items = items;
	}
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
	

}
