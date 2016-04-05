package com.gps808.app.utils;

import java.io.File;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.activity.LoginActivity;
import com.gps808.app.activity.MainActivity;
import com.gps808.app.models.XbUser;
import com.gps808.app.utils.BaseActivity.jsonHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class Utils {
	/**
	 * 弹出Toast消息
	 * 
	 * @param msg
	 */
	public static void ToastMessage(Context cont, String msg) {
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}

	public static void ToastMessage(Context cont, int msg) {
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}

	public static void ToastMessage(Context cont, String msg, int time) {
		Toast.makeText(cont, msg, time).show();
	}

	/**
	 * 显示application的Toast消息
	 * 
	 * @param message
	 */
	public static void showSuperCardToast(Context cont, String message) {
		try {
			if (message != null && message.length() != 0) {
				Toast.makeText(cont.getApplicationContext(), message,
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 检测WIFI是否可用
	 * 
	 * @param context
	 * @return
	 */

	public boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 检测网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetWorkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}

		return false;
	}

	/**
	 * 检测Sdcard是否存在
	 * 
	 * @return
	 */
	public static boolean isExitsSdcard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}

	/**
	 * 屏幕的宽
	 * 
	 * @return
	 */
	public static int getScreenWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	/**
	 * 屏幕的高
	 * 
	 * @return
	 */
	public static int getScreenHight(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	/**
	 * 选择图片裁剪
	 * 
	 * @param output
	 */
	public static void startImagePick(Activity activity, int flag) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		activity.startActivityForResult(Intent.createChooser(intent, "选择图片"),
				flag);
	}

	/**
	 * 相机拍照
	 * 
	 * @param activity
	 * @param output
	 *            相册拍照存储图片的URI
	 * @param flag
	 *            标识
	 */
	public static void startActionCamera(Activity activity, Uri output, int flag) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
		activity.startActivityForResult(intent, flag);
	}

	/**
	 * 
	 * 拍照后裁剪
	 * 
	 * @param activity
	 * @param data
	 *            原始图片
	 * @param output
	 *            裁剪后图片
	 * @param crop
	 *            裁剪大小
	 * @param flag
	 *            标识
	 */
	public static void startActionCrop(Activity activity, Uri data, Uri output,
			int crop, int flag) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(data, "image/*");
		intent.putExtra("output", output);
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", crop);// 输出图片大小
		intent.putExtra("outputY", crop);
		intent.putExtra("scale", true);// 去黑边
		intent.putExtra("scaleUpIfNeeded", true);// 去黑边
		activity.startActivityForResult(intent, flag);
	}

	public static void Share(Context context, String Title, String content,
			String path, String imagePath) {
		ShareSDK.initSDK(context);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		// 分享时Notification的图标和文字
		// oks.setNotification(R.drawable.smyk_logo, context.getResources()
		// .getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(Title);
		// text是分享文本，所有平台都需要这个字段
		oks.setText(content);
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("/storage/emulated/0/picture/default_.jpg");//
		// 确保SDcard下面存在此张图片
		oks.setImagePath(imagePath);// 确保SDcard下面存在此张图片
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl(path);
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(path);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用oks.setComment("我是测试评论文本");

		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(context.getResources().getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl(path);
		oks.setSilent(false);
		oks.setDialogMode();// 显示为弹窗效果
		oks.setTheme(OnekeyShareTheme.CLASSIC);
		// 启动分享GUI
		oks.show(context);
	}

	private static PlatformActionListener paListener = new PlatformActionListener() {

		@Override
		public void onError(Platform arg0, int arg1, Throwable arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onComplete(Platform arg0, int arg1,
				HashMap<String, Object> arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onCancel(Platform arg0, int arg1) {
			// TODO Auto-generated method stub

		}
	};

	public static void shareOneQQ(Context context, String Title,
			String content, String path, String imagePath) {
		ToastMessage(context, "正在启动QQ分享");
		ShareSDK.initSDK(context);
		QQ.ShareParams sp = new QQ.ShareParams();
		sp.setTitle(Title);
		sp.setTitleUrl(path); // 标题的超链接
		sp.setText(content);
		sp.setImagePath(imagePath);
		sp.setImageUrl(imagePath);
		Platform qzone = ShareSDK.getPlatform(QQ.NAME);
		qzone.setPlatformActionListener(paListener); // 设置分享事件回调
		// 执行图文分享
		qzone.share(sp);
	}

	public static void shareOneSina(Context context, String text, String path,
			String imagePath) {
		ToastMessage(context, "正在启动新浪微博分享");
		ShareSDK.initSDK(context);
		SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
		sp.setText(text);
		sp.setImagePath(imagePath);
		// 分享网络图片，新浪分享网络图片，需要申请高级权限,否则会报10014的错误
		// 权限申请：新浪开放平台-你的应用中-接口管理-权限申请-微博高级写入接口-statuses/upload_url_text
		// 注意：本地图片和网络图片，同时设置时，只分享本地图片
		// oks.setImageUrl("http://img.appgo.cn/imgs/sharesdk/content/2013/07/25/1374723172663.jpg");
		sp.setUrl(path);
		Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
		weibo.setPlatformActionListener(paListener); // 设置分享事件回调
		// 执行图文分享
		weibo.share(sp);

	}

	public static void shareOneWechat(Context context, String Title,
			String content, String path, String imagePath) {

		ShareSDK.initSDK(context);
		Wechat.ShareParams sp = new Wechat.ShareParams();
		sp.setShareType(Platform.SHARE_WEBPAGE);
		sp.setTitle(Title);
		sp.setText(content);
		sp.setImagePath(imagePath);
		sp.setUrl(path);
		Platform qzone = ShareSDK.getPlatform(Wechat.NAME);
		qzone.setPlatformActionListener(paListener); // 设置分享事件回调
		// 执行图文分享
		qzone.share(sp);
	}

	public static void shareOneWechatMoments(Context context, String Title,
			String content, String path, String imagePath) {
		ToastMessage(context, "正在启动微信分享");
		ShareSDK.initSDK(context);
		WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
		sp.setShareType(Platform.SHARE_WEBPAGE);
		sp.setTitle(Title);
		sp.setText(content);
		sp.setImagePath(imagePath);
		sp.setUrl(path);
		Platform qzone = ShareSDK.getPlatform(WechatMoments.NAME);
		qzone.setPlatformActionListener(paListener); // 设置分享事件回调
		// 执行图文分享
		qzone.share(sp);
	}

	// 打电话
	public static void callPhone(Context context, String inputStr) {
		if (!StringUtils.isEmpty(inputStr)) {
			Intent phoneIntent = new Intent("android.intent.action.CALL",
					Uri.parse("tel:" + inputStr));
			// 启动
			context.startActivity(phoneIntent);
		}
	}

	public static void toExploer(Context context, String url) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri content_url = Uri.parse(url);
		intent.setData(content_url);
		context.startActivity(intent);
	}

	// 手动取一个key的值
	public static String getKey(String str, String key) {

		String result = "";
		try {
			JSONObject json = new JSONObject(str);
			result = json.getString(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}

	// 手动取一个key的值
	public static String getKey(JSONObject json, String key) {

		String result = "";
		try {
			result = json.getString(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}

	// 网络请求是否成功
	public static boolean requestOk(JSONObject json) {
		LogUtils.DebugLog("result json", json.toString());
		try {
			if (json.getString("isSuccess").equals("true")) {
				return true;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	// 判断访问是否成功，成功后返回结果json
	public static String getResult(JSONObject json) {
		String result = "";
		try {
			if (requestOk(json)) {
				result = json.getString("result");
				LogUtils.DebugLog("result json", result);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}

	public static double[] getLng(String lng) {
		String[] strLng = lng.split(",");
		double[] douLng = { Double.parseDouble(strLng[0]),
				Double.parseDouble(strLng[1]) };
		return douLng;
	}

	public static String[] getSplit(String str, String flag) {
		String[] strLng = str.split(flag);
		return strLng;
	}

	/**
	 * 获取当前客户端版本信息
	 */
	public static int getCurrentVersion(Context mContext) {
		int curVersionCode = 0;
		try {
			PackageInfo info = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0);
			curVersionCode = info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
		return curVersionCode;

	}

	/**
	 * 用户退出
	 */
	public static void exitLogin(final Context context) {
		String url = UrlConfig.getLoginOut();
		HttpUtil.get(context, url, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				PreferenceUtils.getInstance(context).setUserPW("");
				PreferenceUtils.getInstance(context).setUserPW("");
				Intent intent = new Intent(context, LoginActivity.class);
				context.startActivity(intent);

				super.onSuccess(statusCode, headers, response);
			}
		});
	}

	/**
	 * 安装apk
	 * 
	 * @param url
	 */
	public static void installApk(Context context, String apkFilePath) {
		File apkfile = new File(apkFilePath);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		context.startActivity(i);
	}

	
}
