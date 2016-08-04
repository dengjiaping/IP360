package com.truthso.ip360.system;

import java.util.Enumeration;
import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * 应用程序管理
 * @author wsx_summer
 * @date 创建时间：2016年5月27日下午3:47:10
 * @version 1.0  
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class AppManager {

	private static Stack<Activity> activityStack;//堆栈，存储activity
	private static AppManager instance;

	private AppManager() {
	}

	/**
	 * 单例
	 */
	public static AppManager getAppManager() {
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}

	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		Enumeration<Activity> elements=activityStack.elements();
		while(elements.hasMoreElements()){
			Activity activity=elements.nextElement();
			if(null!=activity)activity.finish();
		}
		activityStack.clear();
	}

	/**
	 * 退出应用程序
	 */
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			ActivityManager activityMgr = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
			// activityMgr.killBackgroundProcesses(context.getPackageName());
		} catch (Exception e) {
			
		}
	}

	public static void hideSoftKeyboard(Activity activity) {
		try {
			InputMethodManager inputMethodManager = (InputMethodManager) activity
					.getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(activity
					.getCurrentFocus().getWindowToken(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 软键盘的控制，判断是否点击的是edittext来控制
	 */
	public static void setTouchUI(View rootView, final Activity activity) {
		// Set up touch listener for non-text box views to hide keyboard.
		try {
			if (!((rootView instanceof EditText) || (rootView instanceof Button))) {
				rootView.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View arg0, MotionEvent arg1) {
						hideSoftKeyboard(activity);
						return false;
					}
				});
			}

			// If a layout container, iterate over children and seed recursion.
			if (rootView instanceof ViewGroup) {
				for (int i = 0; i < ((ViewGroup) rootView).getChildCount(); i++) {
					View innerView = ((ViewGroup) rootView).getChildAt(i);
					setTouchUI(innerView, activity);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 用share preference来实现是否绑定的开关。在ionBind且成功时设置true，unBind且成功时设置false
	public static boolean hasBind(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		String flag = sp.getString("bind_flag", "");
		if ("ok".equalsIgnoreCase(flag)) {
			return true;
		}
		return false;
	}

	public static void setBind(Context context, boolean flag) {
		String flagStr = "not";
		if (flag) {
			flagStr = "ok";
		}
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putString("bind_flag", flagStr);
		editor.commit();
	}

	/**
	 * 开始绑定推送,并且发送请求到后台
	 */

	public void bindYunMessage(Context context) {
		// if(!AppManager.hasBind(context)){
		// Log.i("TAG", "-------------->开始绑定百度推送");
		// PushManager.startWork(context,PushConstants.LOGIN_TYPE_API_KEY,Utils.getMetaValue(context,
		// "api_key"));
		// }else{
		// Log.i("TAG", "-------------->百度推送已经绑定过");
		// UploadBindBaiduPushInfo.getInstance().upload(context);
		// }
	}

	/**
	 * 获取手机号
	 * 
	 * @param context
	 */
	public String getPhoneNum(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm == null)
			return null;
		return tm.getLine1Number();
	}

	/**
	 * 拨打电话
	 */
	public void call(Context context, String phoneNum) throws Exception {
		if (phoneNum != null && !phoneNum.equals("")) {
			Uri uri = Uri.parse("tel:" + phoneNum);
			Intent intent = new Intent(Intent.ACTION_DIAL, uri);
			context.startActivity(intent);
		}
	}

	/**
	 * 打开网页
	 */
	public void openWeb(Context context, String url) {
		try {
			Uri uri = Uri.parse(url);
			context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
