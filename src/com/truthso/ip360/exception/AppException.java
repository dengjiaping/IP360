package com.truthso.ip360.exception;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;

import com.truthso.ip360.activity.R;
import com.truthso.ip360.system.AppManager;
import com.truthso.ip360.system.Toaster;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

/**
 * 捕获全局异常的类
 * @author wsx_summer
 * @date 创建时间：2016年5月27日下午3:07:09
 * @version 1.0  
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class AppException extends Exception implements UncaughtExceptionHandler{


	private static final long serialVersionUID = 21736823917L;

	/** 系统默认的UncaughtException处理类 */
	private Thread.UncaughtExceptionHandler mDefaultHandler;

	/** 定义异常类型 */
	public final static byte NET_NOT_CONNECT = 0x01;//无网络连接
	public final static byte PARSE_ERROR = 0x02;// 解析错误
	public final static byte NET_ERROR = 0x03;// 连接超时，服务器协议错误，
	public final static boolean DEBUG = false;// 是否保存错误日志信息
	private byte type;

	private AppException() {
		this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	private AppException(byte type, Exception excp) {
		super(excp);
		this.type = type;
		if (DEBUG) {
			
		}
	}

	/**
	 * 无网络连接
	 * 
	 * @param e
	 * @return
	 */
	public static AppException netNotConnect(Exception e) {
		return new AppException(NET_NOT_CONNECT, e);
	}

	/**
	 * 解析错误
	 * 
	 * @param e
	 * @return
	 */
	public static AppException parserError(Exception e) {
		return new AppException(PARSE_ERROR, e);
	}

	/**
	 * 连接超时，服务器协议错误
	 * 
	 * @param e
	 * @return
	 */
	public static AppException netError(Exception e) {
		return new AppException(NET_ERROR, e);
	}

	/**
	 * 获取APP异常对象
	 * 
	 * @param context
	 * @return
	 */
	public static AppException getAppExceptionHandler() {
		return new AppException();
	}

	/**
	 * 提示的错误信息
	 * 
	 * @param ctx
	 */
	public void makeToast(Context ctx) {
		switch (this.getType()) {
		case NET_NOT_CONNECT:
			Toaster.toast(ctx, R.string.network_not_connected,
					Toast.LENGTH_SHORT);
			break;
		case PARSE_ERROR:
			Toaster.toast(ctx, R.string.json_parser_failed, Toast.LENGTH_SHORT);
			break;
		case NET_ERROR:
			Toaster.toast(ctx, R.string.http_exception,
					Toast.LENGTH_SHORT);
			break;
		}
	}

	/**
	 * 获取异常信息内容
	 * 
	 * @param ctx
	 * @return
	 */
	public String getMsgText(Context ctx) {
		String res = "";
		switch (this.getType()) {
		case NET_NOT_CONNECT:
			res = ctx.getResources().getString(R.string.network_not_connected);
			break;
		case PARSE_ERROR:
			res = ctx.getResources().getString(R.string.json_parser_failed);
			break;
		case NET_ERROR:
			res = ctx.getResources().getString(R.string.http_exception);
			break;
		}
		return res;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO Auto-generated method stub
		if (!handleException(ex) && mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(thread, ex);
		}
	}

	/**
	 * 自定义异常处理:收集错误信息&发送错误报告
	 * 
	 * @param ex
	 * @return true:处理了该异常信息;否则返回false
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}

		final Context context = AppManager.getAppManager().currentActivity();

		if (context == null) {
			return false;
		}

		AppManager.getAppManager().AppExit(context);// 自定义方法，关闭当前打开的所有avtivity
		return true;
	}

	/**
	 * 保存异常日志
	 * 
	 * @param excp
	 */
	public void saveErrorLog(Exception excp) {
		String errorlog = "errorlog.txt";
		String savePath = "";
		String logFilePath = "";
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			// 判断是否挂载了SD卡
			String storageState = Environment.getExternalStorageState();
			if (storageState.equals(Environment.MEDIA_MOUNTED)) {
				savePath = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/IP360/Log/";
				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				logFilePath = savePath + errorlog;
			}
			// 没有挂载SD卡，无法写文件
			if (logFilePath == "") {
				return;
			}
			File logFile = new File(logFilePath);
			if (!logFile.exists()) {
				logFile.createNewFile();
			}
			fw = new FileWriter(logFile, true);
			pw = new PrintWriter(fw);
			pw.println("--------------------" + ((new Date()).toLocaleString())
					+ "---------------------");
			excp.printStackTrace(pw);
			pw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
				}
			}
		}

	}


}
