package com.gps808.app.bean;

import java.util.List;

public class BnShopping {
	private String goods_id;

	private String product_id;

	private String img_url;

	private String name;

	private double price;

	private double mktprice;

	private int quantity;

	private double subtotal;

	private List<BnSpecs> specs ;

	public String getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public List<BnSpecs> getSpecs() {
		return specs;
	}

	public void setSpecs(List<BnSpecs> specs) {
		this.specs = specs;
	}
	
}
