package com.gps808.app.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baidu.mapapi.SDKInitializer;
import com.gps808.app.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tendcloud.tenddata.TCAgent;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

/**
 * @ClassName: HuaShiApplication
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author JIA
 * @date 2013-11-25 下午2:06:20
 * 
 */
public class XtdApplication extends Application {

	private List<Activity> activities = new ArrayList<Activity>();
	private static XtdApplication instance;
	// 用于存放倒计时时间
	public static Map<String, Long> map;

	public static XtdApplication getInstance() {
		if (null == instance) {
			instance = new XtdApplication();
		}
		return instance;
	}

	@Override
	public void onCreate() {
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
		// StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		// .detectAll().penaltyLog().penaltyDialog().build());
		// StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
		// .detectAll().penaltyLog().penaltyDeath().build());
		// }

		super.onCreate();
		// initLocation(getApplicationContext());
		SDKInitializer.initialize(this);
		initImageLoader(getApplicationContext());
		TCAgent.LOG_ON = false;
		TCAgent.init(this);
		TCAgent.setReportUncaughtExceptions(true);

	}

	public void addActivity(Activity activity) {
		activities.add(activity);
	}

	public void exit() {
		for (Activity activity : activities) {
			activity.finish();
		}
		System.exit(0);
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.threadPoolSize(3).denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(30 * 1024 * 1024)
				.defaultDisplayImageOptions(options)
				// 10 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		ImageLoader.getInstance().clearMemoryCache();

	}

}
