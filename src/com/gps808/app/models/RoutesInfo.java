package com.gps808.app.models;

import java.util.List;

public class RoutesInfo {
	private int groupId;
	private String groupName;
	private List<XbRoute> sub;

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<XbRoute> getSub() {
		return sub;
	}

	public void setSub(List<XbRoute> sub) {
		this.sub = sub;
	}

}
