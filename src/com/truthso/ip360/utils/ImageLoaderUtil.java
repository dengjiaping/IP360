package com.truthso.ip360.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.truthso.ip360.application.MyApplication;

/**
 * @author 浩
 *
 */
public class ImageLoaderUtil {
	private static DisplayImageOptions options;
	private static  ImageLoader imageLoader;
	/**
	 * @param downloadImage 下载期间显示的图片
	 * @param uriErrorImage  Uri为空或是错误的时候显示的图片 
	 * @param loadingErrorImage  加载/解码过程中错误时候显示的图片
	 * @param delayLoadingTime  延时多少时间后开始下载
	 * @param iscacheMem  是否缓存在内存中
	 * @param iscacheSD 是否缓存在SD卡中
	 */
	public static  DisplayImageOptions setOptions(int downloadImage,int uriErrorImage,int loadingErrorImage,
			int delayLoadingTime,Boolean iscacheMem,Boolean iscacheSD){
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(downloadImage)//设置图片在下载期间显示的图片 
				.showImageForEmptyUri(uriErrorImage)//设置图片Uri为空或是错误的时候显示的图片 
				.showImageOnFail(loadingErrorImage)//设置图片加载/解码过程中错误时候显示的图片
				.delayBeforeLoading(delayLoadingTime)//设置延时多少时间后开始下载
				.cacheInMemory(iscacheMem)//设置下载的图片是否缓存在内存中
				.cacheOnDisk(iscacheSD)// 设置下载的资源是否缓存在SD卡中
				.considerExifParams(true)// 是否考虑JPEG图像EXIF参数（旋转，翻转）
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)//设置图片以何种编码方式显示
				.bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型
				.displayer(new FadeInBitmapDisplayer(1000))//是否图片加载好后渐入的动画时间
				.build();
		return options;
	}
	/**
	 * @param uri
	 * @param imageView
	 * @param opt
	 */
	public static void dispalyImage(String uri,ImageView imageView,DisplayImageOptions opt)
	{
		MyApplication.getInstance().getImageLoader().displayImage(uri, imageView, opt);
	}
	
	/**
	 * @param uri
	 * @param imageView
	 */
	public static void dispalyImage(String uri,ImageView imageView)
	{
//		imageLoader.init(ImageLoaderConfiguration.createDefault(MainActivity.this));
		MyApplication.getInstance().getImageLoader().displayImage(uri, imageView);
	}
	
	/**
	 * @param uri
	 * @param imageView
	 * @param listener
	 */
	public static void dispalyImage(String uri,ImageView imageView,ImageLoadingListener listener)
	{
		MyApplication.getInstance().getImageLoader().displayImage(uri, imageView,listener);
	}
	
	/**
	 * @param uri
	 * @param imageView
	 * @param opt
	 * @param listener
	 */
	public static void dispalyImage(String uri,ImageView imageView,DisplayImageOptions opt,ImageLoadingListener listener)
	{
		MyApplication.getInstance().getImageLoader().displayImage(uri, imageView, opt, listener);
	}
	/**
	 * @param uri
	 * @param imageView
	 * @param opt
	 * @param listener
	 * @param progressListener
	 */
	public static void dispalyImage(String uri,ImageView imageView,DisplayImageOptions opt,
			ImageLoadingListener listener,ImageLoadingProgressListener progressListener )
	{
		MyApplication.getInstance().getImageLoader().displayImage(uri, imageView, opt, listener, progressListener);
	}
	/**
	 * 从内容提提供者中抓取图片
	 */
	public static  void displayFromContentopt(String uri, ImageView imageView,DisplayImageOptions opt) {
		// String imageUri = "content://media/external/audio/albumart/13"; //
		// from content provider
		MyApplication.getInstance().getImageLoader().displayImage("content://" + uri, imageView,opt);
	}
	/**
	 * 从drawable中异步加载本地图片
	 * 
	 * @param imageId
	 * @param imageView
	 */
	public static  void displayFromDrawableopt(String uri, ImageView imageView,DisplayImageOptions opt) {
		// String imageUri = "content://media/external/audio/albumart/13"; //
		// from content provider
		MyApplication.getInstance().getImageLoader().displayImage("drawable://" + uri, imageView,opt);
	}
	/**
	 * 从assets文件夹中异步加载图片
	 * 
	 * @param imageName
	 *            图片名称，带后缀的，例如：1.png
	 * @param imageView
	 */
	public static  void dispalyFromAssetsopt(String uri, ImageView imageView,DisplayImageOptions opt) {
		// String imageUri = "content://media/external/audio/albumart/13"; //
		// from content provider
		MyApplication.getInstance().getImageLoader().displayImage("assets://" + uri, imageView,opt);
	}
	/**
	 * 从内存卡中异步加载本地图片
	 * 
	 * @param uri
	 * @param imageView
	 */
	public static  void displayFromSDCardopt(String uri, ImageView imageView,DisplayImageOptions opt) {
		// String imageUri = "content://media/external/audio/albumart/13"; //
		// from content provider
		MyApplication.getInstance().getImageLoader().displayImage("file://" + uri, imageView,opt);
	}

}
