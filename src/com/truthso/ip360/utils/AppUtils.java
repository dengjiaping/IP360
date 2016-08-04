package com.truthso.ip360.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 应用工具类
 * @author wsx_summer
 * @date 创建时间：2016年5月27日下午6:05:51
 * @version 1.0  
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class AppUtils {
	private static AppUtils appUtils;

	private AppUtils(){
		
	}
	
	/**
	 * 单例
	 * @return
	 */
	public static AppUtils getAppUtils() {
		if (appUtils == null) {
			appUtils = new AppUtils();
		}
		return appUtils;
	}

	/**
	 * 获取App安装包信息
	 * 
	 * @return
	 */
	public PackageInfo getPackageInfo() {
//		PackageInfo info = null;
//		try {
//			info =appContext.getPackageManager().getPackageInfo(
//					appContext.getPackageName(), 0);
//		} catch (NameNotFoundException e) {
//			e.printStackTrace(System.err);
//		}
//		if (info == null)
//			info = new PackageInfo();
//		return info;
		return null;
	}

	/**
	 * 获取App唯一标识
	 * 
	 * @return
	 */
	public String getAppId() {
//		String uniqueID = appContext.getProperty(AppConfig.CONF_APP_UNIQUEID);
//		if (StringUtils.isEmpty(uniqueID)) {
//			uniqueID = UUID.randomUUID().toString();
//			appContext.setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
//		}
//		return uniqueID;
		return "";
	}

	/**
	 * 获取当前格式化时间
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat") 
	public static String getCurrentTime() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(date);
		return dateString;
	}

	/**
	 * 在文件名后面加格式化时间
	 * 
	 * @param fileName
	 * @return
	 */
	@Deprecated
	public static String chanFileName(String fileName) {
		// int las = fileName.lastIndexOf(".");
		// if(las!=-1) {
		// String s_first = fileName.substring(0, las);
		// String s_sec = fileName.substring(fileName.lastIndexOf("."),
		// fileName.length());
		// fileName= s_first+getCurrentTime()+s_sec;
		// }else {
		// fileName= fileName+getCurrentTime();
		// }
		return fileName;
	}

	@Deprecated
	public static String changeFilename2(String fileName) {
		// int las = fileName.lastIndexOf(".");
		// if(las!=-1) {
		// String s_first = fileName.substring(0, las);
		// s_first = s_first.substring(0, s_first.length()-14);
		// String s_sec = fileName.substring(fileName.lastIndexOf("."),
		// fileName.length());
		// fileName= s_first+s_sec;
		// }else {
		// fileName= fileName.substring(0, fileName.length()-14);
		// }
		return fileName;
	}

	/**
	 * 检测网络是否可用
	 * 
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	/**
	 * 获取系统版本号
	 * 
	 * @return
	 */
	public static int getSDKVersionNumber() {
		int sdkVersion;
		try {
			sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK_INT);
		} catch (NumberFormatException e) {
			sdkVersion = 0;
		}
		return sdkVersion;
	}

	public static boolean checkBrowser(Context context, String packageName) {
		if (packageName == null || "".equals(packageName))
			return false;
		try {
			ApplicationInfo info = context.getPackageManager()
					.getApplicationInfo(packageName,
							PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	public static boolean checkBrowser(Context context) {
		try {
			ApplicationInfo info = context.getPackageManager()
					.getApplicationInfo("com.appsino.bingluo.fycz",
							PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

//	public static boolean isServiceStarted(Context context, String PackageName) {
//		boolean isStarted = false;
//		try {
//			ActivityManager mActivityManager = (ActivityManager) context
//					.getSystemService(Context.ACTIVITY_SERVICE);
//			int intGetTastCounter = 1000;
//			List<ActivityManager.RunningServiceInfo> mRunningService = mActivityManager
//					.getRunningServices(intGetTastCounter);
//			for (ActivityManager.RunningServiceInfo amService : mRunningService) {
//				if (0 == amService.service.getPackageName().compareTo(
//						PackageName)) {
//					isStarted = true;
//					break;
//				}
//			}
//		} catch (SecurityException e) {
//			e.printStackTrace();
//		}
//		return isStarted;
//	}

	/**
	 * 判断应用是否应用
	 * 
	 */
//	public static boolean isTopActivity(Context context) {
//		ActivityManager activityManager = (ActivityManager) context
//				.getSystemService(Context.ACTIVITY_SERVICE);
//		List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
//		if (tasksInfo.size() > 0) {
//			// 应用程序位于堆栈的顶层
//			if ("com.appsino.bingluo.fycz".equals(tasksInfo.get(0).topActivity
//					.getPackageName())) {
//				return true;
//			}
//		}
//		return false;
//	}
}
