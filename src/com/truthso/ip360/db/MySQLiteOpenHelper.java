package com.truthso.ip360.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 创建数据库和表
 * @author wsx_summer  Email:wangshaoxia@truthso.com
 * @date 创建时间：2016年6月7日下午2:39:44
 * @version 1.0  
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

	public MySQLiteOpenHelper(Context context) {
		super(context, "IP360.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IP360_user (userId text PRIMARY KEY , userName text ,loginId text , icon text , password text , autoLogin text , email text)");
		db.execSQL("CREATE TABLE IP360_media_detail(id integer PRIMARY KEY autoincrement,title text,createTime text,fileSize text,recordTime text,jsonObject text,resourceUrl text,type integer,lable text,remark text,location text)");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
 
}
