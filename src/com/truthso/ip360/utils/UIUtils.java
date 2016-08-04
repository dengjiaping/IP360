package com.truthso.ip360.utils;




import com.truthso.ip360.application.MyApplication;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.provider.MediaStore.Video;
import android.view.LayoutInflater;
import android.view.View;

public class UIUtils {

	public static Context getContext() {
		return MyApplication.getApplication();
	}

	public static Thread getMainThread() {
		return MyApplication.getMainThread();
	}

	public static long getMainThreadId() {
		return MyApplication.getMainThreadId();
	}

	/** dip转换px */
	public static int dip2px(int dip) {
		final float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}

	/** pxz转换dip */
	public static int px2dip(int px) {
		final float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	/** 获取主线程的handler */
	public static Handler getHandler() {
		return MyApplication.getMainThreadHandler();
	}

	/** 延时在主线程执行runnable */
	public static boolean postDelayed(Runnable runnable, long delayMillis) {
		return getHandler().postDelayed(runnable, delayMillis);
	}

	/** 在主线程执行runnable */
	public static boolean post(Runnable runnable) {
		
		return getHandler().post(runnable);
	}

	/** 从主线程looper里面移除runnable */
	public static void removeCallbacks(Runnable runnable) {
		getHandler().removeCallbacks(runnable);
	}

	public static View inflate(int resId){
		return LayoutInflater.from(getContext()).inflate(resId,null);
	}

	/** 获取资源 */
	public static Resources getResources() {
		return getContext().getResources();
	}

	/** 获取文字 */
	public static String getString(int resId) {
		return getResources().getString(resId);
	}

	/** 获取文字数组 */
	public static String[] getStringArray(int resId) {
		return getResources().getStringArray(resId);
	}

	/** 获取dimen */
	public static int getDimens(int resId) {
		return getResources().getDimensionPixelSize(resId);
	}

	/** 获取drawable */
	public static Drawable getDrawable(int resId) {
		return getResources().getDrawable(resId);
	}

	/** 获取颜色 */
	public static int getColor(int resId) {
		return getResources().getColor(resId);
	}

	/** 获取颜色选择�?? */
	public static ColorStateList getColorStateList(int resId) {
		return getResources().getColorStateList(resId);
	}
	//判断当前的线程是不是在主线程 
	public static boolean isRunInMainThread() {
		return android.os.Process.myTid() == getMainThreadId();
	}
    
	public static void runInMainThread(Runnable runnable) {
		if (isRunInMainThread()) {
			runnable.run();
		} else {
			post(runnable);
		}
	}

	/**
	 * 获得视频缩略图
	 * @param videoPath
	 * @param width
	 * @param height
	 * @param kind
	 * @return
	 */
	public static Bitmap getVideoThumbnail( String videoPath,int width, int height,int kind) {
		Bitmap bitmap = null;
		// 获取视频的缩略图
		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, Video.Thumbnails.MINI_KIND);
//		System.out.println("w"+bitmap.getWidth());
//		System.out.println("h"+bitmap.getHeight());
//		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
//		ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

//	/** 对toast的简易封装�?�线程安全，可以在非UI线程调用�?? */
//	public static void showToastSafe(final int resId) {
//		showToastSafe(getString(resId));
//	}

//	/** 对toast的简易封装�?�线程安全，可以在非UI线程调用�?? */
//	public static void showToastSafe(final String str) {
//		if (isRunInMainThread()) {
//			showToast(str);
//		}
//		else {
//			post(new Runnable() {
//				@Override
//				public void run() {
//					showToast(str);
//				}
//			});
//		}
//		
		
		
//	}

	
}


