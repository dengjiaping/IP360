package com.truthso.ip360.application;

import java.util.LinkedHashMap;
import java.util.concurrent.Future;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.updownload.UpLoadRunnable;
import com.truthso.ip360.utils.SharePreferenceUtil;

/**
 * application
 * 
 * @author wsx_summer Email：wangshaoxia@truthso.com
 * @date 创建时间：2016年5月30日上午11:54:49
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class MyApplication extends Application {
	/**
	 * 获取到主线程的上下文
	 */
	private static MyApplication mContext;
	private static MyApplication theInstance;
	/**
	 * 获取到主线程的handler
	 */
	private static Handler mMainThreadHandler;
	/**
	 * 获取到主线程
	 */
	private static Thread mMainThread;

	/**
	 * 获取到主线程的轮询器
	 */
	private static Looper mMainThreadLooper;

	/**
	 * 获取到主线程id
	 */
	private static int mMainTheadId;
	
	private static ImageLoader imageLoader;
	
	@Override
	public void onCreate() {
		super.onCreate();
		theInstance = this;
		mMainThreadHandler = new Handler();
		mMainThread = Thread.currentThread();
		mMainThreadLooper = getMainLooper();
		mMainTheadId = android.os.Process.myTid();
		
	}

	

	public static Handler getMainThreadHandler() {
		return mMainThreadHandler;
	}

	public static Thread getMainThread() {
		return mMainThread;
	}

	public static Looper getMainThreadLooper() {
		return mMainThreadLooper;
	}

	public static int getMainThreadId() {
		return mMainTheadId;
	}

	public static MyApplication getInstance() {		
		return theInstance;
	}
	public static MyApplication getApplication() {
		return mContext;
	}

		
	// 获取屏幕宽度
	public int getScreanWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager mWm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		mWm.getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	//获取屏幕高度
	public int getScreanHeight() {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager mWm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		mWm.getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}
	
	//获取手机的IMEI
	public String getDeviceImei(){
		String Imei = ((TelephonyManager)getSystemService(TELEPHONY_SERVICE))
				.getDeviceId();
		return Imei;
	}
	
	public ImageLoader getImageLoader(){
		if(imageLoader==null){
			imageLoader = ImageLoader.getInstance();
			ImageLoaderInitialize(this);
			return imageLoader;
		}
		ImageLoaderInitialize(this);
		return imageLoader;	
	}
    public void ImageLoaderInitialize(Context context) {
		
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).memoryCacheExtraOptions(480, 800) // max																										// height，即保存的每个缓存文件的最大长宽
				.threadPoolSize(3)// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory().memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You																						// implementation/你可以通过自己的内存缓存实现
				.memoryCacheSize(2 * 1024 * 1024).discCacheSize(50 * 1024 * 1024).discCacheFileNameGenerator(new Md5FileNameGenerator())// 将保存的时候的URI名称用MD5																															// 加密
				.tasksProcessingOrder(QueueProcessingType.LIFO).discCacheFileCount(100) // 缓存的文件数量
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple()).imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout																																			// s)超时时间
				.build();// 开始构建
	}

    public String getTokenId (){
		return (String)SharePreferenceUtil.getAttributeByKey(this, MyConstants.SP_USER_KEY, "token", com.truthso.ip360.utils.SharePreferenceUtil.VALUE_IS_STRING);
	}
    
}