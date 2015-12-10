package com.gps808.app.utils;

import org.json.JSONObject;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class LogUtils {
	// 日志开关，！！！！默认为关闭，开发可以自行打开，但是切记不要提交到svn
	public static boolean LOG_ON = true; // ！！！！默认为关闭，开发可以自行打开.
	public static boolean FREE_FLOW_ON = false;
	public static boolean AUTOTEST = false;

	public static void DebugLog(String src) {
		if (LOG_ON)
			Log.d("Xtd--byJIA", src);
	}

	public static void DebugLog(Exception e) {
		if (LOG_ON)
			Log.e("Xtd--byJIA", "" + e);

	}

	public static void DebugLog(double double1) {
		if (LOG_ON)
			Log.d("Xtd--byJIA", "" + double1);

	}

	public static void DebugLog(JSONObject put) {
		if (LOG_ON)
			Log.d("Xtd--byJIA", "" + put);

	}

	public static void DebugLog(Object object) {
		if (LOG_ON)
			Log.d("Xtd--byJIA", "" + object);

	}

	public static void DebugLog(String string, String string2) {
		if (LOG_ON)
			Log.d("Xtd--byJIA", string + " " + string2);

	}

	public static void DebugLog(String logTag, String string, Exception e) {
		if (LOG_ON)
			Log.d("Xtd--byJIA", logTag + " " + string + e.toString());
	}

	public static void ShowToast(Context context, String text, int duration) {
		if (context != null)
			Toast.makeText(context, text, duration).show();
	}

	public static void ShowToast(Context context, int resid, int duration) {
		if (context != null)
			Toast.makeText(context, context.getString(resid), duration).show();

	}

	public static void d(String string, String string2) {
		if (LOG_ON)
			Log.d(string, string2);
	}

	public static void e(String string, String string2) {
		if (LOG_ON)
			Log.e(string, string2);
	}

	public static void w(String string, String string2) {
		if (LOG_ON)
			Log.w(string, string2);
	}

	public static void PST(Exception e) {
		if (LOG_ON) {
			e.printStackTrace();
		}
	}

	public static void PST(OutOfMemoryError e) {
		if (LOG_ON) {
			e.printStackTrace();
		}
	}
}
