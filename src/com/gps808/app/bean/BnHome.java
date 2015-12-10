package com.gps808.app.bean;

import java.util.List;

public class BnHome {

	private List<BnSlider> slider;

	private List<BnHButton> button;

	private List<BnHActive> activity;

	private List<BnHGame> game;

	private List<BnHCate> cate;

	private List<List<BnHGoods>> goods_banner;
	

	public List<List<BnHGoods>> getGoods_banner() {
		return goods_banner;
	}

	public void setGoods_banner(List<List<BnHGoods>> goods_banner) {
		this.goods_banner = goods_banner;
	}

	public List<BnSlider> getSlider() {
		return slider;
	}

	public void setSlider(List<BnSlider> slider) {
		this.slider = slider;
	}

	public List<BnHButton> getButton() {
		return button;
	}

	public void setButton(List<BnHButton> button) {
		this.button = button;
	}

	public List<BnHActive> getActivity() {
		return activity;
	}

	public void setActivity(List<BnHActive> activity) {
		this.activity = activity;
	}

	public List<BnHGame> getGame() {
		return game;
	}

	public void setGame(List<BnHGame> game) {
		this.game = game;
	}

	public List<BnHCate> getCate() {
		return cate;
	}

	public void setCate(List<BnHCate> cate) {
		this.cate = cate;
	}

	

}
