package com.truthso.ip360.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.db.MySQLiteOpenHelper;

/**
 * 群组的dao
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016年6月7日下午5:09:12
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class GroupDao {
	private Context ctx;

	private GroupDao(Context ctx) {
		this.ctx = ctx;
		dbHelper = new MySQLiteOpenHelper(ctx);
	}

	private static GroupDao instance;

	public static synchronized GroupDao getInstance(Context ctx) {
		if (instance == null) {
			instance = new GroupDao(ctx);
		}
		return instance;
	}

	private MySQLiteOpenHelper dbHelper;

	/**
	 * 根据文件类型查询数据
	 * @param type
	 * @return
	 */
	public List<DbBean> queryByFileType(int type) {
		List<DbBean> list = new ArrayList<DbBean>();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.query("IP360_media_detail", null, "type=?", new String[] { type+"" }, null, null, "id desc");
		while (cursor.moveToNext()) {
			DbBean bean = new DbBean();
			bean.setTitle(cursor.getString(cursor.getColumnIndex("title")));
			bean.setId(cursor.getInt(cursor.getColumnIndex("id")));
			bean.setType(cursor.getInt(cursor.getColumnIndex("type")));
			bean.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
			bean.setFileSize(cursor.getString(cursor.getColumnIndex("fileSize")));
			bean.setResourceUrl(cursor.getString(cursor.getColumnIndex("resourceUrl")));
			bean.setRecordTime(cursor.getString(cursor.getColumnIndex("recordTime")));
			bean.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
			// bean.setJsonObject(cursor.getString(cursor.getColumnIndex("jsonObject")));
			list.add(bean);

		}
		 cursor.close();
		return list;
	}
	
	
	
	public List<DbBean> queryAll() {
		List<DbBean> list = new ArrayList<DbBean>();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.query("IP360_media_detail", null,null, null, null, null, "id desc");
		while (cursor.moveToNext()) {
			DbBean bean = new DbBean();
			bean.setTitle(cursor.getString(cursor.getColumnIndex("title")));
			bean.setId(cursor.getInt(cursor.getColumnIndex("id")));
			bean.setType(cursor.getInt(cursor.getColumnIndex("type")));
			bean.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
			bean.setFileSize(cursor.getString(cursor.getColumnIndex("fileSize")));
			bean.setResourceUrl(cursor.getString(cursor.getColumnIndex("resourceUrl")));
			bean.setRecordTime(cursor.getString(cursor.getColumnIndex("recordTime")));
			bean.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
			bean.setStatus(cursor.getString(cursor.getColumnIndex("status")));
			list.add(bean);
		}
		 cursor.close();
		return list;
	}
	
	
	}
