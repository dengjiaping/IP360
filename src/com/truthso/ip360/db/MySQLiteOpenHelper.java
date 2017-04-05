package com.truthso.ip360.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.truthso.ip360.constants.MyConstants;

/**
 * 创建数据库和表
 * @author wsx_summer  Email:wangshaoxia@truthso.com
 * @date 创建时间：2016年6月7日下午2:39:44
 * @version 1.0  
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

	public MySQLiteOpenHelper(Context context) {
		super(context, "IP360.db", null, MyConstants.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IP360_user (userId text PRIMARY KEY , userName text ,loginId text , icon text , password text , autoLogin text , email text)");
		db.execSQL("CREATE TABLE IP360_media_detail(id integer PRIMARY KEY autoincrement,title text,createTime text,fileSize text,videoTime text,recordTime text,jsonObject text,resourceUrl text,type integer,lable text,remark text,location text,status text,expStatus integer,llsize varchar(10),pkvalue varchar(10),fileformat varchar(10),userId Integer,dataType Integer)");
		db.execSQL("CREATE TABLE updownloadlog (_id integer primary key autoincrement,sourceid varchar(10), uploadfilepath varchar(100), downloadurl varchar(100),filename varchar(100),filesize varchar(100),position varchar(100),downorupload varchar (10),objectkey varchar(100), llsize varchar(10),status varchar(10),fileurlformatname varchar(100),userId Integer,dataType Integer,remark text,fileformat varchar(10))");
	}

/*	(sourceid,uploadfilepath,downloadurl,filename,filesize,position,downorupload,objectkey,llsize,status,fileurlformatname,userId,dataType)*/
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(oldVersion==1){
			db.beginTransaction();
			try {
				db.execSQL("ALTER  TABLE updownloadlog RENAME TO updownloadlog_temp");
				db.execSQL("CREATE TABLE updownloadlog (_id integer primary key autoincrement,sourceid varchar(10), uploadfilepath varchar(100), downloadurl varchar(100),filename varchar(100),filesize varchar(100),position varchar(100),downorupload varchar (10),objectkey varchar(100), llsize varchar(10),status varchar(10),fileurlformatname varchar(100),userId Integer,dataType Integer,remark varchar)");
				db.execSQL("INSERT INTO updownloadlog  SELECT *,'' FROM updownloadlog_temp");
				db.execSQL("DROP TABLE updownloadlog_temp");
				db.setTransactionSuccessful();
			}finally {
				db.endTransaction();
			}
		}
	}
 
}
