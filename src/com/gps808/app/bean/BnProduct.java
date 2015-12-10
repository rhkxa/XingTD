package com.gps808.app.bean;

import java.util.List;

public class BnProduct {

	private String id;
	private double price;
	private double mktprice;
	private String agent_income;
	private List<BnSpecs> specs;

	public String getAgent_income() {
		return agent_income;
	}

	public void setAgent_income(String agent_income) {
		this.agent_income = agent_income;
	}

	public List<BnSpecs> getSpecs() {
		return specs;
	}

	public void setSpecs(List<BnSpecs> specs) {
		this.specs = specs;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getMktprice() {
		return mktprice;
	}

	public void setMktprice(double mktprice) {
		this.mktprice = mktprice;
	}

}
