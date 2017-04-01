package com.truthso.ip360.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.bean.DownLoadInfo;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.db.MySQLiteOpenHelper;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.utils.SharePreferenceUtil;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class UpDownLoadDao {

	private static MySQLiteOpenHelper dbOpenHelper;
	private static UpDownLoadDao dao = new UpDownLoadDao(
			MyApplication.getApplication());

	private UpDownLoadDao(Context context) {
		this.dbOpenHelper = new MySQLiteOpenHelper(context);
	}

	public static UpDownLoadDao getDao() {
		return dao;
	}

	public void save(String sourceid, File uploadFile) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL(
				"insert into updownloadlog(uploadfilepath, sourceid) values(?,?)",
				new Object[] { uploadFile.getAbsolutePath(), sourceid });
	}

	public void save(int sourceid, String uploadFilePath) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL(
				"insert into updownloadlog(uploadfilepath, sourceid) values(?,?)",
				new Object[] { uploadFilePath, sourceid });
	}

	public void delete(File uploadFile) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from updownloadlog where uploadfilepath=?",
				new Object[] { uploadFile.getAbsolutePath() });
	}

	public void deleteAll() {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from updownloadlog");
		MyApplication
				.getApplication()
				.getContentResolver()
				.notifyChange(
						Uri.parse("content://com.truthso.ip360/updownloadlog/down"),
						null);
		MyApplication
				.getApplication()
				.getContentResolver()
				.notifyChange(
						Uri.parse("content://com.truthso.ip360/updownloadlog/up"),
						null);

	}

	public String getBindId(File uploadFile) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select sourceid from updownloadlog where uploadfilepath=?",
				new String[] { uploadFile.getAbsolutePath() });
		if (cursor.moveToFirst()) {
			return cursor.getString(0);
		}
		return null;
	}

	public String getPositionByUrl(String url) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select position from updownloadlog where downloadurl=?",
				new String[] { url });
		if (cursor.moveToFirst()) {
			return cursor.getString(0);
		}

		return null;
	}

	public void saveDownLoadInfo(String url, String fileName, String fileSize,
			int position, int resourceId, String objectkey,String llsize,String fileurlformatname,int dataType,int status,String remark) {
		int userId=(Integer) SharePreferenceUtil.getAttributeByKey(MyApplication.getApplication(), MyConstants.SP_USER_KEY, "userId", SharePreferenceUtil.VALUE_IS_INT);
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL(
				"insert into updownloadlog(downloadurl,filename,filesize,position,sourceid,downorupload,objectkey,llsize,fileurlformatname,userId,dataType,status,remark) values(?,?,?,?,?,?,?,?,?,?,?,?,?)",
				new Object[] { url, fileName, fileSize, position, resourceId,
						"0", objectkey ,llsize,fileurlformatname,userId,dataType,status,remark});
		MyApplication
				.getApplication()
				.getContentResolver()
				.notifyChange(
						Uri.parse("content://com.truthso.ip360/updownloadlog/down"),
						null);
	}

	public void saveUpLoadInfo(String url, String fileName, String fileSize,
			int position, int resourceId, String objectkey,int status) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		int userId=(Integer) SharePreferenceUtil.getAttributeByKey(MyApplication.getApplication(), MyConstants.SP_USER_KEY, "userId", SharePreferenceUtil.VALUE_IS_INT);
		db.execSQL(
				"insert into updownloadlog(uploadfilepath,filename,filesize,position,sourceid,downorupload,objectkey,userId,status) values(?,?,?,?,?,?,?,?,?)",
				new Object[] { url, fileName, fileSize, position, resourceId,
						"1", objectkey,userId ,status});

		MyApplication
				.getApplication()
				.getContentResolver()
				.notifyChange(
						Uri.parse("content://com.truthso.ip360/updownloadlog/up"),
						null);
	}

	public void updateDownLoadProgress(String objectkey, long position) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("update updownloadlog set position=? where objectkey =?",
				new Object[] { position, objectkey });
	}

	public void updateUpLoadProgress(String uploadfilepath, long position) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL(
				"update updownloadlog set position=? where uploadfilepath =?",
				new Object[] { position, uploadfilepath });
	}

	public List<FileInfo> queryDownLoadList() {
		int userId=(Integer) SharePreferenceUtil.getAttributeByKey(MyApplication.getApplication(), MyConstants.SP_USER_KEY, "userId", SharePreferenceUtil.VALUE_IS_INT);
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select * from updownloadlog where downorupload=? and userId=?",
				new String[] { "0" ,userId+""});
		List<FileInfo> list = new ArrayList<FileInfo>();

		while (cursor.moveToNext()) {

			FileInfo info = new FileInfo();
			info.setResourceId(cursor.getInt(cursor.getColumnIndex("sourceid")));
			info.setFileName(cursor.getString(cursor.getColumnIndex("filename")));
			info.setFileSize(cursor.getString(cursor.getColumnIndex("filesize")));
			info.setFilePath(cursor.getString(cursor
					.getColumnIndex("downloadurl")));
			info.setLlsize(cursor.getString(cursor.getColumnIndex("llsize")));
			info.setPosition(cursor.getInt(cursor.getColumnIndex("position")));
			info.setObjectKey(cursor.getString(cursor
					.getColumnIndex("objectkey")));
			info.setFileUrlFormatName(cursor.getString(cursor.getColumnIndex("fileurlformatname")));
			info.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
			info.setReMark(cursor.getString(cursor.getColumnIndex("remark")));
			list.add(info);
		}
		return list;
	}
	public List<FileInfo> queryDownLoadListUnComplete() {
		int userId=(Integer) SharePreferenceUtil.getAttributeByKey(MyApplication.getApplication(), MyConstants.SP_USER_KEY, "userId", SharePreferenceUtil.VALUE_IS_INT);
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select * from updownloadlog where downorupload=? and userId=? and status<>?",
				new String[] { "0" ,userId+"","0"});
		List<FileInfo> list = new ArrayList<FileInfo>();

		while (cursor.moveToNext()) {

			FileInfo info = new FileInfo();
			info.setResourceId(cursor.getInt(cursor.getColumnIndex("sourceid")));
			info.setFileName(cursor.getString(cursor.getColumnIndex("filename")));
			info.setFileSize(cursor.getString(cursor.getColumnIndex("filesize")));
			info.setFilePath(cursor.getString(cursor
					.getColumnIndex("downloadurl")));
			info.setLlsize(cursor.getString(cursor.getColumnIndex("llsize")));
			info.setPosition(cursor.getInt(cursor.getColumnIndex("position")));
			info.setObjectKey(cursor.getString(cursor
					.getColumnIndex("objectkey")));
			info.setFileUrlFormatName(cursor.getString(cursor.getColumnIndex("fileurlformatname")));
			info.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
			info.setReMark(cursor.getString(cursor.getColumnIndex("remark")));
			list.add(info);
		}
		return list;
	}

	public List<FileInfo> queryUpLoadList() {
		int userId=(Integer) SharePreferenceUtil.getAttributeByKey(MyApplication.getApplication(), MyConstants.SP_USER_KEY, "userId", SharePreferenceUtil.VALUE_IS_INT);
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select * from updownloadlog where downorupload=? and userId=?",
				new String[] { "1",userId+"" });

		List<FileInfo> list = new ArrayList<FileInfo>();
		while (cursor.moveToNext()) {
			FileInfo info = new FileInfo();
			info.setResourceId(cursor.getInt(cursor.getColumnIndex("sourceid")));
			info.setFileName(cursor.getString(cursor.getColumnIndex("filename")));
			info.setFileSize(cursor.getString(cursor.getColumnIndex("filesize")));
//			info.setLlsize(cursor.getString(cursor.getColumnIndex("llsize")));
			info.setFilePath(cursor.getString(cursor
					.getColumnIndex("uploadfilepath")));
			info.setPosition(cursor.getInt(cursor.getColumnIndex("position")));
			info.setObjectKey(cursor.getString(cursor.getColumnIndex("objectkey")));
			info.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
			info.setFileUrlFormatName(cursor.getString(cursor.getColumnIndex("fileurlformatname")));

			list.add(info);
		}
		return list;
	}

	public List<FileInfo> queryUpLoadListUnComplete() {
		int userId=(Integer) SharePreferenceUtil.getAttributeByKey(MyApplication.getApplication(), MyConstants.SP_USER_KEY, "userId", SharePreferenceUtil.VALUE_IS_INT);
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select * from updownloadlog where downorupload=? and userId=? and status<>? ",
				new String[] { "1",userId+"","0"});

		List<FileInfo> list = new ArrayList<FileInfo>();
		while (cursor.moveToNext()) {
			FileInfo info = new FileInfo();
			info.setResourceId(cursor.getInt(cursor.getColumnIndex("sourceid")));
			info.setFileName(cursor.getString(cursor.getColumnIndex("filename")));
			info.setFileSize(cursor.getString(cursor.getColumnIndex("filesize")));
//			info.setLlsize(cursor.getString(cursor.getColumnIndex("llsize")));
			info.setFilePath(cursor.getString(cursor
					.getColumnIndex("uploadfilepath")));
			info.setPosition(cursor.getInt(cursor.getColumnIndex("position")));
			info.setObjectKey(cursor.getString(cursor.getColumnIndex("objectkey")));
			info.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
			info.setFileUrlFormatName(cursor.getString(cursor.getColumnIndex("fileurlformatname")));

			list.add(info);
		}
		return list;
	}



	public FileInfo queryDownLoadInfoByResourceId(int resourceId) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select * from updownloadlog where sourceid=?",
				new String[] { resourceId + "" });
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
		Cursor cursor = db.rawQuery(
				"select * from updownloadlog where sourceid=?",
				new String[] { resourceId + "" });

		cursor.moveToNext();
		FileInfo info = new FileInfo();
		info.setResourceId(cursor.getInt(cursor.getColumnIndex("sourceid")));
		info.setFileName(cursor.getString(cursor.getColumnIndex("filename")));
		info.setFileSize(cursor.getString(cursor.getColumnIndex("filesize")));
		info.setFilePath(cursor.getString(cursor
				.getColumnIndex("uploadfilepath")));
		info.setPosition(cursor.getInt(cursor.getColumnIndex("position")));
		info.setObjectKey(cursor.getString(cursor.getColumnIndex("objectkey")));
		return info;
	}

	public void deleteDownInfoByObjectKey(String objectkey) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from updownloadlog where objectkey=?",
				new Object[] { objectkey });
		MyApplication
				.getApplication()
				.getContentResolver()
				.notifyChange(
						Uri.parse("content://com.truthso.ip360/updownloadlog/down"),
						null);
	}

	public void deleteDownInfoByResourceId(String resourceId) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.enableWriteAheadLogging();
		db.execSQL("delete from updownloadlog where sourceid=?",
				new Object[] { resourceId });
		MyApplication
				.getApplication()
				.getContentResolver()
				.notifyChange(
						Uri.parse("content://com.truthso.ip360/updownloadlog/down"),
						null);
	}

	public void deleteUpInfoByResourceId(String resourceId) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.enableWriteAheadLogging();
		db.execSQL("delete from updownloadlog where sourceid=?",
				new Object[] { resourceId });
		MyApplication
				.getApplication()
				.getContentResolver()
				.notifyChange(
						Uri.parse("content://com.truthso.ip360/updownloadlog/up"),
						null);
	}

	public void updateStatusByResourceId(String status,String resourceId) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.enableWriteAheadLogging();
		db.execSQL(
				"update updownloadlog set status=? where sourceid =?",
				new Object[] { status,resourceId });
		MyApplication
				.getApplication()
				.getContentResolver()
				.notifyChange(
						Uri.parse("content://com.truthso.ip360/updownloadlog/down"),
						null);
	}

	public void deleteByResourceId(int resourceId) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from updownloadlog where sourceid=?",
				new Object[] { resourceId });
		MyApplication
				.getApplication()
				.getContentResolver()
				.notifyChange(
						Uri.parse("content://com.truthso.ip360/updownloadlog/up"),
						null);
	}

	public void deleteUploadInfoByUrl(String url) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from updownloadlog where uploadfilepath=?",
				new Object[] { url });
		MyApplication
				.getApplication()
				.getContentResolver()
				.notifyChange(
						Uri.parse("content://com.truthso.ip360/updownloadlog/up"),
						null);
	}

	public void updateUploadInfoByUrl(String url,int status) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("update updownloadlog set status =? where uploadfilepath=?",
				new Object[] { status,url});
		MyApplication
				.getApplication()
				.getContentResolver()
				.notifyChange(
						Uri.parse("content://com.truthso.ip360/updownloadlog/up"),
						null);

	}

	public boolean queryByPkValue(int pkvalue) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		Cursor cursor = db.query("updownloadlog", null, "sourceid=?", new String[] { pkvalue+"" }, null, null, null);

		boolean moveToNext = cursor.moveToNext();
		db.close();
		return moveToNext;
	}
}
