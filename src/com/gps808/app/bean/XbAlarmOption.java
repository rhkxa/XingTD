package com.gps808.app.bean;

public class XbAlarmOption {
	private boolean isAcceptAlarm;

	private boolean isSound;

	private boolean isVibration;

	private boolean emergency;

	private boolean overSpeed;

	private boolean inArea;

	private boolean outArea;

	public boolean isAcceptAlarm() {
		return isAcceptAlarm;
	}

	public void setAcceptAlarm(boolean isAcceptAlarm) {
		this.isAcceptAlarm = isAcceptAlarm;
	}

	public boolean isSound() {
		return isSound;
	}

	public void setSound(boolean isSound) {
		this.isSound = isSound;
	}

	public boolean isVibration() {
		return isVibration;
	}

	public void setVibration(boolean isVibration) {
		this.isVibration = isVibration;
	}

	public boolean isEmergency() {
		return emergency;
	}

	public void setEmergency(boolean emergency) {
		this.emergency = emergency;
	}

	public boolean isOverSpeed() {
		return overSpeed;
	}

	public void setOverSpeed(boolean overSpeed) {
		this.overSpeed = overSpeed;
	}

	public boolean isInArea() {
		return inArea;
	}

	public void setInArea(boolean inArea) {
		this.inArea = inArea;
	}

	public boolean isOutArea() {
		return outArea;
	}

	public void setOutArea(boolean outArea) {
		this.outArea = outArea;
	}

}
