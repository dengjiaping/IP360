package com.truthso.ip360.utils;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.provider.Settings;

public class NetStatusUtil {

	private static AlertDialog dialog;

	/**
	 * 3G网络是否已打开
	 * 
	 * @param context
	 * @return true:已打开
	 */
	public static boolean is3GValid(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		State mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		if (State.CONNECTED == mobileState) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 3G无效时转向设置页面
	 * 
	 * @param context
	 * @return
	 */
	public static void is3GInvalidSetting(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		State mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		if (State.CONNECTED != mobileState) {
			context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
		}
	}

	/**
	 * wifi是否已打开
	 * 
	 * @param context
	 * @return true:已打开
	 */
	public static boolean isWifiValid(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		State wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		if (State.CONNECTED == wifiState) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * wifi未打开时转向设置页面
	 * 
	 * @param context
	 * @return
	 */
	public static void isWifiInvalidSetting(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		State mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		if (State.CONNECTED != mobileState) {
			context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
		}
	}

	/**
	 * 网络是否可用
	 * 
	 * @param context
	 * @return true:可用
	 */
	public static boolean isNetValid(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netStates = cm.getAllNetworkInfo();
		for (NetworkInfo item : netStates) {
			if (item.getState() == State.CONNECTED) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 对网络连接状态进行判断
	 * 
	 * @return true, 可用； false， 不可用
	 */
	public static boolean isOpenNetwork(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager.getActiveNetworkInfo() != null) {
			return connManager.getActiveNetworkInfo().isAvailable();
		}
		return false;
	}

	/**
	 * 提示用户，并调出网络设置
	 */
	public static void setUpNetWork(final Context context) {
		if (dialog != null && dialog.isShowing()) {
			return;
		}
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context).setTitle("网络信息").setMessage("网络无连接,请先连接网络！").setCancelable(false).setPositiveButton("是", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialoginterface, int i) {
				Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
				context.startActivity(intent);
			}
		}).setNegativeButton("否", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		dialog = dialogBuilder.create();
		dialog.show();
	}

	public static void dissmissDialog() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}
}
