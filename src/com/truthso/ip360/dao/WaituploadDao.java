package com.truthso.ip360.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.bean.WaituploadBean;
import com.truthso.ip360.db.MySQLiteOpenHelper;

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
    public void save(WaituploadBean bean) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("filepath", bean.getFilePath());
        values.put("filetitle", bean.getFileTitle());
        values.put("filetype", bean.getFileType());
        values.put("filesize", bean.getFileSize());
        values.put("hashcode", bean.getHashCode());
        values.put("filedate", bean.getFileDate());
        values.put("filelocation", bean.getFileLocation());
        values.put("filetime", bean.getFileTime());
        values.put("latitudelongitude", bean.getLatitudeLongitude());
        values.put("prikey", bean.getPriKey());
        values.put("rsaid",bean.getRsaId());
        db.insert(TABLE_NAME, null, values);
        db.close();
        Log.i("djj",bean.toString());
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
    public WaituploadBean queryById(int id) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, "id=?", new String[] { id +"" }, null, null, null);
        WaituploadBean  bean=null;
        if (cursor.moveToNext()) {
            bean= new WaituploadBean();
            bean.setFilePath(cursor.getString(cursor.getColumnIndex("filepath")));//文件路径
            bean.setFileTitle(cursor.getString(cursor.getColumnIndex("filetitle")));//文件title
            bean.setFileType(cursor.getInt(cursor.getColumnIndex("filetype")));//文件类型
            bean.setFileSize(cursor.getString(cursor.getColumnIndex("fileSize")));//文件大小
            bean.setHashCode(cursor.getString(cursor.getColumnIndex("hashcode")));//hashcode
            bean.setFileDate(cursor.getString(cursor.getColumnIndex("filedate")));//文件创建时间
            bean.setFileLocation(cursor.getString(cursor.getColumnIndex("filelocation")));//文件地点
            bean.setFileTime(cursor.getString(cursor.getColumnIndex("filetime")));//录制时长
            bean.setLatitudeLongitude(cursor.getString(cursor.getColumnIndex("latitudelongitude")));
            bean.setPriKey(cursor.getString(cursor.getColumnIndex("prikey")));//加签后的字符串
            bean.setRsaId(cursor.getInt(cursor.getColumnIndex("rsaid")));//私钥id
        }
        db.close();
        return bean;
    }

    /**
     * 查所有
     *
     * @return
     */
    public List<WaituploadBean> queryAll( ) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        List<WaituploadBean> list=new ArrayList<>();
        if (cursor.moveToNext()) {
            WaituploadBean  bean= new WaituploadBean();
            bean.setFilePath(cursor.getString(cursor.getColumnIndex("filepath")));//文件路径
            bean.setFileTitle(cursor.getString(cursor.getColumnIndex("filetitle")));//文件title
            bean.setFileType(cursor.getInt(cursor.getColumnIndex("filetype")));//文件类型
            bean.setFileSize(cursor.getString(cursor.getColumnIndex("fileSize")));//文件大小
            bean.setHashCode(cursor.getString(cursor.getColumnIndex("hashcode")));//hashcode
            bean.setFileDate(cursor.getString(cursor.getColumnIndex("filedate")));//文件创建时间
            bean.setFileLocation(cursor.getString(cursor.getColumnIndex("filelocation")));//文件地点
            bean.setFileTime(cursor.getString(cursor.getColumnIndex("filetime")));//录制时长
            bean.setLatitudeLongitude(cursor.getString(cursor.getColumnIndex("latitudelongitude")));
            bean.setPriKey(cursor.getString(cursor.getColumnIndex("prikey")));//加签后的字符串
            bean.setRsaId(cursor.getInt(cursor.getColumnIndex("rsaid")));//私钥id
            list.add(bean);
        }
        db.close();
        return list;
    }
}
