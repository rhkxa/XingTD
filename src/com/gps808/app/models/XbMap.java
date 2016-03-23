package com.gps808.app.models;

public class XbMap {
	private int id;
	private String name;
	private String size;

	/**
	 * 下载的进度
	 */
	private int progress;

	private Flag flag = Flag.NO_STATUS;

	/**
	 * 下载的状态：无状态，暂停，正在下载
	 */
	public enum Flag {
		NO_STATUS, PAUSE, DOWNLOADING
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public Flag getFlag() {
		return flag;
	}

	public void setFlag(Flag flag) {
		this.flag = flag;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

}
