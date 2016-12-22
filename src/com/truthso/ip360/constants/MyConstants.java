package com.truthso.ip360.constants;

import java.util.Map;

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
	//回传时候的resultCode
	
	public static final int REALNAME_VERTIFICATION = 0;//实名认证
	public static final int BINDNEWEMAIL = 1;//未绑定邮箱的情况下绑定邮箱
	public static final int OFFBIND_BINDNEWEMAIL = 2;//解绑旧邮箱绑定新邮箱
	public static final int BINDNEWEMOBILE = 3;//未绑定手机号时绑定手机号
	public static final int OFFBIND_BINDNEWEMOBILE = 4;//解绑旧手机号绑定新手机号
	public static final int ACCOUNT_YUE = 5;//账户余额

	//SD卡路径
	public static final String SD_ROOT = Environment
			.getExternalStorageDirectory().getAbsolutePath();
	
	
	// 文件类型 0照片，1视频，2录音 3云端拍照 4云端视频 5云端录音6确权文件 7线上取证

	public static final int PHOTO = 0;
	public static final int VIDEO = 1;
	public static final int RECORD = 2;
	public static final int CLOUD_PHOTO = 3;
	public static final int CLOUD_VIDEO = 4;
	public static final int CLOUD_RECORD = 5;
	public static final int QUEQUAN = 6;
	public static final int XSQZ = 7;
	
	//1-注册；2-找回密码；3-绑定手机号；4-解绑旧手机号5-绑定新手机号6-绑定邮箱7-解绑旧邮箱8-绑定新邮箱
	public static final String REGISTER = "1";
	public static final String FIND_PWD = "2";
	public static final String BIND_PHONUM = "3";
	public static final String OFFBIND_PHONUM = "4";
	public static final String BINDNEW_PHONUM = "5";
	public static final String BINDEMAIL = "6";
	public static final String OFFBIND_EMAIL = "7";
	public static final String BINDNEW_EMAIL = "8";
	
	//取证的业务类型 50001现场取证拍照 50002 现场取证录音 50003现场取证录像
	public static final int PHOTOTYPE = 50001;
	public static final int VIDEOTYPE = 50003;
	public static final int RECORDTYPE = 50002;
	/**
	 * 判断是否是第一次进入应用的文件标识
	 */
	public final static String APP_KEY= "app_key";
	/**
	 * 是否是第一次进来
	 */
	public final static  String APP_ISFIRST_IN="app_isFirst_in";
	
	/**
	 * 照片保存路径
	 */
	public final static  String PHOTO_PATH=Environment.getExternalStorageDirectory()+"/ip360/photo";
	
	/**
	 * 下载证据路径
	 */
	public final static  String DOWNLOAD_PATH=Environment.getExternalStorageDirectory()+"/ip360/download";
	/**
	 *录音保存路径
	 */
	public final static  String RECORD_PATH=Environment.getExternalStorageDirectory()+"/ip360/record";
	/**
	 * 数据库表名_media
	 */
	public final static  String TABLE_MEDIA_DETAIL="IP360_media_detail";
	
	public static int GPS_LOOP_TIME = 30;// gps轮询定位时间单位（秒）
	public final static String SP_SYSTEM_CONFIG="IP360_services_system_sp";
	/**
	 * 保存的用户信息
	 */
	public final static String SP_USER_KEY = "IP360_user_key";

	/**
	 * 是否wifi下上传
	 */
	public final static String ISWIFI = "IsWifi";
	

}
