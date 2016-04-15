/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gps808.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {

	/**
	 * 保存Preference的name
	 */

	public static final String PREFERENCE_NAME = "saveInfo";
	public static final String AUTO_REFRESH_TRACK = "auto_refresh_track";
	public static final String AUTO_REFRESH_MONITOR = "auto_refresh_monitor";

	private static SharedPreferences mSharedPreferences;
	private static PreferenceUtils mPreferenceUtils;
	private static SharedPreferences.Editor editor;

	// 用户类信息

	private String USERNAME = "userName";
	private String USERPW = "userPassword";
	private String USERSTATE = "userState";

	private PreferenceUtils(Context cxt) {
		mSharedPreferences = cxt.getSharedPreferences(PREFERENCE_NAME,
				Context.MODE_PRIVATE);
	}

	/**
	 * 单例模式，获取instance实例
	 * 
	 * @param cxt
	 * @return
	 */
	public static PreferenceUtils getInstance(Context cxt) {
		if (mPreferenceUtils == null) {
			mPreferenceUtils = new PreferenceUtils(cxt);
		}
		editor = mSharedPreferences.edit();
		return mPreferenceUtils;
	}

	public void setFirstPage(String firstPage) {
		editor.putString("firstPage", firstPage);
		editor.commit();
	}

	public String getFirstPage() {
		return mSharedPreferences.getString("firstPage", null);
	}

	public void setUserNick(String nick) {
		editor.putString("nick", nick);
		editor.commit();
	}

	public String getUserNick() {
		return mSharedPreferences.getString("nick", "星通达 ");
	}

	public String getUserName() {
		return mSharedPreferences.getString(USERNAME, null);
	}

	public void setUserName(String name) {
		editor.putString(USERNAME, name);
		editor.commit();
	}

	public String getUserImg() {
		return mSharedPreferences.getString("userimg", null);
	}

	public void setUserImg(String userimg) {
		editor.putString("userimg", userimg);
		editor.commit();
	}

	public String getUserId() {
		return mSharedPreferences.getString("userid", null);
	}

	public void setUserId(String userid) {
		editor.putString("userid", userid);
		editor.commit();
	}

	public String getUserPW() {
		return mSharedPreferences.getString(USERPW, null);
	}

	public void setUserPW(String pw) {
		editor.putString(USERPW, pw);
		editor.commit();
	}

	public int getUserState() {
		return mSharedPreferences.getInt(USERSTATE, 0);
	}

	public void setUserState(int userType) {
		editor.putInt(USERSTATE, userType);
		editor.commit();
	}

	public boolean getAutoLogin() {
		return mSharedPreferences.getBoolean("autologin", true);
	}

	public void setAutoLogin(boolean state) {
		editor.putBoolean("autologin", state);
		editor.commit();
	}

	public boolean getSavePassWord() {
		return mSharedPreferences.getBoolean("savepassword", true);
	}

	public void setSavePassWord(boolean state) {
		editor.putBoolean("savepassword", state);
		editor.commit();
	}

	public boolean getPush() {
		return mSharedPreferences.getBoolean("push", true);

	}

	public void setPush(boolean state) {
		editor.putBoolean("push", state);
		editor.commit();
	}

	public boolean getVoice() {
		return mSharedPreferences.getBoolean("voice", true);

	}

	public void setVoice(boolean voice) {
		editor.putBoolean("voice", voice);
		editor.commit();
	}

	public boolean getShock() {
		return mSharedPreferences.getBoolean("shock", true);

	}

	public void setShock(boolean shock) {
		editor.putBoolean("shock", shock);
		editor.commit();
	}

	public String getCall() {
		return mSharedPreferences.getString("phonenumber", "4000311449");

	}

	public void setCall(String phonenumber) {
		editor.putString("phonenumber", phonenumber);
		editor.commit();
	}

	public String getLocal() {
		return mSharedPreferences.getString("local", null);

	}

	public void setLocal(String local) {
		editor.putString("local", local);
		editor.commit();
	}

	public String getShopping() {
		return mSharedPreferences.getString("shopping", null);

	}

	public void setShopping(String shopping) {
		editor.putString("shopping", shopping);
		editor.commit();
	}

	public String getAddress() {
		return mSharedPreferences.getString("address", null);

	}

	public void setAddress(String address) {
		editor.putString("address", address);
		editor.commit();
	}

	public String getCurrentAddress() {
		return mSharedPreferences.getString("currentaddress", null);

	}

	public void setCurrentAddress(String address) {
		editor.putString("currentaddress", address);
		editor.commit();
	}

	public int getMonitorTime() {
		return mSharedPreferences.getInt(AUTO_REFRESH_MONITOR, 10);

	}

	public void setMonitorTime(int time) {
		editor.putInt(AUTO_REFRESH_MONITOR, time);
		editor.commit();
	}

	public int getTrackTime() {
		return mSharedPreferences.getInt(AUTO_REFRESH_TRACK, 10);

	}

	public void setTrackTime(int time) {
		editor.putInt(AUTO_REFRESH_TRACK, time);
		editor.commit();
	}


	public void setValue(String key,long time){
		editor.putLong(key, time);
		editor.commit();
	}
	public long getValue(String key){
		return mSharedPreferences.getLong(key, 0);
	}
	public void setPingTai(int key){
		editor.putInt("pingtai",key);
		editor.commit();
	}
	public int getPingTai(){
		return mSharedPreferences.getInt("pingtai", 0);
	}

}
