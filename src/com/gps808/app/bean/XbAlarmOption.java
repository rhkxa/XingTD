package com.gps808.app.bean;

public class XbAlarmOption {
	private boolean acceptAlarm;

	private boolean sound;

	private boolean vibration;

	private boolean emergency;

	private boolean overSpeed;

	private boolean inArea;

	private boolean outArea;

	public boolean isAcceptAlarm() {
		return acceptAlarm;
	}

	public void setAcceptAlarm(boolean acceptAlarm) {
		this.acceptAlarm = acceptAlarm;
	}

	public boolean isSound() {
		return sound;
	}

	public void setSound(boolean sound) {
		this.sound = sound;
	}

	public boolean isVibration() {
		return vibration;
	}

	public void setVibration(boolean vibration) {
		this.vibration = vibration;
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
