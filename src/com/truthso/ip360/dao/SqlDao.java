package com.truthso.ip360.dao;

import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.db.MySQLiteOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class SqlDao {

	private MySQLiteOpenHelper helper;
	@SuppressWarnings("unused")
	private Context ctx;
	public SqlDao(Context context) {
		ctx = context;
		helper = new MySQLiteOpenHelper(context);
	}

	private static SqlDao dao;

	public static  synchronized SqlDao getSQLiteOpenHelper(Context context) {
		if (dao == null) {
			dao = new SqlDao(context);
		}
		return dao;
	}

	/**
	 * 把数据保存到数据库
	 * 
	 * @param bean对象
	 */
	public void save(DbBean dbBean, String table) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("title", dbBean.getTitle());
		values.put("fileSize", dbBean.getFileSize());
		values.put("createTime", dbBean.getCreateTime());
		values.put("jsonObject", dbBean.getJsonObject());
		values.put("type", dbBean.getType());
		values.put("lable", dbBean.getLable());
		values.put("resourceUrl", dbBean.getResourceUrl());
		values.put("remark", dbBean.getRemark());
		db.insert(table, null, values);
		db.close();
		ctx.getContentResolver().notifyChange(Uri.parse("content://com.truthso.ip360/IP360_media_detail"), null);
	}

	/**
	 * 删
	 * 
	 * @param table
	 *            
	 * @param id
	 *            唯一ID，根据ID删除字段数据。
	 */
	public void delete(String table, int id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete(table, "id=?", new String[] { id + "" });
		db.close();
	}

	/**
	 * 改
	 * 
	 * @param 
	 *          
	 */
	public void update(DbBean dbBean) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("username", "bean对象要修改的");
		db.update("user", values, "id=?", new String[] { "要修改的字段的ID" + "" });
		db.close();
	}

	/**
	 * 查
	 * 
	 * @param id
	 *            需要查询的字段的ID值
	 * @return
	 */
	public DbBean queryById(int id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("IP360_media_detail", null, "id=?", new String[] { id +"" }, null, null, null);
		DbBean  dbBean= new DbBean();
		if (cursor.moveToNext()) {
			dbBean.setLable(cursor.getString(cursor.getColumnIndex("lable")));//标签
			dbBean.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));//生成日期
			dbBean.setTitle(cursor.getString(cursor.getColumnIndex("title")));//标题
			dbBean.setFileSize(cursor.getString(cursor.getColumnIndex("fileSize")));//文件大小
			dbBean.setType(cursor.getInt(cursor.getColumnIndex("type")));//1 照片2视频3录音
			dbBean.setJsonObject(cursor.getString(cursor.getColumnIndex("jsonObject")));//详细信息
			dbBean.setRecordTime(cursor.getString(cursor.getColumnIndex("recordTime")));//录制时长
			dbBean.setRemark(cursor.getString(cursor.getColumnIndex("remark")));//备注
			dbBean.setResourceUrl(cursor.getString(cursor.getColumnIndex("resourceUrl")));//资源路径
			
		}
		db.close();
		return dbBean;
	}
	

}
