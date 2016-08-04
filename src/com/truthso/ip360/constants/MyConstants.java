package com.truthso.ip360.constants;

import android.os.Environment;

/**
 * 存放常量的类
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016年6月7日下午3:40:09
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class MyConstants {

	// 0照片，1视频，2录音

	public static final int PHOTO = 0;
	public static final int VIDEO = 1;
	public static final int RECODE = 2;
	/**
	 * 判断是否是第一次进入应用的文件标识
	 */
	public final static String SP_ISFIRST_IN_TAG= "sp_isFirst_in_tag";
	/**
	 * 是否是第一次进来
	 */
	public final static  String APP_ISFIRST_IN="app_isFirst_in";
	
	/**
	 * 照片保存路径
	 */
	public final static  String PHOTO_PATH=Environment.getExternalStorageDirectory()+"/ip360/photo";
	
	/**
	 * 数据库表名_media
	 */
	public final static  String TABLE_MEDIA_DETAIL="IP360_media_detail";
	
	public static int GPS_LOOP_TIME = 30;// gps轮询定位时间单位（秒）
	public final static String SP_SYSTEM_CONFIG="IP360_services_system_sp";
}
