package com.truthso.ip360.dao;

import java.io.File;

import com.truthso.ip360.db.MySQLiteOpenHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UpDownLoadDao {

	private MySQLiteOpenHelper dbOpenHelper;  
    
    public UpDownLoadDao(Context context){  
        this.dbOpenHelper = new MySQLiteOpenHelper(context);  
    }  
      
    public void save(String sourceid, File uploadFile){  
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();  
        db.execSQL("insert into updownloadlog(uploadfilepath, sourceid) values(?,?)",  
                new Object[]{uploadFile.getAbsolutePath(),sourceid});  
    }  
      
    public void delete(File uploadFile){  
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();  
        db.execSQL("delete from updownloadlog where uploadfilepath=?", new Object[]{uploadFile.getAbsolutePath()});  
    }  
      
    public String getBindId(File uploadFile){  
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();  
        Cursor cursor = db.rawQuery("select sourceid from updownloadlog where uploadfilepath=?",   
                new String[]{uploadFile.getAbsolutePath()});  
        if(cursor.moveToFirst()){  
            return cursor.getString(0);  
        }  
        return null;  
    }  

}
