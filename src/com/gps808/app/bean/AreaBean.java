package com.gps808.app.bean;

import java.util.List;

public class AreaBean {

	private List<BnSlider> top_banner;
	private List<BnHGoods> goods_banner;
	private List<BnGoods> goods;
	public List<BnSlider> getTop_banner() {
		return top_banner;
	}
	public void setTop_banner(List<BnSlider> top_banner) {
		this.top_banner = top_banner;
	}
	public List<BnHGoods> getGoods_banner() {
		return goods_banner;
	}
	public void setGoods_banner(List<BnHGoods> goods_banner) {
		this.goods_banner = goods_banner;
	}
	public List<BnGoods> getGoods() {
		return goods;
	}
	public void setGoods(List<BnGoods> goods) {
		this.goods = goods;
	}
	
}
