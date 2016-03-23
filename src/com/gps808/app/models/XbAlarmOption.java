package com.gps808.app.models;

public class XbAlarmOption {
	private boolean inArea;

	private boolean rolloverAlarm;

	private boolean tmnlPowerDown;

	private boolean emergency;

	private boolean dangerAlarm;

	private boolean gnssModeFault;

	private boolean vssFault;

	private boolean gnssDisconnect;

	private boolean overSpeed;

	private boolean outArea;

	private boolean sound;

	private boolean acceptAlarm;

	private boolean vibration;

	private boolean gnssShortCircuit;

	private boolean crashAlarm;
	
	
	

	public boolean isRolloverAlarm() {
		return rolloverAlarm;
	}

	public void setRolloverAlarm(boolean rolloverAlarm) {
		this.rolloverAlarm = rolloverAlarm;
	}

	public boolean isTmnlPowerDown() {
		return tmnlPowerDown;
	}

	public void setTmnlPowerDown(boolean tmnlPowerDown) {
		this.tmnlPowerDown = tmnlPowerDown;
	}

	public boolean isDangerAlarm() {
		return dangerAlarm;
	}

	public void setDangerAlarm(boolean dangerAlarm) {
		this.dangerAlarm = dangerAlarm;
	}

	public boolean isGnssModeFault() {
		return gnssModeFault;
	}

	public void setGnssModeFault(boolean gnssModeFault) {
		this.gnssModeFault = gnssModeFault;
	}

	public boolean isVssFault() {
		return vssFault;
	}

	public void setVssFault(boolean vssFault) {
		this.vssFault = vssFault;
	}

	public boolean isGnssDisconnect() {
		return gnssDisconnect;
	}

	public void setGnssDisconnect(boolean gnssDisconnect) {
		this.gnssDisconnect = gnssDisconnect;
	}

	public boolean isGnssShortCircuit() {
		return gnssShortCircuit;
	}

	public void setGnssShortCircuit(boolean gnssShortCircuit) {
		this.gnssShortCircuit = gnssShortCircuit;
	}

	public boolean isCrashAlarm() {
		return crashAlarm;
	}

	public void setCrashAlarm(boolean crashAlarm) {
		this.crashAlarm = crashAlarm;
	}

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
