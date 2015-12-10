package com.gps808.app.bean;

import java.util.List;

public class BnHGoods {
	private String img_url;
	private String title;
	private String goods_id;
	private List<BnAction> action;
	
	

	public String getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public List<BnAction> getAction() {
		return action;
	}
	public void setAction(List<BnAction> action) {
		this.action = action;
	}


}
