package com.truthso.ip360.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.bean.WaituploadBean;
import com.truthso.ip360.db.MySQLiteOpenHelper;
import com.truthso.ip360.updownload.FileInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/4.
 */

public class WaituploadDao {
    private String TABLE_NAME="waitupload";
    private static MySQLiteOpenHelper dbOpenHelper;
    private static WaituploadDao dao = new WaituploadDao(
            MyApplication.getApplication());

    private WaituploadDao(Context context) {
        this.dbOpenHelper = new MySQLiteOpenHelper(context);
    }

    public static WaituploadDao getDao() {
        return dao;
    }

    /**
     * 增
     *
     * @param
     */
    public void save(FileInfo fileInfo) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("filepath", fileInfo.getFilePath());
        values.put("filetitle", fileInfo.getFileName());
        values.put("filetype", fileInfo.getType());
        values.put("filesize", fileInfo.getFileSize());
        values.put("hashcode", fileInfo.getHashCode());
        values.put("filedate", fileInfo.getFileCreatetime());
        values.put("filelocation", fileInfo.getFileLoc());
        values.put("filetime", fileInfo.getFileTime());
        values.put("latitudelongitude", fileInfo.getLatitudeLongitude());
        values.put("encrypte", fileInfo.getEncrypte());
        values.put("rsaid",fileInfo.getRsaId());
        values.put("mintime",fileInfo.getMinTime());
        db.insert(TABLE_NAME, null, values);
        db.close();

        MyApplication
                .getApplication()
                .getContentResolver()
                .notifyChange(
                        Uri.parse("content://com.truthso.ip360/updownloadlog/up"),
                        null);
        Log.i("djj",fileInfo.toString());
    }

    /**
     * 删
     *
     * @param id
     *            唯一ID，根据ID删除字段数据。
     */
    public void delete(int id) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.delete(TABLE_NAME, "_id=?", new String[] { String.valueOf(id)});
        db.close();
        MyApplication
                .getApplication()
                .getContentResolver()
                .notifyChange(
                        Uri.parse("content://com.truthso.ip360/updownloadlog/up"),
                        null);
    }

    /**
     * 改
     *
     * @param
     *
     */
    public void update(WaituploadBean bean) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", "bean对象要修改的");
        db.update(TABLE_NAME, values, "id=?", new String[] { "要修改的字段的ID" + "" });
        db.close();
    }

    /**
     * 通过ID查
     *
     * @param id
     *            需要查询的字段的ID值
     * @return
     */
    public FileInfo queryById(int id) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, "id=?", new String[] { id +"" }, null, null, null);
        FileInfo  info=null;
        if (cursor.moveToNext()) {
            info= new FileInfo();
            info.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            info.setFilePath(cursor.getString(cursor.getColumnIndex("filepath")));//文件路径
            info.setFileName(cursor.getString(cursor.getColumnIndex("filetitle")));//文件title
            info.setType(cursor.getInt(cursor.getColumnIndex("filetype")));//文件类型
            info.setFileSize(cursor.getString(cursor.getColumnIndex("fileSize")));//文件大小
            info.setHashCode(cursor.getString(cursor.getColumnIndex("hashcode")));//hashcode
            info.setFileCreatetime(cursor.getString(cursor.getColumnIndex("filedate")));//文件创建时间
            info.setFileLoc(cursor.getString(cursor.getColumnIndex("filelocation")));//文件地点
            info.setFileTime(cursor.getString(cursor.getColumnIndex("filetime")));//录制时长
            info.setLatitudeLongitude(cursor.getString(cursor.getColumnIndex("latitudelongitude")));
            info.setEncrypte(cursor.getString(cursor.getColumnIndex("encrypte")));//加签后的字符串
            info.setRsaId(cursor.getInt(cursor.getColumnIndex("rsaid")));//私钥id
            info.setMinTime(cursor.getInt(cursor.getColumnIndex("mintime")));//计费时长
        }
        db.close();
        return info;
    }

    /**
     * 查所有
     *
     * @return
     */
    public List<FileInfo> queryAll( ) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        List<FileInfo> list=new ArrayList<>();
        while (cursor.moveToNext()) {
            FileInfo info= new FileInfo();
            info.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            info.setFilePath(cursor.getString(cursor.getColumnIndex("filepath")));//文件路径
            info.setFileName(cursor.getString(cursor.getColumnIndex("filetitle")));//文件title
            info.setType(cursor.getInt(cursor.getColumnIndex("filetype")));//文件类型
            info.setFileSize(cursor.getString(cursor.getColumnIndex("filesize")));//文件大小
            info.setHashCode(cursor.getString(cursor.getColumnIndex("hashcode")));//hashcode
            info.setFileCreatetime(cursor.getString(cursor.getColumnIndex("filedate")));//文件创建时间
            info.setFileLoc(cursor.getString(cursor.getColumnIndex("filelocation")));//文件地点
            info.setFileTime(cursor.getString(cursor.getColumnIndex("filetime")));//录制时长
            info.setLatitudeLongitude(cursor.getString(cursor.getColumnIndex("latitudelongitude")));
            info.setEncrypte(cursor.getString(cursor.getColumnIndex("encrypte")));//加签后的字符串
            info.setRsaId(cursor.getInt(cursor.getColumnIndex("rsaid")));//私钥id
            info.setMinTime(cursor.getInt(cursor.getColumnIndex("mintime")));//计费时长
            info.setStatus(4);//状态码4等待重新上传
            list.add(info);
        }
        db.close();
        return list;
    }
}
