package com.truthso.ip360.system;

import android.content.Context;
import android.widget.Toast;

/**
 * 系统的toaster提示框
 * 
 * @author wsx_summer
 * @date 创建时间：2016年5月27日下午3:14:47
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class Toaster {
	public static Toast sToast;
	private static final boolean DEBUG = true;

	private static void makeToast(Context context, String text, int duration) {
		Toast.makeText(context, text, duration).show();
	}

	public static void showToast(Context mContext, String msg) {
		if (sToast == null) {
			sToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
		}
		sToast.setText(msg);
		sToast.show();
	}

	/**
	 * 线上环境
	 * 
	 * @param context
	 * @param resId
	 * @param duration
	 */

	public static void toast(Context context, String text, int duration) {
		makeToast(context, text, duration);
	}

	/**
	 * 线上环境
	 * 
	 * @param context
	 * @param resId
	 * @param duration
	 */
	public static void toast(Context context, int resId, int duration) {
		makeToast(context, context.getString(resId), duration);
	}

	/**
	 * 开发模式
	 * 
	 * @param context
	 * @param resId
	 * @param duration
	 */
	public static void debugToast(Context context, String text, int duration) {
		if (DEBUG) {
			makeToast(context, text, duration);
		}
	}

	/**
	 * 开发模式
	 * 
	 * @param context
	 * @param resId
	 * @param duration
	 */
	public static void debugToast(Context context, int resId, int duration) {
		if (DEBUG) {
			makeToast(context, context.getString(resId), duration);
		}
	}

}
