package com.gps808.app.bean;

import java.util.List;

public class PDetailsBean {
	private String id;
	private String img_url;
	private String name;
	private String spec;
	private double price;
	private double mktprice;
	private int buy_count;
	private int comment_count;
	private String product_id;
	private String agent_income;
	private String intro;
	private List<BnProduct> products;
	//private List<BnSpecs> specs;
	private List<BnGallery> gallery;
	
	
	public List<BnProduct> getProducts() {
		return products;
	}

	public void setProducts(List<BnProduct> products) {
		this.products = products;
	}

	public String getId() {
		return id;
	}
	
	public String getAgent_income() {
		return agent_income;
	}

	public void setAgent_income(String agent_income) {
		this.agent_income = agent_income;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
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
	public int getBuy_count() {
		return buy_count;
	}
	public void setBuy_count(int buy_count) {
		this.buy_count = buy_count;
	}
	public int getComment_count() {
		return comment_count;
	}
	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	
	// public List<BnSpecs> getSpecs() {
	// return specs;
	// }
	// public void setSpecs(List<BnSpecs> specs) {
	// this.specs = specs;
	// }
	public List<BnGallery> getGallery() {
		return gallery;
	}
	public void setGallery(List<BnGallery> gallery) {
		this.gallery = gallery;
	}
	
}
