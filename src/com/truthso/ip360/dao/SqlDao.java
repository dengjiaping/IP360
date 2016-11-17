package com.truthso.ip360.dao;

import java.util.ArrayList;
import java.util.List;

import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.db.MySQLiteOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class SqlDao {

	private static SqlDao dao=new SqlDao(MyApplication.getApplication());
	private MySQLiteOpenHelper helper;
	
	@SuppressWarnings("unused")
	public SqlDao(Context ctx) {

		this.helper = new MySQLiteOpenHelper(ctx);
	}
	
	public static  SqlDao getSQLiteOpenHelper() {
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
		values.put("location", dbBean.getLocation());
		values.put("status", dbBean.getStatus());//状态 0：正在上传  1上传完成  2上传失败
		values.put("llsize", dbBean.getLlsize());
		values.put("pkvalue", dbBean.getPkValue());
		values.put("fileformat", dbBean.getFileFormat());
		db.insert(table, null, values);
		db.close();
		MyApplication.getApplication().getContentResolver().notifyChange(Uri.parse("content://com.truthso.ip360/IP360_media_detail"), null);
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
		MyApplication.getApplication().getContentResolver().notifyChange(Uri.parse("content://com.truthso.ip360/IP360_media_detail"), null);
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
	
	
	public List<DbBean> searchByKey(String key) {
		SQLiteDatabase db = helper.getWritableDatabase();
		List<DbBean> list=new ArrayList<DbBean>();
		//Cursor cursor = db.query("IP360_media_detail", null, "id like ?", new String[] { key }, null, null, null);
		Cursor cursor = db.rawQuery("select * from IP360_media_detail where title like ?", new String[]{"%"+key+"%"});
		
		while (cursor.moveToNext()) {
			DbBean  dbBean= new DbBean();
			dbBean.setLable(cursor.getString(cursor.getColumnIndex("lable")));//标签
			dbBean.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));//生成日期
			dbBean.setTitle(cursor.getString(cursor.getColumnIndex("title")));//标题
			dbBean.setFileSize(cursor.getString(cursor.getColumnIndex("fileSize")));//文件大小
			dbBean.setType(cursor.getInt(cursor.getColumnIndex("type")));//1 照片2视频3录音
			dbBean.setJsonObject(cursor.getString(cursor.getColumnIndex("jsonObject")));//详细信息
			dbBean.setRecordTime(cursor.getString(cursor.getColumnIndex("recordTime")));//录制时长
			dbBean.setRemark(cursor.getString(cursor.getColumnIndex("remark")));//备注
			dbBean.setResourceUrl(cursor.getString(cursor.getColumnIndex("resourceUrl")));//资源路径
			dbBean.setStatus(cursor.getString(cursor.getColumnIndex("status")));
			list.add(dbBean);
		}
		
		db.close();
		return list;
	}

	//更改状态
	public void updateStatus(String name,String status) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("status", status);
		db.update("IP360_media_detail", values, "title=?", new String[] { name });
		db.close();			
		Uri uri=Uri.parse("content://com.truthso.ip360/IP360_media_detail");
		MyApplication.getApplication().getContentResolver().notifyChange(uri,null);
	}
	
	//查询所有
	public List<DbBean> queryAll() {
		List<DbBean> list = new ArrayList<DbBean>();
		SQLiteDatabase db = helper.getWritableDatabase();
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
			bean.setPkValue(cursor.getString(cursor.getColumnIndex("pkvalue")));
			bean.setFileFormat(cursor.getString(cursor.getColumnIndex("fileformat")));
			list.add(bean);
		}
		 cursor.close();
		return list;
	}


	//查询所有
	public List<DbBean> queryByType(String[] types) {
		List<DbBean> list = new ArrayList<DbBean>();
		SQLiteDatabase db = helper.getWritableDatabase();
	//	Cursor cursor = db.query("IP360_media_detail", null,null, null, null, null, "id desc");

		Cursor cursor = db.query("IP360_media_detail",null ,"type=? or type=?", types, null, null, "id desc");
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
			bean.setPkValue(cursor.getString(cursor.getColumnIndex("pkvalue")));
			bean.setFileFormat(cursor.getString(cursor.getColumnIndex("fileformat")));
			list.add(bean);
		}
		cursor.close();
		return list;
	}
}
