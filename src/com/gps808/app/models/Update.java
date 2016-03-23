package com.gps808.app.models;

/**
 * APP更新实体类
 * @author Administrator
 *
 */
public class Update {
	private Float appVer;

	private String releaseTime;

	private boolean forceUpdate;

	private String versionDesc;

	private String updateUrl;

	public Float getAppVer() {
		return appVer;
	}

	public void setAppVer(Float appVer) {
		this.appVer = appVer;
	}

	public String getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}

	
	public boolean isForceUpdate() {
		return forceUpdate;
	}

	public void setForceUpdate(boolean forceUpdate) {
		this.forceUpdate = forceUpdate;
	}

	public String getVersionDesc() {
		return versionDesc;
	}

	public void setVersionDesc(String versionDesc) {
		this.versionDesc = versionDesc;
	}

	public String getUpdateUrl() {
		return updateUrl;
	}

	public void setUpdateUrl(String updateUrl) {
		this.updateUrl = updateUrl;
	}

	

}
