package com.gps808.app.utils;

import org.apache.http.HttpEntity;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpUtil {
	private static AsyncHttpClient client = new AsyncHttpClient(); // 实例化对象
	static {
		// client.setSSLSocketFactory(arg0) https方式访问
		//
		// client.setUserAgent(arg0)
		// client.setBasicAuth(arg0, arg1);
		client.setTimeout(30000); // 设置链接超时，如果不设置，默认为10s
		// client.getHttpClient().getParams()
		// .setParameter("http.protocol.allow-circular-redirects", true);// 重定向
		// getClient().getHttpClient().getConnectionManager().shutdown();
	}

	// 用一个完整url获取一个string对象,可以拉取进度
	public static void get(String urlString, AsyncHttpResponseHandler res) {
		LogUtils.DebugLog("http get", urlString);
		client.get(urlString, res);
	}

	// url里面带参数
	public static void post(String urlString, RequestParams params,
			AsyncHttpResponseHandler res) {
		LogUtils.DebugLog("http post", urlString);
		client.post(urlString, params, res);
	}

	// 下载数据使用，会返回byte数据
	public static void get(String uString, BinaryHttpResponseHandler bHandler) {
		client.get(uString, bHandler);
	}

	// 不带参数，获取json对象或者数组
	public static void get(Context context, String urlString,
			JsonHttpResponseHandler res) {
		LogUtils.DebugLog("http get", urlString);
		client.get(urlString, res);
		Utils.ToastMessage(context, "请检查网络状态，网络不可用");

	}

	// 带参数，获取json对象或者数组
	public static void post(String urlString, RequestParams params,
			JsonHttpResponseHandler res) {
		LogUtils.DebugLog("http post", urlString);
		client.post(urlString, params, res);
	}

	/**
	 * post 上传
	 * @param arg0 上下文
	 * @param arg1 地址
	 * @param arg2 数据
	 * @param arg3 格式
	 * @param arg4 
	 */
	public static void post(Context arg0, String arg1, HttpEntity arg2, String arg3, AsyncHttpResponseHandler arg4) {
		LogUtils.DebugLog("http post", arg1);
		client.post(arg0, arg1, arg2, arg3, arg4);
	}

	// 带参数的更新操作，获取json对象或者数组
	public static void put(String urlString, RequestParams params,
			JsonHttpResponseHandler res) {
		client.put(urlString, params, res);
	}

	// 删除操作，获取json对象或者数组
	public static void delete(String urlString, JsonHttpResponseHandler res) {
		client.delete(urlString, res);
	}

	public static AsyncHttpClient getClient() {
		return client;
	}
}
