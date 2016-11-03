package com.truthso.ip360.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.bean.DownLoadInfo;
import com.truthso.ip360.db.MySQLiteOpenHelper;
import com.truthso.ip360.updownload.FileInfo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class UpDownLoadDao {

	private static MySQLiteOpenHelper dbOpenHelper;
	private static UpDownLoadDao dao = new UpDownLoadDao(MyApplication.getApplication());
	private static SQLiteDatabase db;

	private UpDownLoadDao(Context context) {
		this.dbOpenHelper = new MySQLiteOpenHelper(context);
	}

	public static UpDownLoadDao getDao() {
		db = dbOpenHelper.getReadableDatabase();
		return dao;
	}

	public void save(String sourceid, File uploadFile) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into updownloadlog(uploadfilepath, sourceid) values(?,?)", new Object[] { uploadFile.getAbsolutePath(), sourceid });
	}

	public void save(int sourceid, String uploadFilePath) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into updownloadlog(uploadfilepath, sourceid) values(?,?)", new Object[] { uploadFilePath, sourceid });
	}

	public void delete(File uploadFile) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from updownloadlog where uploadfilepath=?", new Object[] { uploadFile.getAbsolutePath() });
	}

	public String getBindId(File uploadFile) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select sourceid from updownloadlog where uploadfilepath=?", new String[] { uploadFile.getAbsolutePath() });
		if (cursor.moveToFirst()) {
			return cursor.getString(0);
		}
		return null;
	}

	public String getPositionByUrl(String url) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select position from updownloadlog where downloadurl=?", new String[] { url });
		if (cursor.moveToFirst()) {
			return cursor.getString(0);
		}

		return null;
	}

	public void saveDownLoadInfo(String url, String fileName, String fileSize, int position, int resourceId) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		db.execSQL("insert into updownloadlog(downloadurl,filename,filesize,position,sourceid,downorupload) values(?,?,?,?,?,?)", new Object[] { url, fileName, fileSize, position, resourceId, "0" });
		MyApplication.getApplication().getContentResolver().notifyChange(Uri.parse("content://com.truthso.ip360/updownloadlog/down"), null);
	}

	public void saveUpLoadInfo(String url, String fileName, String fileSize, int position, int resourceId) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		db.execSQL("insert into updownloadlog(uploadfilepath,filename,filesize,position,sourceid,downorupload) values(?,?,?,?,?,?)",
				new Object[] { url, fileName, fileSize, position, resourceId, "1" });
		MyApplication.getApplication().getContentResolver().notifyChange(Uri.parse("content://com.truthso.ip360/updownloadlog/up"), null);
	}

	public void updateDownLoadProgress(String url, long position) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("update updownloadlog set position=? where downloadurl =?", new Object[] { position, url });
	}

	public void updateUpLoadProgress(String uploadfilepath , long position) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("update updownloadlog set position=? where uploadfilepath =?", new Object[] { position, uploadfilepath });
	}

	public List<FileInfo> queryDownLoadList() {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from updownloadlog where downorupload=?", new String[] { "0" });
		List<FileInfo> list = new ArrayList<FileInfo>();
		while (cursor.moveToNext()) {
			FileInfo info = new FileInfo();
			info.setResourceId(cursor.getInt(cursor.getColumnIndex("sourceid")));
			info.setFileName(cursor.getString(cursor.getColumnIndex("filename")));
			info.setFileSize(cursor.getString(cursor.getColumnIndex("filesize")));
			info.setFilePath(cursor.getString(cursor.getColumnIndex("downloadurl")));
			info.setPosition(cursor.getInt(cursor.getColumnIndex("position")));
			list.add(info);
		}
		return list;
	}

	public List<FileInfo> queryUpLoadList() {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from updownloadlog where downorupload=?", new String[] { "1" });
		List<FileInfo> list = new ArrayList<FileInfo>();

		while (cursor.moveToNext()) {
			FileInfo info = new FileInfo();
			info.setResourceId(cursor.getInt(cursor.getColumnIndex("sourceid")));
			info.setFileName(cursor.getString(cursor.getColumnIndex("filename")));
			info.setFileSize(cursor.getString(cursor.getColumnIndex("filesize")));
			info.setFilePath(cursor.getString(cursor.getColumnIndex("uploadfilepath")));
			info.setPosition(cursor.getInt(cursor.getColumnIndex("position")));
			list.add(info);

		}
		return list;
	}

	public FileInfo queryDownLoadInfoByResourceId(int resourceId) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from updownloadlog where sourceid=?", new String[] { resourceId+"" });
		FileInfo info = new FileInfo();
		cursor.moveToNext();

		info.setResourceId(cursor.getInt(cursor.getColumnIndex("sourceid")));
		info.setFileName(cursor.getString(cursor.getColumnIndex("filename")));
		info.setFileSize(cursor.getString(cursor.getColumnIndex("filesize")));
		info.setFilePath(cursor.getString(cursor.getColumnIndex("downloadurl")));
		info.setPosition(cursor.getInt(cursor.getColumnIndex("position")));

		return info;
	}

	public FileInfo queryUpLoadInfoByResourceId(int resourceId) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from updownloadlog where sourceid=?", new String[] { resourceId + "" });

		cursor.moveToNext();
		FileInfo info = new FileInfo();
		info.setResourceId(cursor.getInt(cursor.getColumnIndex("sourceid")));
		info.setFileName(cursor.getString(cursor.getColumnIndex("filename")));
		info.setFileSize(cursor.getString(cursor.getColumnIndex("filesize")));
		info.setFilePath(cursor.getString(cursor.getColumnIndex("uploadfilepath")));
		info.setPosition(cursor.getInt(cursor.getColumnIndex("position")));

		return info;
	}

	public void deleteDownInfoByResourceId(int resourceId) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from updownloadlog where sourceid=?", new Object[] {resourceId });
		MyApplication.getApplication().getContentResolver().notifyChange(Uri.parse("content://com.truthso.ip360/updownloadlog/down"), null);
	}

	public void deleteByResourceId(int resourceId) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from updownloadlog where sourceid=?", new Object[] { resourceId });
		MyApplication.getApplication().getContentResolver().notifyChange(Uri.parse("content://com.truthso.ip360/updownloadlog/up"), null);
	}

}
