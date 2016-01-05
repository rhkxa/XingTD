package com.gps808.app.timeout;

import java.lang.reflect.Method;
import java.util.List;

import com.gps808.app.utils.LogUtils;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.util.Log;

public class ScreenObserver {


    private static String TAG = "ScreenObserver";
    private Context mContext;
    private ScreenBroadcastReceiver mScreenReceiver;
    private ScreenStateListener mScreenStateListener;
    private static Method mReflectScreenState;
 
    public ScreenObserver(Context context) {
        mContext = context;
        mScreenReceiver = new ScreenBroadcastReceiver();
        try {
            mReflectScreenState = PowerManager.class.getMethod("isScreenOn",
                    new Class[] {});
        } catch (NoSuchMethodException nsme) {
            Log.d(TAG, "API < 7," + nsme);
        }
    }
 
    /**
     * screen状态广播接收者
     * 
     * @author xishaomin
     * 
     */
    private class ScreenBroadcastReceiver extends BroadcastReceiver {
        private String action = null;
 
        @Override
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                mScreenStateListener.onScreenOn();
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                mScreenStateListener.onScreenOff();
            }else if(Intent.ACTION_USER_PRESENT.equals(action)){
                LogUtils.DebugLog("---->屏幕解锁完成");
            }
        }
    }
 
    /**
     * 请求screen状态更新
     * 
     * @param listener
     */
    public void requestScreenStateUpdate(ScreenStateListener listener) {
        mScreenStateListener = listener;
        startScreenBroadcastReceiver();
 
        firstGetScreenState();
    }
 
    /**
     * 第一次请求screen状态
     */
    private void firstGetScreenState() {
        PowerManager manager = (PowerManager) mContext
                .getSystemService(Activity.POWER_SERVICE);
        if (isScreenOn(manager)) {
            if (mScreenStateListener != null) {
                mScreenStateListener.onScreenOn();
            }
        } else {
            if (mScreenStateListener != null) {
                mScreenStateListener.onScreenOff();
            }
        }
    }
 
    /**
     * 停止screen状态更新
     */
    public void stopScreenStateUpdate() {
        mContext.unregisterReceiver(mScreenReceiver);
    }
 
    /**
     * 启动screen状态广播接收器
     */
    private void startScreenBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        //当用户解锁屏幕时
        filter.addAction(Intent.ACTION_USER_PRESENT);
        mContext.registerReceiver(mScreenReceiver, filter);
    }
 
    /**
     * screen是否打开状态
     * 
     * @param pm
     * @return
     */
    private static boolean isScreenOn(PowerManager pm) {
        boolean screenState;
        try {
            screenState = (Boolean) mReflectScreenState.invoke(pm);
        } catch (Exception e) {
            screenState = false;
        }
        return screenState;
    }
 
    public interface ScreenStateListener {
        public void onScreenOn();
 
        public void onScreenOff();
    }
    /**
     * 判断屏幕是否已被锁定
     * @param c
     * @return
     */
    public final static boolean isScreenLocked(Context c) 
    {  
        android.app.KeyguardManager mKeyguardManager = (KeyguardManager) c  
                .getSystemService(Context.KEYGUARD_SERVICE);  
        return mKeyguardManager.inKeyguardRestrictedInputMode();  
    }  
     
    /**
     * 判断当前应用是否是本应用
     * @param context
     * @return
     */
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}
